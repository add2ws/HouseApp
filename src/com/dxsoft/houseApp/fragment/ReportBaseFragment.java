package com.dxsoft.houseApp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dxsoft.houseApp.activity.AddReportActivity;
import com.dxsoft.houseApp.fragment.StatisticFragment.MenuItemList;
import com.dxsoft.houseApp.R;
import com.zxing.activity.CaptureActivity;

public class ReportBaseFragment extends Fragment {
	public static String tagName = "ReportBaseFragment";
	private final String TAG = this.getClass().getName();
	private View qrcodeBtn;

	/**
	 * 作为页面容器的ViewPager
	 */
	private ViewPager viewPager;

	private PagerTitleStrip pagerTitleStrip; // viewpager的标题
	private PagerTabStrip pagerTabStrip; // 一个viewpager的指示器，效果就是一个横的粗的下划线
	private List<String> titleList = new ArrayList<String>(); // viewpager的标题
	private CreatedReportBaseFragment createdReportBaseFragment = new CreatedReportBaseFragment();
	private ShareReportBaseFragment shareReportBaseFragment = new ShareReportBaseFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_reportbase, container, false);
		qrcodeBtn = rootView.findViewById(R.id.rl_qrcode);
		qrcodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ReportBaseFragment.this.getActivity(), CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});

		viewPager = (ViewPager) rootView.findViewById(R.id.pager);
		pagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.green));
		// pagerTitleStrip = (PagerTitleStrip)
		// this.findViewById(R.id.pagertitle);
		// pagerTitleStrip.setTextColor(this.getResources().getColor(R.color.green));
		FragmentManager fm = this.getActivity().getSupportFragmentManager();
		final List<Fragment> fragmentList = new ArrayList<Fragment>();
		titleList.add("我创建的报表");
		titleList.add("他人共享的报表");
		fragmentList.add(createdReportBaseFragment);
		fragmentList.add(shareReportBaseFragment);
		FragmentStatePagerAdapter fspa = new FragmentStatePagerAdapter(fm) {

			@Override
			public int getCount() {
				return fragmentList.size();
			}

			@Override
			public Fragment getItem(int position) {
				return fragmentList.get(position);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titleList.get(position);
			}
		};

		viewPager.setAdapter(fspa);
		return rootView;
	}
	
	public void reloadData() {
		createdReportBaseFragment.reloadData();
		shareReportBaseFragment.reloadData();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String result = "";
		if (data != null) {
			result = data.getExtras().getString("result");
		} else {
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("shareSid", "1123");
				jobj.put("name", "淮南当月销售一览表");
				jobj.put("createTime", "2015-09-01 12:30:21");
				jobj.put("expireTime", "2015-10-01 00:00:00");
				jobj.put("createUser", "王皮匠");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			result = jobj.toString();
		}

		try {
			JSONObject obj = new JSONObject(result);
			String shareSid = obj.getString("shareSid");
			String name = obj.getString("name");
			String createTime = obj.getString("createTime");
			String expireTime = obj.getString("expireTime");
			String createUser = obj.getString("createUser");
			Intent intent = new Intent(this.getActivity(), AddReportActivity.class);
			intent.putExtra("shareSid", shareSid);
			intent.putExtra("name", name);
			intent.putExtra("createTime", createTime);
			intent.putExtra("expireTime", expireTime);
			intent.putExtra("createUser", createUser);
			startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
