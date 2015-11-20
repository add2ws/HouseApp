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

public class ZhuzhaiTxGjgActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuzhai_txgqjg);
		button = (Button) findViewById(R.id.chaxun);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhuzhaiTxGjgActivity.this.finish();
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETTXGQJGB, C.Url, param[0]);
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
					Toast.makeText(ZhuzhaiTxGjgActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("rows");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String mianji = jsonObject.get("mianji").toString();
							String ysmj = jsonObject.get("ysmj").toString();
							double ysmjzb = Double.parseDouble(jsonObject.get("ysmjzb").toString());
							String ksmj = jsonObject.get("ksmj").toString();
							double ksmjzb = Double.parseDouble(jsonObject.get("ksmjzb").toString());
							String djmj = jsonObject.get("djmj").toString();
							double djmjzb = Double.parseDouble(jsonObject.get("djmjzb").toString());
							String ysts = jsonObject.get("ysts").toString();
							double ystszb = Double.parseDouble(jsonObject.get("ystszb").toString());
							String ksts = jsonObject.get("ksts").toString();
							double kstszb = Double.parseDouble(jsonObject.get("kstszb").toString());
							String djts = jsonObject.get("djts").toString();
							double djtszb = Double.parseDouble(jsonObject.get("djtszb").toString());
							String djxs = jsonObject.get("djxs").toString();
							double djzb = Double.parseDouble(jsonObject.get("djzb").toString());
							map.put("mianji",mianji+"/m²");
							map.put("ysmjzb",df.format(ysmjzb));
							map.put("ysmj",ysmj);
							map.put("ksmjzb",df.format(ksmjzb));
							map.put("ksmj",ksmj);
							map.put("djmjzb",df.format(djmjzb));
							map.put("djmj",djmj);
							map.put("ystszb",df.format(ystszb));
							map.put("ysts",ysts);
							map.put("kstszb",df.format(kstszb));
							map.put("ksts",ksts);
							map.put("djtszb",df.format(djtszb));
							map.put("djts",djts);
							map.put("djzb",df.format(djzb));
							map.put("djxs",djxs);
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(ZhuzhaiTxGjgActivity.this, list,
								R.layout.zhuzhai_item_txgqjg, new String[]
										{"mianji","ysmjzb","ysmj","ksmjzb","ksmj","djmjzb","djmj","ystszb","ysts","kstszb","ksts","djtszb","djts","djzb","djxs"}, new int[]
										{R.id.zhuzhai_type,R.id.ysmjzb,R.id.ysmj,R.id.ksmjzb,R.id.ksmj,R.id.djmjzb,R.id.djmj,R.id.ystszb,
											R.id.ysts,R.id.kstszb,R.id.ksts,R.id.djtszb,R.id.djts,R.id.djzb,R.id.djxs});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
