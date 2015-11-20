package com.dxsoft.houseApp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.Base64;
import com.google.gson.Gson;
import com.rop.utils.RopUtils;

public abstract class HttpHelper {

	private final static String TAG = "HttpHelper";
	private static CookieStore cookieStore;
 
	public static String invoke(String url, List<NameValuePair> params) {
		String result = null;
		try {
			Log.d(TAG, "url is" + url);

			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Authorization", String.format(
					"Basic %s",
					new String(Base64.encode(String.format("%s:%s", "iHealth",
							"secret").getBytes("UTF-8")), "UTF-8")));
			httpPost.addHeader("username", "true");
			
			
			if (params != null && params.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);
			}

			DefaultHttpClient httpClient = new DefaultHttpClient();

			// 添加Cookie
			if (cookieStore != null) {
				httpClient.setCookieStore(cookieStore);
			}
			  String name="";
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getLastHeader("username")!=null){
        name =  httpResponse.getLastHeader("username").getValue();
        Log.i("HttpHelper---->", "登录返回的uid:"+name);
           
			}

        String t="\""+"username"+"\""+":"+"\""+name+"\"";
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
				//builder.append(t);
			}
			
			result = builder.toString();
			
			Log.d(TAG, "result is ( " + result + " )");
			// 保存Cookie
//			cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		Log.d(TAG, "over");
		return result;
	}

	public static String invoke(String url) {
		return invoke(url, null);
	}

	/**
	 * @descrption 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * @param controllerName
	 *            .NETMVC中的controllerName
	 * @param actionName
	 *            .NETMVC中的actionName
	 * @param params
	 *            key：为C#方法接受的参数，value为参数�??
	 * @param files
	 *            key：为C#方法接受的参数，不要重复，value为参数�??
	 * @return
	 * @throws IOException
	 */
	public static String httpPostByte(String url, Map<String, String> params, Map<String, byte[]> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时�?
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓�?
		conn.setRequestMethod("POST");
		
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参�?
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
//		// 发�?�文件数�?
//		if (files != null) {
//			for (Map.Entry<String, byte[]> file : files.entrySet()) {
//				StringBuilder sb1 = new StringBuilder();
//				sb1.append(PREFIX);
//				sb1.append(BOUNDARY);
//				sb1.append(LINEND);
//				sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getKey() + "\"" + LINEND);
//				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
//				sb1.append(LINEND);
//				outStream.write(sb1.toString().getBytes());
//
//				// 这里如何是File对象，则写成这种形式
//				// InputStream is = new FileInputStream(file.getValue());
//				// byte[] buffer = new byte[1024];
//				// int len = 0;
//				// while ((len = is.read(buffer)) != -1) {
//				// outStream.write(buffer, 0, len);
//				// }
//
//				outStream.write(file.getValue());
//
//				// is.close();
//				outStream.write(LINEND.getBytes());
//			}
//		}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应�?
		int res = conn.getResponseCode();
		InputStream in = null;
		if (res == 200) {
			in = conn.getInputStream();
		conn.getHeaderField(""
				+ ""
				+ ""
				+ "username");
			
			int ch;
			StringBuilder sb2 = new StringBuilder();
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		return in == null ? null : in.toString();
	}
	
	
	public static String getData(String url) {
		try {
			URL u = new URL(url);
			Log.i("url", url);

			URLConnection ucon = u.openConnection();
			ucon.setConnectTimeout(10 * 1000);
			ucon.setReadTimeout(20 * 1000);
			InputStream inStream = ucon.getInputStream();
			StringBuffer xmlbuf = new StringBuffer();
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(inStream, "GBK"));// 不加encoding,将以平台默认编码方式进行解析，可能导致中文乱�?
			String inputLine = null;
			while ((inputLine = bufreader.readLine()) != null) {
				xmlbuf.append(inputLine + "\r\n");
				// // System.out.println(inputLine);
			} // win平台下的回车换行
			bufreader.close();
			// System.out.println("网络数据�?" + xmlbuf.toString());
			return xmlbuf.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* 获取网址内容
	* @param url
	* @return
	* @throws Exception
	*/
	public static String getContent(String url) throws Exception{
	    StringBuilder sb = new StringBuilder();
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    //设置网络超时参数
	    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
	    HttpConnectionParams.setSoTimeout(httpParams, 20000);
	    HttpResponse response = client.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
	        String line = null;
	        while ((line = reader.readLine())!= null){
	            sb.append(line + "\n");
	        }
	        reader.close();
	    }
	    return sb.toString();
	} 
	
	
	public static String getUrl(String method, String url, Parameter parameter) {
		Gson gson = new Gson();  
		Map<String, String> form1 = new HashMap<String, String>();
		form1.put("method", method);// <--指定方法名称
		form1.put("appKey", C.APPKEY);
		form1.put("sessionId", C.SESSIONID);
		form1.put("v", C.V);
		form1.put("messageFormat", C.MESSAGEFORMAT);
		form1.put("paramValue", gson.toJson(parameter).toString());
		// 对请求参数列表进行签�?
		String sign = RopUtils.sign(form1, C.SECRET);
		form1.put("sign", sign);
		
		url += "/" + method + ".do?" + "appKey="+form1.get("appKey")+"&sessionId="+form1.get("sessionId")
				+"&v="+form1.get("v")+"&messageFormat="+form1.get("messageFormat")+"&paramValue="+form1.get("paramValue")
				+"&sign="+form1.get("sign");
		
		return url;
	}
	
	
	public static String getWebData(String url) {
		double startTime =  System.currentTimeMillis(), endTime = startTime;
		try {
			URL u = new URL(url);
			Log.i("url", url);
			HttpURLConnection ucon = (HttpURLConnection) u.openConnection();
			ucon.setConnectTimeout(20 * 1000);
			ucon.setReadTimeout(20 * 1000);
			ucon.setRequestMethod("POST");
			ucon.setDoOutput(true);
			ucon.setUseCaches(false);
			InputStream inStream = ucon.getInputStream();
			StringBuffer xmlbuf = new StringBuffer();
			BufferedReader bufreader = new BufferedReader( new InputStreamReader(inStream, "utf-8"));// 不加encoding,将以平台默认编码方式进行解析，可能导致中文乱�?
			String inputLine = null;
			while ((inputLine = bufreader.readLine()) != null) {
				xmlbuf.append(inputLine + "\r\n");
				// // System.out.println(inputLine);
			} // win平台下的回车换行
			bufreader.close();
			// System.out.println("网络数据�?" + xmlbuf.toString());
			return xmlbuf.toString();
		} catch(Exception e) {
			e.printStackTrace();
			Log.i("url", e.getMessage());
			return e.getMessage();
		} finally {
			endTime =  System.currentTimeMillis();
			Log.i("url", "请求耗时:" + (endTime - startTime) /1000);
		}
	}
	
	public static String requestData(String method, String url_, Parameter parameter) throws IOException {
		double startTime =  System.currentTimeMillis(), endTime = startTime;
		String url = getUrl(method, url_, parameter);
		URL u = new URL(url);
		Log.i("url", url);
		HttpURLConnection ucon = (HttpURLConnection) u.openConnection();
		ucon.setConnectTimeout(20 * 1000);
		ucon.setReadTimeout(20 * 1000);
		ucon.setRequestMethod("POST");
		ucon.setDoOutput(true);
		ucon.setUseCaches(false);
		InputStream inStream = ucon.getInputStream();
		StringBuffer xmlbuf = new StringBuffer();
		BufferedReader bufreader = new BufferedReader( new InputStreamReader(inStream, "utf-8"));// 不加encoding,将以平台默认编码方式进行解析，可能导致中文乱�?
		String inputLine = null;
		while ((inputLine = bufreader.readLine()) != null) {
			xmlbuf.append(inputLine + "\r\n");
			// // System.out.println(inputLine);
		} // win平台下的回车换行
		bufreader.close();
		// System.out.println("网络数据�?" + xmlbuf.toString());
		String result = xmlbuf.toString();
		Log.i("url", result);
		endTime =  System.currentTimeMillis();
		Log.i("url", "请求耗时:" + (endTime - startTime) /1000);
		return result;
	}
}