package com.dxsoft.houseApp.activity.module;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dxsoft.houseApp.R;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HouseUtil;
import com.dxsoft.houseApp.util.HttpHelper;

public class FqxsylbActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	
	private RelativeLayout backBtn;
	private EditText onMonthEditor;
	private EditText houseTypeEditor;
	private Dialog houseTypeDialog;
	private Button cfmBtn;
	private ListView listView;
	
	List<HashMap<String,Object>> dataList = new ArrayList<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fqxsylb);
		initData();
		initView();
		setListener();
	}
	
	//初始化数据
	private void initData() {
		
	}
	
	//初始化视图组件
	private void initView() {
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		onMonthEditor = (EditText) this.findViewById(R.id.et_onMonth);
		HouseUtil.toMonthPicker(onMonthEditor);
		houseTypeEditor = (EditText) this.findViewById(R.id.et_houseType);
		houseTypeDialog = HouseUtil.createHouseTypeDialog(this, houseTypeEditor);
		cfmBtn = (Button) this.findViewById(R.id.btn_tjcx);
		listView = (ListView) this.findViewById(R.id.lv_fqxs_list);
		
	}
	
	//初始化事件监听器
	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FqxsylbActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		
		houseTypeEditor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				houseTypeDialog.show();
			}
		});
		
		cfmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String houseType = houseTypeEditor.getText().toString();
				String onMonth = onMonthEditor.getText().toString();
				
				Parameter p = new Parameter();
				houseType = URLEncoder.encode(houseType);
				p.setHouseType(houseType);
				p.setOnMonth(onMonth);

				cfmBtn.setEnabled(false);
				cfmBtn.setText(R.string.query_loading_name);
				new QueryData().execute(p);
			}
		});
		
	}
	
	//异步类(主要用于请求数据)
	class QueryData extends AsyncTask<Parameter, Void, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected String doInBackground(Parameter... param) {
			String result = null;
			try {
				result = HttpHelper.requestData(HouseConfig.METHOD_FQXSYLB, C.Url, param[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			cfmBtn.setEnabled(true);
			cfmBtn.setText(R.string.query_button_name);

			if (TextUtils.isEmpty(result)) {
				Toast.makeText(FqxsylbActivity.this, "网络异常,请稍后再试", Toast.LENGTH_SHORT).show();
			} else {
				
				JSONObject obj;
				List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
				try {
					obj = new JSONObject(result);
					JSONArray data = obj.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject r = data.getJSONObject(i);
						Map<String, Object> report = new HashMap<String, Object>();
						report.put("xzqhmc", r.getString("xzqhmc"));
						report.put("jzks", r.getString("jzks") + getResources().getString(R.string.unit_t));
						report.put("xzks", r.getString("xzks") + getResources().getString(R.string.unit_t));
						report.put("sjxs", r.getString("sjxs") + getResources().getString(R.string.unit_t));
						double jzks = r.getDouble("jzks");
						double sjxs = r.getDouble("sjxs");
						double ssksb = 0;
						if (jzks != 0) {
							ssksb = sjxs / jzks * 100;
						} else {
							ssksb = 0;
						}
						DecimalFormat df=new DecimalFormat("##.##");
						String ssksbStr = df.format(ssksb);
						report.put("tsssyksb", ssksbStr + "%");
						maps.add(report);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				SimpleAdapter sa = new SimpleAdapter(FqxsylbActivity.this, maps, R.layout.item_list_fqxs, new String[] {"xzqhmc", "jzks", "xzks", "sjxs", "tsssyksb"}, new int[] {R.id.tv_xzqhmc, R.id.tv_jzks, R.id.tv_xzks, R.id.tv_sjxsl, R.id.tv_tsssyksb});
				listView.setAdapter(sa);
			}
			
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
