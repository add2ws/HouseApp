package com.dxsoft.houseApp.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.data.HouseSharePreference;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.R;

public class AddReportActivity extends Activity {
	private String TAG = this.getClass().getName();
	
	private RelativeLayout backBtn;
	private RelativeLayout rlLoading;
	private WebView webView;
	private TextView reportNameTv;
	private TextView createTimeTv;
	private TextView createUserTv;
	private TextView expireTimeTv;
	private TextView progressText;
	private TextView saveTv;
	
	private String shareSid;
	private String userSid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_report);
		
		initView();
		initData();
		setListener();
	}
	
	//初始化数据
	private void initData() {
		shareSid = this.getIntent().getStringExtra("shareSid");
		userSid = HouseSharePreference.getStringData(this, HouseConfig.KEY_USERSID);
		if (TextUtils.isEmpty(shareSid)) {
			return;
		}
		
		new AsyncTask<Void, String, String>() {

			@Override
			protected String doInBackground(Void... arg0) {
				Parameter parameter = new Parameter();
				parameter.setSid(shareSid);
				String result = "";
				try {
					result = HttpHelper.requestData(HouseConfig.METHOD_GETSHAREREPORT, C.Url, parameter);
				} catch (IOException e) {
					rlLoading.setVisibility(View.GONE);
					e.printStackTrace();
				}
				
				return result;
			}
			
			protected void onPostExecute(String result) {
				rlLoading.setVisibility(View.GONE);
				if (TextUtils.isEmpty(result)) return;
				try {
					JSONObject obj = new JSONObject(result);
					String sid = obj.getString("sid");
					String name = obj.getString("name");
//					String createTime = obj.getString("createTime");
					String createUser = obj.getString("createUserName");
					String expireTime = obj.getString("expireTime");
					boolean isExpire = obj.getBoolean("isExpire");
					if (expireTime.equals("null")) {
						expireTime = "∞";
					}
					
					reportNameTv.setText(name);
//					createTimeTv.setText("创建时间：" + createTime);
					createUserTv.setText("创建人：" + createUser);
					expireTimeTv.setText("过期时间：" + expireTime);
					
					
					if (!isExpire) {
						
						String url = C.Url + "/preview.do?id=" + sid;
						webView.setWebChromeClient(new WebChromeClient() {//显示网页刷新进度
							@Override
							public void onProgressChanged(WebView view, int progressPercent) {
								if (progressPercent == 100) {
									progressText.setVisibility(View.GONE);
								} else {
									progressText.setText("载入中" + progressPercent + "%");
								}
							}
							
						});
						Log.i(TAG, url);
						webView.loadUrl(url);
					} else {
						saveTv.setVisibility(View.GONE);
						progressText.setText("数据已过期");
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
			
		}.execute();
	}
	
	//初始化视图组件
	private void initView() {
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		rlLoading = (RelativeLayout) this.findViewById(R.id.rl_loading);
		webView = (WebView) this.findViewById(R.id.wv_content);
		progressText = (TextView) this.findViewById(R.id.tv_progress);
		reportNameTv = (TextView) this.findViewById(R.id.tv_report_name);
//		createTimeTv = (TextView) this.findViewById(R.id.tv_create_time);
		createUserTv = (TextView) this.findViewById(R.id.tv_create_user);
		expireTimeTv = (TextView) this.findViewById(R.id.tv_expire_time);
		saveTv = (TextView) this.findViewById(R.id.tv_save);
	}
	
	//初始化事件监听器
	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		
		saveTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new Builder(AddReportActivity.this);
				builder.setMessage("确认保存到我的仓库");
				
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AddReportActivity.this, "保存中...", Toast.LENGTH_SHORT).show();
						new AsyncTask<Void, String, String>() {

							@Override
							protected String doInBackground(Void... args) {
								String result = "";
								try {
									Parameter param = new Parameter();
									param.setUserSid(Integer.valueOf(userSid));
									param.setSid(shareSid);
									result = HttpHelper.requestData(HouseConfig.METHOD_SAVE_SHARE_REPORT, C.Url, param);
								} catch (IOException e) {
									e.printStackTrace();
								}
								return result;
							}

							protected void onPostExecute(String result) {
								if (TextUtils.isEmpty(result)) {
									Toast.makeText(AddReportActivity.this, "服务器故障，请稍后再试", Toast.LENGTH_SHORT).show();
								} else {
									try {
										JSONObject obj = new JSONObject(result);
										boolean success = obj.getBoolean("success");
										if (success) {
											Toast.makeText(AddReportActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(AddReportActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}
							
						}.execute();
						
						dialog.dismiss();
						AddReportActivity.this.finish();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alertDialog = builder.create();
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.show();
			}
		});
	}
	

}
