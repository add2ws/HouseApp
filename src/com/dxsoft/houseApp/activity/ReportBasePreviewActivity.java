package com.dxsoft.houseApp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.R;

public class ReportBasePreviewActivity extends FragmentActivity {
	private String TAG = this.getClass().getName();
	
    private RelativeLayout backBtn;
    private WebView webView;
    private TextView progressText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_reportbase_preview);
        initData();
        initView();
        setListener();
    }
  

	//初始化数据
	private void initData() {
	}

	//初始化视图组件
	private void initView() {
		String name = this.getIntent().getStringExtra("name");
		String id = String.valueOf(this.getIntent().getIntExtra("sid", 0));
		TextView titleView = (TextView) this.findViewById(R.id.tv_report_title);
		titleView.setText(name);
		
		backBtn = (RelativeLayout) this.findViewById(R.id.backlayout);
		progressText = (TextView) this.findViewById(R.id.tv_progress);
		webView = (WebView) this.findViewById(R.id.wv_content);
		String url = C.Url + "/preview.do?id=" + id;
		Log.i(TAG, url);
		
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
		
		webView.loadUrl(url);
	}
	
	//初始化事件监听器
	private void setListener() {
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ReportBasePreviewActivity.this.finish();
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		
	}

}  
