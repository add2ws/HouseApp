package com.dxsoft.houseApp.activity;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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

public class ZhuzhaiFqActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private EditText houseType;
	private Dialog houseTypeDialog;
	private RelativeLayout backBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuzhai_fq);
		button = (Button) findViewById(R.id.btn_tjcx);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhuzhaiFqActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		textDate = (EditText) findViewById(R.id.et_onMonth);
		HouseUtil.toMonthPicker(textDate);
		houseType = (EditText) findViewById(R.id.et_houseType);
		houseTypeDialog = HouseUtil.createHouseTypeDialog(this, houseType);
		listview = (ListView) findViewById(R.id.lv_fqxs_list);
		
		button.setOnClickListener(new OnClickListener() {
			
	        
			@Override
			public void onClick(View arg0) {
				String onMonth = textDate.getText().toString();
				Parameter p = new Parameter();
				p.setDate(onMonth);
				int house = 0;
				if(houseType.getText().toString().equals("住宅、非住宅")){
					p.setCountryType(house);
				}else if (houseType.getText().toString().equals("住宅")) {
					house=1;
					p.setCountryType(house);
				}else if (houseType.getText().toString().equals("非住宅")){
					house=2;
					p.setCountryType(house);
				}
				p.setCountryType(house);

				button.setEnabled(false);
				button.setText(R.string.query_loading_name);
				new readData().execute(p);
			}
		});
		houseType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				houseTypeDialog.show();
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
					result = HttpHelper.requestData(HouseConfig.METHOD_GETZZFQ, C.Url, param[0]);
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
					Toast.makeText(ZhuzhaiFqActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("data");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String quyu = jsonObject.get("mc").toString();
							String tao = jsonObject.get("taoshu").toString();
							String area = jsonObject.get("area").toString();
							String price = jsonObject.get("junjia").toString();
							map.put("quyu",quyu);
							map.put("tao",tao);
							map.put("area",area);
							map.put("price", price);
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(ZhuzhaiFqActivity.this, list,
								R.layout.zhuzhai_item_fq, new String[]{"quyu","tao","area","price"}, new int[]{R.id.zhuzhai_diqu,R.id.taoshu,R.id.area,R.id.junjia});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
