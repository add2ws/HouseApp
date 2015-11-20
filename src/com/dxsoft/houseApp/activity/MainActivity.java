package com.dxsoft.houseApp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.dxsoft.houseApp.R;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.data.HouseSharePreference;
import com.dxsoft.houseApp.fragment.PreferenceFragment;
import com.dxsoft.houseApp.fragment.ReportBaseFragment;
import com.dxsoft.houseApp.fragment.StatisticFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private final String TAG = this.getClass().getName();

	private boolean flag = false;
	private Button statisticBtn;
	private Button reportBaseBtn;
	private Button preferenceBtn;
	private int curBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		setListener();
		
		//第三方推送程序初始化
		Boolean r = HouseSharePreference.getBooleanData(this, HouseConfig.KEY_RECEIVENOTIFY);//用户是否在设置界面开启了推送
		if (r == null) {//默认开启推送
			PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, C.PUSH_API_KEY);
			HouseSharePreference.putBooleanData(this, HouseConfig.KEY_RECEIVENOTIFY, true);
		} else if (r) {
			PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, C.PUSH_API_KEY);
		} else {
			PushManager.stopWork(this);
		}
	}

	private void initView() {
		statisticBtn = (Button) this.findViewById(R.id.btn_statistic);
		reportBaseBtn = (Button) this.findViewById(R.id.btn_reportbase);
		preferenceBtn = (Button) this.findViewById(R.id.btn_preference);
		FragmentTransaction tran = this.getSupportFragmentManager().beginTransaction();
		curFragment = new StatisticFragment();
		curBtn = R.id.btn_statistic;
		tran.add(R.id.fl_content, curFragment, StatisticFragment.tagName);
		tran.commit();
	}

	private void setListener() {
		statisticBtn.setOnClickListener(this);
		reportBaseBtn.setOnClickListener(this);
		preferenceBtn.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (flag) {
				android.os.Process.killProcess(android.os.Process.myPid());
			} else {
				Toast.makeText(this, "再按一次将退出程序", Toast.LENGTH_SHORT).show();
				flag = true;
			}
		}
		return false;
	}

	private Fragment curFragment = null;

	@Override
	public void onClick(View view) {
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		switch (view.getId()) {
		case R.id.btn_statistic:
			if (curBtn != R.id.btn_statistic) {
				StatisticFragment statisticFragment = (StatisticFragment) fm.findFragmentByTag(StatisticFragment.tagName);
				if (statisticFragment == null) {
					tran.hide(curFragment);
					statisticFragment = new StatisticFragment();
					tran.add(R.id.fl_content, statisticFragment, StatisticFragment.tagName);
				} else {
					tran.hide(curFragment);
					tran.show(statisticFragment);
					statisticFragment.reloadData();
				}
				curFragment = statisticFragment;
			}
			break;
		case R.id.btn_reportbase:
			if (curBtn != R.id.btn_reportbase) {
				ReportBaseFragment reportBaseFragment = (ReportBaseFragment) fm.findFragmentByTag(ReportBaseFragment.tagName);
				if (reportBaseFragment == null) {
					tran.hide(curFragment);
					reportBaseFragment = new ReportBaseFragment();
					tran.add(R.id.fl_content, reportBaseFragment, ReportBaseFragment.tagName);
				} else {
					tran.hide(curFragment);
					tran.show(reportBaseFragment);
					reportBaseFragment.reloadData();
				}
				curFragment = reportBaseFragment;
			}
			break;
		case R.id.btn_preference:
			if (curBtn != R.id.btn_preference) {
				PreferenceFragment preferenceFragment = (PreferenceFragment) fm.findFragmentByTag(PreferenceFragment.tagName);
				if (preferenceFragment == null) {
					tran.hide(curFragment);
					preferenceFragment = new PreferenceFragment();
					tran.add(R.id.fl_content, preferenceFragment, PreferenceFragment.tagName);
				} else {
					tran.hide(curFragment);
					tran.show(preferenceFragment);
				}
				curFragment = preferenceFragment;
			}
			break;
		default:
			break;
		}
		curBtn = view.getId();
		tran.commit();

	}
}
