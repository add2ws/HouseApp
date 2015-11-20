package com.dxsoft.houseApp.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushManager;
import com.dxsoft.houseApp.R;
import com.dxsoft.houseApp.activity.LoginActivity;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.data.HouseSharePreference;

public class PreferenceFragment extends Fragment {

	public static String tagName = "PreferenceFragment";

	private ImageButton receiveBtn;
	private RelativeLayout messageListBtn;
	private Button unLoginBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_preference, container, false);
		receiveBtn = (ImageButton) rootView.findViewById(R.id.ib_receive);
		messageListBtn = (RelativeLayout) rootView.findViewById(R.id.rl_messagelist);
		unLoginBtn = (Button) rootView.findViewById(R.id.btn_unlogin);
		
		Boolean r = HouseSharePreference.getBooleanData(this.getActivity(), HouseConfig.KEY_RECEIVENOTIFY);
		if (!r) {
			receiveBtn.setBackgroundResource(R.drawable.setting_off);
		}
		setListener();
		return rootView;
	}

	private void setListener() {
		receiveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Boolean r = HouseSharePreference.getBooleanData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_RECEIVENOTIFY);
				if (r) {
					HouseSharePreference.putBooleanData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_RECEIVENOTIFY, false);
					receiveBtn.setBackgroundResource(R.drawable.setting_off);
					PushManager.stopWork(PreferenceFragment.this.getActivity());
				} else {
					HouseSharePreference.putBooleanData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_RECEIVENOTIFY, true);
					receiveBtn.setBackgroundResource(R.drawable.setting_on);
					PushManager.resumeWork(PreferenceFragment.this.getActivity());
				}
			}
		});

		messageListBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});

		unLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new Builder(PreferenceFragment.this.getActivity());
				builder.setMessage("您确定要退出登录吗？");
				
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						PushManager.stopWork(PreferenceFragment.this.getActivity());//停止推送程序 
						HouseSharePreference.putBooleanData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_ISAUTOLOGIN, false);
						HouseSharePreference.putBooleanData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_ISREMBER, false);
						HouseSharePreference.putStringData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_USERSID, null);
						HouseSharePreference.putStringData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_NAME, null);
						HouseSharePreference.putStringData(PreferenceFragment.this.getActivity(), HouseConfig.KEY_PASSWORD, null);
						Intent intent = new Intent(PreferenceFragment.this.getActivity(), LoginActivity.class);
						startActivity(intent);
						PreferenceFragment.this.getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
						PreferenceFragment.this.getActivity().finish();
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
