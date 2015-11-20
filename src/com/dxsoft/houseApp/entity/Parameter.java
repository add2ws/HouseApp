package com.dxsoft.houseApp.entity;

public class Parameter {
	
	private String username;//登录名
	/*****用户口令****/
	private String passwd;
	
	private String channelId;//云推送ID
	private String sid;
	private Integer userSid;//用户SID
	private Integer pSid;//父ID
	private String sort;//菜单排序
	private int page;//页码
	private int rows;//每页条数
	
	private String houseuse;//房屋用途
	private String houseType;//房屋类型
	private String onMonth;//月份
	private String housearea;//城市 

	//报表
	private String country;
	private Integer countryType;
	private String date;
	private String date1;

	private String datenum;//时间维度
	private String startdate;//起始日期
	private String enddate;//结束日期
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Integer getUserSid() {
		return userSid;
	}
	public void setUserSid(Integer userSid) {
		this.userSid = userSid;
	}
	public Integer getpSid() {
		return pSid;
	}
	public void setpSid(Integer pSid) {
		this.pSid = pSid;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getHouseuse() {
		return houseuse;
	}
	public void setHouseuse(String houseuse) {
		this.houseuse = houseuse;
	}
	public String getHousearea() {
		return housearea;
	}
	public void setHousearea(String housearea) {
		this.housearea = housearea;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Integer getCountryType() {
		return countryType;
	}
	public void setCountryType(Integer countryType) {
		this.countryType = countryType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDatenum() {
		return datenum;
	}
	public void setDatenum(String datenum) {
		this.datenum = datenum;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public String getOnMonth() {
		return onMonth;
	}
	public void setOnMonth(String onMonth) {
		this.onMonth = onMonth;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	
	
}
