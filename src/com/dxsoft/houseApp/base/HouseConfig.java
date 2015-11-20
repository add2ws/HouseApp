package com.dxsoft.houseApp.base;

public class HouseConfig {

	public static final String METHOD_BINDPUSH = "bindPush";
	public static final String METHOD_GETCITY = "getCity";
	public static final String METHOD_GETLIST = "getList";//获取数据传给后台
	public static final String METHOD_GETLIST_KS = "getList_ks";//获取数据传给后台
	public static final String METHOD_GETPERSON = "getPersonalhouse";
	public static final String METHOD_GETSHAREREPORT ="getShareReport";
	public static final String METHOD_GETPSALES ="getsales";
	public static final String METHOD_GETPAREA ="getsalesarea";
	public static final String METHOD_GETPTAOPRICE="getZzSalesnumprice";
	public static final String METHOD_GETPTYPE ="getZzSalestype";
	public static final String METHOD_FQXSYLB = "fqxsylb";
	public static final String METHOD_GETZZFQ = "getZzfq";
	public static final String METHOD_GETXSQK = "getxsqk";
	public static final String METHOD_GETGYQK = "getgyqk";
	public static final String METHOD_GETKSQK = "getksqk";
	public static final String METHOD_GETXSJG = "getxsjg";
	public static final String METHOD_GETHXFB = "hxfbqk";
	public static final String METHOD_GETMJFB = "mjfbqk";
	public static final String METHOD_GETJWGQJGB = "jwgqjgb";
	public static final String METHOD_GETGGXJBQK = "gxjbqk";
	public static final String METHOD_GETTXGQJGB = "txgqjgb";
	public static final String METHOD_PAIHANGTS ="paihangts";
	public static final String METHOD_PAIHANGJE ="paihangje";
	public static final String METHOD_PAIHANGMJ ="paihangmj";
	public static final String METHOD_PAIHANGJJ ="paihangjj";
	public static final String METHOD_SAVE_SHARE_REPORT ="saveShareReport";

	public static final int STARTDATE_DIALOG_ID = 1; //开始时间
	public static final int ENDDATE_DIALOG_ID = 2; //结束时间
	
	/**
	 * shareperference系统保存数据 常量字段
	 */
	public static final String SHAREPREFERENCE_SETTING = "HouseSetting";
	public static final String KEY_USERSID="usersid";//用户SID
	public static final String KEY_PSID="psid";//菜单模型父id
	public static final String KEY_TITLE="title";
	public static final String KEY_ITEMID="itemid";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_UID = "uid"; //拥有者ID
	public static final String KEY_UPDATETIME = "updatetime";
	public static final String KEY_USERID = "userid"; //用户登录ID
	public static final String KEY_PARERSID = "parersid";
	public static final String KEY_ISLOGIN = "islogin";
	public static final String KEY_NAME = "username";
	public static final String KEY_PASSWORD="password";
	public static final String KEY_MESSAGENUM="messagenum";
	public static final String KEY_ADVICENUM="advicenum";
	public static final String KEY_CHECKOUTNUM="checkoutnum";
	public static final String KEY_PHOTO_NAME="photoname";
	public static final String KEY_RENAME = "rename";
	public static final String KEY_REPASSWORD="repassword";
	public static final String KEY_ISREMBER="isremember";//是否记住密码
	public static final String KEY_ISAUTOLOGIN="isautologin";//是否自动登录
	public static final String KEY_PHONE="phone";//手机�?
	public static final String KEY_RECEIVENOTIFY="receiveNortify";//是否接收通知
	
	/**
	 * 数据库常量字�?
	 */	
	public static final String DATABASE_NAME = "HouseBase";
	public static final String ID = "_id";
	public static final String GSON = "json";
	public static final String UID = "uid";
	public static final String URL = "url";

	public static final String HOUSETYPE="T_DM_MA_FWDFL";//房屋类型,---新建商品房 存量房
	public static final String HOUSEUSE="T_MAS_TJHS";// 房屋用途 

	/**
	 * 服务器访问字�?
	 */
	public static final String METHOD_LOGIN = "MobileLogin";//登录
	public static final String METHOD_MENUITEMLIST="queryMenuItemList";//用户菜单列表
	public static final String METHOD_HOUSEUSE="queryHouseUse";//房屋用途
	public static final String METHOD_HOUSEAREA="queryHouseArea";//房屋用途
	public static final String METHOD_HOUSELIST="queryHouseList";//查询交易
	public static final String METHOD_HOUSESELLTOTALLIST="queryHouseSellTotalList";//住房按总价段销售情况统计表 查询 
	public static final String METHOD_MY_REPORTBASE = "myReportBase";//我创建的报表仓库
	public static final String METHOD_SHARE_REPORTBASE = "shareReportBase";//分享给我的报表仓库
	
	/**
	 * 常量
	 */
	public static final int F_PSID=40087;//菜单模型父id
	public static final int B_DAY=30;//默认查询开始时间与结束时间差
	public static final long A_DAY=24*60*60*1000;//一天毫秒值
	
	
	/**
	 * 字符串常量
	 */
	public static String DATE_YYYYMMDD="yyyy-MM-dd";
}