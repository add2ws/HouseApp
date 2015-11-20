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

import com.dxsoft.houseApp.activity.module.FqxsylbActivity;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HouseUtil;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.util.MyAdapter;
import com.dxsoft.houseApp.R;

public class TableksqkActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_ksqk);
		button = (Button) findViewById(R.id.chaxun);
		backBtn = (RelativeLayout) findViewById(R.id.backlayout);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TableksqkActivity.this.finish();
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETKSQK, C.Url, param[0]);
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
					Toast.makeText(TableksqkActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("rows");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						String buildingType=null;
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String building = jsonObject.get("buildingType").toString();
							if(building.equals("0")){
								buildingType="全市";
							}else if(building.equals("2")){
								buildingType="其中：多层";
							}else if(building.equals("4")){
								buildingType="高层";
							}
							double zzmj = Double.parseDouble(jsonObject.get("zzmj").toString());
							double zzmjtb = Double.parseDouble(jsonObject.get("zzmjtb").toString());
							String zzts = jsonObject.get("zzts").toString();
							double zztstb = Double.parseDouble(jsonObject.get("zztstb").toString());
							double fzzmj = Double.parseDouble(jsonObject.get("fzzmj").toString());
							double fzzmjtb = Double.parseDouble(jsonObject.get("fzzmjtb").toString());
							map.put("buildingType",buildingType);
							map.put("zzmj",df.format(zzmj/10000));
							map.put("zzmjtb",df.format(zzmjtb));
							map.put("zzts",zzts);
							map.put("zztstb",df.format(zztstb));
							map.put("fzzmj",df.format(fzzmj/10000));
							map.put("fzzmjtb",df.format(fzzmjtb));
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(TableksqkActivity.this, list,
								R.layout.table_item_ksqk, new String[]{"buildingType","zzmj","zzmjtb","zzts","zztstb","fzzmj","fzzmjtb"},
								new int[]{R.id.buildingType,R.id.zzmj,R.id.zzmjtb,R.id.zzts,R.id.zztstb,R.id.fzzmj,R.id.fzzmjtb});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
