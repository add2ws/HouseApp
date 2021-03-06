package com.dxsoft.houseApp.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dxsoft.houseApp.R;


public class ViewUtil {
	
	//显示等待图标
	public static Dialog showWaitingView(Context context, String msg){
		int w = context.getResources().getDisplayMetrics().widthPixels - 50;
		View view = LayoutInflater.from(context).inflate(R.layout.load_data_item, null);
		TextView mBarMsg = (TextView)view.findViewById(R.id.message);
		mBarMsg.setText(msg);
		Dialog mDialog = new Dialog(context);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		
		Window dialogWindow = mDialog.getWindow(); 
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        lp.width = w; // 宽度 
        dialogWindow.setAttributes(lp);
        mDialog.show();
        return mDialog;
	}
	
	//给EditText限制最大字符数
	public static EditText limitMaxLenght(final EditText et, final int maxLength) {
		et.addTextChangedListener(new TextWatcher() {
			CharSequence text;
			int editStart, editEnd;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				text = s;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				editStart = et.getSelectionStart();  
	            editEnd = et.getSelectionEnd();
	            if (text.length() > maxLength) {
	                s.delete(editStart-1, editEnd);
	                int tempSelection = editStart;
	                et.setText(s);  
	                et.setSelection(tempSelection);  
	            }  
			}
		});
		return et;
	}
	
	 public static boolean isApplicationBroughtToBackground(final Context context) {
	        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	        if (!tasks.isEmpty()) {
	            ComponentName topActivity = tasks.get(0).topActivity;
	            if (!topActivity.getPackageName().equals(context.getPackageName())) {
	                return true;
	            }
	        }
	        return false;

	    }
}
