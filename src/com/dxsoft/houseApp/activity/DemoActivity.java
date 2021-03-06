package com.dxsoft.houseApp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.R;

public class DemoActivity extends Activity {
	private String TAG = this.getClass().getName();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initData();
		initView();
		setListener();
	}
	
	//初始化数据
	private void initData() {
		
	}
	
	//初始化视图组件
	private void initView() {
	}
	
	//初始化事件监听器
	private void setListener() {
	}
	
	//异步类(主要用于请求数据)
	class DemoAsync extends AsyncTask<Parameter, Void, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

	
		
		@Override
		protected String doInBackground(Parameter... param) {
			return HttpHelper.getWebData(HttpHelper.getUrl(HouseConfig.METHOD_LOGIN, C.Url, param[0]));
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			Toast.makeText(DemoActivity.this, result, 3000).show();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
