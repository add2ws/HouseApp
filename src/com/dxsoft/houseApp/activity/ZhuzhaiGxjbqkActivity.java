package com.dxsoft.houseApp.activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HouseUtil;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.util.MyAdapter;
import com.dxsoft.houseApp.R;

public class ZhuzhaiGxjbqkActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuzhai_gxjbqk);
		button = (Button) findViewById(R.id.chaxun);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhuzhaiGxjbqkActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		textDate = (EditText) findViewById(R.id.editText1);
		HouseUtil.toMonthPicker(textDate);
		listview = (ListView) findViewById(R.id.lv_listview);
		
		button.setOnClickListener(new OnClickListener() {
			
	        
			@Override
			public void onClick(View arg0) {
				String onMonth = textDate.getText().toString();
				Parameter p = new Parameter();
				p.setDate(onMonth);

				button.setEnabled(false);
				button.setText(R.string.query_loading_name);
				new readData().execute(p);
			}
		});
		
	}
	
	//客服端获取值传给服务端后台
		class readData extends AsyncTask<Parameter, Void, String>{

			private String url = "";
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
			}
			
			@Override
			protected String doInBackground(Parameter... param) {
				String result = null;
				try {
					result = HttpHelper.requestData(HouseConfig.METHOD_GETGGXJBQK, C.Url, param[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			}
			
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				button.setEnabled(true);
				button.setText(R.string.query_button_name);
				DecimalFormat df = new DecimalFormat("0.00");
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(ZhuzhaiGxjbqkActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("rows");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String house = jsonObject.get("house").toString();
							String ysmj = jsonObject.get("ysmj").toString();
							String ksmj = jsonObject.get("ksmj").toString();
							String djmj = jsonObject.get("djmj").toString();
							String djys = jsonObject.get("djys").toString();
							String djxs = jsonObject.get("djxs").toString();
							String jj = jsonObject.get("jj").toString();
							double ysYearbl = Double.parseDouble(jsonObject.get("ysYearbl").toString());
							double ysMonthbl = Double.parseDouble(jsonObject.get("ysMonthbl").toString());
							double ksYearbl = Double.parseDouble(jsonObject.get("ksYearbl").toString());
							double ksMonthbl = Double.parseDouble(jsonObject.get("ksMonthbl").toString());
							double djYearbl = Double.parseDouble(jsonObject.get("djYearbl").toString());
							double djMonthbl = Double.parseDouble(jsonObject.get("djMonthbl").toString());
							double jjYearbl = Double.parseDouble(jsonObject.get("jjYearbl").toString());
							double jjMonthbl = Double.parseDouble(jsonObject.get("jjMonthbl").toString());
							double djystb = Double.parseDouble(jsonObject.get("djystb").toString());
							double djyshb = Double.parseDouble(jsonObject.get("djyshb").toString());
							double djxstb = Double.parseDouble(jsonObject.get("djxstb").toString());
							double djxshb = Double.parseDouble(jsonObject.get("djxshb").toString());
							map.put("house",house);
							map.put("ysmj",ysmj);
							map.put("ksmj",ksmj);
							map.put("djmj",djmj);
							map.put("djys",djys);
							map.put("djxs",djxs);
							map.put("ysYearbl",df.format(ysYearbl));
							map.put("ysMonthbl",df.format(ysMonthbl));
							map.put("ksYearbl",df.format(ksYearbl));
							map.put("ksMonthbl",df.format(ksMonthbl));
							map.put("djYearbl",df.format(djYearbl));
							map.put("djMonthbl",df.format(djMonthbl));
							map.put("jjYearbl",df.format(jjYearbl));
							map.put("jjMonthbl",df.format(jjMonthbl));
							map.put("djystb",df.format(djystb));
							map.put("djyshb",df.format(djyshb));
							map.put("djxstb",df.format(djxstb));
							map.put("djxshb",df.format(djxshb));
							map.put("jj", jj);
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(ZhuzhaiGxjbqkActivity.this, list,
								R.layout.zhuzhai_item_gxjbqk, new String[]
										{"house","ysmj","ksmj","djmj","djys","djxs","jj","ysYearbl","ysMonthbl","ksYearbl","ksMonthbl","djYearbl"
										,"djMonthbl","jjYearbl","jjMonthbl","djystb","djyshb","djxstb","djxshb"}, new int[]
										{R.id.zhuzhai_type,R.id.ysmj,R.id.ksmj,R.id.djmj,R.id.djys,R.id.djxs,R.id.jj,R.id.ysYearbl,R.id.ysMonthbl
												,R.id.ksYearbl,R.id.ksMonthbl,R.id.djYearbl,R.id.djMonthbl,R.id.jjYearbl,R.id.jjMonthbl
												,R.id.djystb,R.id.djyshb,R.id.djxstb,R.id.djxshb});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
