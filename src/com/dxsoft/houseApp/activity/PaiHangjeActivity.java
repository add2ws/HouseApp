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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import com.dxsoft.houseApp.R.id;

public class PaiHangjeActivity extends Activity {

	private Button button;
	private ListView listview;
	private EditText textDate;
	private RelativeLayout backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paihang_xsje);
		button = (Button) findViewById(R.id.chaxun);
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PaiHangjeActivity.this.finish();
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
					result = HttpHelper.requestData(HouseConfig.METHOD_PAIHANGJE, C.Url, param[0]);
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
					Toast.makeText(PaiHangjeActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
				} else {
					try {
						JSONObject json1= new JSONObject(result);
						JSONArray jsonArray= json1.getJSONArray("rows");
						List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
						for(int i=0;i<jsonArray.length();i++){
							HashMap<String,Object> map=new HashMap<String,Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String rowMonth = jsonObject.get("rowMonth").toString();
							String total = jsonObject.get("total").toString();
							String name = jsonObject.get("projectName").toString();
							String titleName = jsonObject.get("projectName").toString();
							String place = jsonObject.get("projectPlace").toString();
							String jj = jsonObject.get("jj").toString();
							String rowYear = jsonObject.get("rowYear").toString();
							map.put("rowMonth",rowMonth);
							map.put("total",total);
							map.put("titleName",titleName);
							map.put("name",name);
							map.put("place",place);
							map.put("jj",jj);
							map.put("rowYear",rowYear);
							list.add(map);
						}
						
						MyAdapter adp= new MyAdapter(PaiHangjeActivity.this, list,
								R.layout.paihang_item_xsje, new String[]
										{"titleName","rowMonth","total","name","place","jj","rowYear"}, new int[]
										{R.id.title_name,R.id.rowMonth,R.id.total,R.id.name,R.id.place,R.id.jj,R.id.rowYear});
				        listview.setAdapter(adp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}

		
}
