package com.dxsoft.houseApp.activity;

import java.io.IOException;
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

public class ZhuzhaiTypeActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuzhai_area);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhuzhaiTypeActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		button = (Button) findViewById(R.id.chaxun);
		textDate = (EditText) findViewById(R.id.editText1);
		HouseUtil.toMonthPicker(textDate);
		listview = (ListView) findViewById(R.id.zhuzhai_listview);
		
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETPTYPE, C.Url, param[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			}
			
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				button.setEnabled(true);
				button.setText(R.string.query_button_name);
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(ZhuzhaiTypeActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("data");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String onDate = jsonObject.get("onDate").toString();
							String num1 = jsonObject.get("a1").toString();
							String num2 = jsonObject.get("a2").toString();
							String num3 = jsonObject.get("a3").toString();
							String num4 = jsonObject.get("a4").toString();
							String num5 = jsonObject.get("a5").toString();
							String num6 = jsonObject.get("a6").toString();
							String num7 = jsonObject.get("a7").toString();
							String num8 = jsonObject.get("a8").toString();
							
							map.put("onDate",onDate);
							map.put("num1",num1);
							map.put("num2",num2);
							map.put("num3",num3);
							map.put("num4",num4);
							map.put("num5",num5);
							map.put("num6",num6);
							map.put("num7",num7);
							map.put("num8",num8);
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(ZhuzhaiTypeActivity.this, list,
								R.layout.zhuzhai_item_type, new String[]{"onDate","num1","num2","num3","num4","num5","num6","num7","num8"}, new int[]{R.id.zhuzhai_date,R.id.num1,R.id.num2,R.id.num3,R.id.num4,R.id.num5,R.id.num6,R.id.num7,R.id.num8});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
