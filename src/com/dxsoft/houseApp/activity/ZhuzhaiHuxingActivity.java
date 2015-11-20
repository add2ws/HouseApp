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

public class ZhuzhaiHuxingActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuzhai_huxing);
		button = (Button) findViewById(R.id.chaxun);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhuzhaiHuxingActivity.this.finish();
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETHXFB, C.Url, param[0]);
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
					Toast.makeText(ZhuzhaiHuxingActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("rows");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String name = jsonObject.get("name").toString();
							String ssts = jsonObject.get("ssts").toString();
							String ssljts = jsonObject.get("ssljts").toString();
							double ssbl = Double.parseDouble(jsonObject.get("ssbl").toString());
							String xsts = jsonObject.get("xsts").toString();
							String xsljts = jsonObject.get("xsljts").toString();
							double xsbl = Double.parseDouble(jsonObject.get("xsbl").toString());
							String ksts = jsonObject.get("ksts").toString();
							double ksbl = Double.parseDouble(jsonObject.get("ksbl").toString());
							map.put("name",name);
							map.put("ssts",ssts);
							map.put("ssljts",ssljts);
							map.put("ssbl",df.format(ssbl));
							map.put("xsts",xsts);
							map.put("xsljts",xsljts);
							map.put("xsbl",df.format(xsbl));
							map.put("ksts",ksts);
							map.put("ksbl",df.format(ksbl));
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(ZhuzhaiHuxingActivity.this, list,
								R.layout.zhuzhai_item_huxing, new String[]
										{"name","ssts","ssljts","ssbl","xsts","xsljts","xsbl","ksts","ksbl"}, new int[]
										{R.id.zhuzhai_type,R.id.ssts,R.id.ssljts,R.id.ssbl,R.id.xsts,R.id.xsljts,R.id.xsbl,R.id.ksts,R.id.ksbl});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
