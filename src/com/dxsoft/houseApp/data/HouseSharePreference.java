package com.dxsoft.houseApp.data;

import com.dxsoft.houseApp.base.HouseConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HouseSharePreference {

	public HouseSharePreference(){
	}
	
	/**
	 * 将数据放入sharepreference中
	 * @param context
	 * @param key 键值
	 * @param value 值
	 * @return
	 */
	public static boolean putStringData(Context context, String key, String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * 获取sharepreference中对应键值的值
	 * @param context
	 * @param key 键值
	 * @return
	 */
	public static String getStringData(Context context, String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(key, "");
	}
	
	
	/**
	 * 将数据放入sharepreference中
	 * @param context
	 * @param key 键值
	 * @param value 值
	 * @return
	 */
	public static boolean putBooleanData(Context context, String key, boolean value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * 获取sharepreference中对应键值的值
	 * @param context
	 * @param key 键值
	 * @return
	 */
	public static boolean getBooleanData(Context context, String key){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getBoolean(key,false);
	}
	
	public static boolean putUpdateTime(Context context, String token){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_UPDATETIME, token);
		return editor.commit();
	}
	
	public static String getUpdateTime(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_UPDATETIME, "");
	}
	
	public static boolean putUserId(Context context, String id){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_USERID, id);
		return editor.commit();
	}
	
	public static String getUserId(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_USERID, "");
	}
	public static boolean putPassword(Context context, String password){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_PASSWORD, password);
		return editor.commit();
	}
	
	public static String getPassword(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_PASSWORD, "");
	}
	public static boolean putUserParersId(Context context, String id){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_PARERSID, id);
		return editor.commit();
	}
	
	public static String getUserParersId(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_PARERSID, "");
	}
	
	public static boolean putMessageNum(Context context, String messageNum){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_MESSAGENUM, messageNum);
		return editor.commit();
	}
	public static String getMessageNum(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_MESSAGENUM, "");
	}
	public static boolean putAdviceNum(Context context, String adviceNum){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_ADVICENUM, adviceNum);
		return editor.commit();
	}
	public static String getAdviceNum(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_ADVICENUM, "");
	}
	public static boolean putCheckoutNum(Context context, String checkoutNum){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(HouseConfig.KEY_CHECKOUTNUM, checkoutNum);
		return editor.commit();
	}
	public static String getCheckoutNum(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(HouseConfig.SHAREPREFERENCE_SETTING, 0);
		return sharedPreferences.getString(HouseConfig.KEY_CHECKOUTNUM, "");
	}
	
	
}
