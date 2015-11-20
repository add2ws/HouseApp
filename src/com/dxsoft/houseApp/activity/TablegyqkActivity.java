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
import android.widget.ImageView;
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

public class TablegyqkActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_gyqk);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TablegyqkActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});

		button = (Button) findViewById(R.id.chaxun);
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETGYQK, C.Url, param[0]);
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
					Toast.makeText(TablegyqkActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
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
							String zzmj = jsonObject.get("zzmj").toString();
							String zzljmj = jsonObject.get("zzljmj").toString();
							double zzmjtb = Double.parseDouble(jsonObject.get("zzmjtb").toString());
							String zzts = jsonObject.get("zzts").toString();
							String zzljts = jsonObject.get("zzljts").toString();
							double zztstb = Double.parseDouble(jsonObject.get("zztstb").toString());
							String fzzmj = jsonObject.get("fzzmj").toString();
							String fzzljmj = jsonObject.get("fzzljmj").toString();
							double fzzmjtb = Double.parseDouble(jsonObject.get("fzzmjtb").toString());
							map.put("buildingType", buildingType);
							map.put("zzmj",zzmj);
							map.put("zzljmj",zzljmj);
							map.put("zzmjtb",df.format(zzmjtb));
							map.put("zzts",zzts);
							map.put("zzljts",zzljts);
							map.put("zztstb",df.format(zztstb));
							map.put("fzzmj",fzzmj);
							map.put("fzzljmj",fzzljmj);
							map.put("fzzmjtb",df.format(fzzmjtb));
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(TablegyqkActivity.this, list,
								R.layout.table_item_gyqk, new String[]{"buildingType","zzmj","zzljmj","zzmjtb","zzts","zzljts","zztstb","fzzmj","fzzljmj","fzzmjtb"},
								new int[]{R.id.buildingType,R.id.zzmj,R.id.zzljmj,R.id.zzmjtb,R.id.zzts,R.id.zzljts,R.id.zztstb,R.id.fzzmj,R.id.fzzljmj,R.id.fzzmjtb});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
