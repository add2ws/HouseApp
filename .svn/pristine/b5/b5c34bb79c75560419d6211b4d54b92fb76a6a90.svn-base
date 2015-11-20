package com.dxsoft.houseApp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dxsoft.houseApp.activity.MenuListDetailActivity;
import com.dxsoft.houseApp.activity.module.FqxsylbActivity;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class HouseUtil {
	private static SimpleDateFormat sd = new SimpleDateFormat(HouseConfig.DATE_YYYYMMDD);
	public static Map<String, String> json2Map(String jsonStr) {

		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, String> map = g.fromJson(jsonStr,
				new TypeToken<Map<String, String>>() {
				}.getType());
		return map;

	}
	/**
	 * 读取properties文件
	 */
	public static String getProperValue(Context c,String str){
		
		String value = null;
		  Properties pro = new Properties();
		  try {
			  pro.load(MenuListDetailActivity.class.getResourceAsStream(C.FILEPATH));
			  value = pro.getProperty(str);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  return value;
	}
	/**
	 * 转换格式YYYY-MM-DD
	 * @param dateLong
	 * @return
	 */
	public static String dateFormatString(long dateLong){
		Date date = new Date(dateLong);
		return sd.format(date);
	}
	/**
	 * 转换格式YYYY-MM-DD
	 * @param dateLong
	 * @return
	 */
	public static Date stringFormatDate(String str){
		Date parse=new Date();
		try {
			parse= sd.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}
	
	/**
     * 检查当前网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity){
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }else{
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
	public static void monthPicker(Context context, final EditText et) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month, int day) {
				et.setText(year + "年" + (month + 1) + "月");
			}
		}, year, month, day).show();
	}
	
	public static void toMonthPicker(EditText et) {
		et.setClickable(false);
		et.setFocusable(false);
		String monthStr = new SimpleDateFormat("yyyy-MM").format(new Date());
		et.setText(monthStr);
		
		et.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final EditText editor = (EditText) v;
				String monthStr = editor.getText().toString();
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				if (!TextUtils.isEmpty(monthStr)) {
					try {
						Date date = new SimpleDateFormat("yyyy-MM").parse(monthStr);
						c.setTime(date);
						year = c.get(Calendar.YEAR);
						month = c.get(Calendar.MONTH);
						day = c.get(Calendar.DAY_OF_MONTH);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				new DatePickerDialog(editor.getContext(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int day) {
						c.set(year, month, day);
						String monthStr = new SimpleDateFormat("yyyy-MM").format(c.getTime());
						editor.setText(monthStr);
					}
				}, year, month, day).show();
			}
		});
	}
    
	public static Dialog createHouseTypeDialog(Context context, final EditText houseTypeEditor) {
		final String[] houseTypeText = {"住宅", "非住宅"};
		final boolean[] houseTypeChecked = {true, true};
		return new AlertDialog.Builder(context).setTitle("请选择房屋类型").setMultiChoiceItems(houseTypeText, houseTypeChecked, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = "";
				for (int i = 0; i < houseTypeChecked.length; i++) {
					if (houseTypeChecked[i]) {
						text += houseTypeText[i] + "、";
					}
				}
				if (!TextUtils.isEmpty(text)) {
					text = text.substring(0, text.length() - 1);
				}
				houseTypeEditor.setText(text);
			}
		}).create();
	}
}
