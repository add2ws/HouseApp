package com.dxsoft.houseApp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HouseUtil;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.util.ViewUtil;
import com.dxsoft.houseApp.R;
/**
 * 住房按总价段销售情况统计表 
 * @author duwenang
 *
 */
public class HouseSellTotalStatistic extends Activity implements OnDateSetListener,OnClickListener{
	
	private String listname;
	private TextView title;
	private ListView lv;
	private List<String> houseUse=new ArrayList<String>();
	private List<String> houseArea=new ArrayList<String>();
	private List<String> houseAreaIds=new ArrayList<String>();
	private String [] arrayhouseArea=null;//记录房屋类型
	private boolean [] arrayFruitSelectedArea=null;//判断房屋类型选中
	private EditText spinner;//房屋用途
	private EditText spinner1;//统计地区
	private EditText startDate;//起始日期
	private EditText endDate;//结束日期
	private EditText thisDate;
	private Button button;//查询按钮
	private RelativeLayout backlayout;//回退按钮
	
	private AlertDialog dialog;
	
	private String [] arrayHouseUse=new String[]{"住宅类","非住宅类"};
	private boolean[] arrayFruitSelected = new boolean[] {false, false};
	
	private String houseAreaId="";//地区参数
	private ArrayList<HashMap<String, Object>> dItems = new ArrayList<HashMap<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house_trade_statistic);
		init();
		setListener();
	}
	private void init() {
		Intent intent = this.getIntent();
		listname=intent.getStringExtra(HouseConfig.KEY_TITLE);
		title=(TextView) findViewById(R.id.title);
		title.setText(listname);
		
		
		long timeLong=System.currentTimeMillis();
		lv=(ListView) findViewById(R.id.listTableView);
		spinner=(EditText) findViewById(R.id.houseUseSpinner);
		spinner1=(EditText) findViewById(R.id.statlocationSpinner);
		
		startDate=(EditText) findViewById(R.id.startDate);
		startDate.setInputType(InputType.TYPE_NULL);//设置不启动软键盘
		startDate.setText(HouseUtil.dateFormatString(timeLong-HouseConfig.B_DAY*HouseConfig.A_DAY));//设置默认时间
		endDate=(EditText) findViewById(R.id.endDate);
		endDate.setInputType(InputType.TYPE_NULL);
		endDate.setText(HouseUtil.dateFormatString(timeLong));//设置默认时间
		
		button=(Button) findViewById(R.id.queryButton);
		backlayout=(RelativeLayout) findViewById(R.id.housebacklayout);
		
		//查询房屋用途
//		new QueryHouseUse().execute("1");
		
		//查询统计城区
		new QueryHouseArea().execute("1");
		
	}
	private void setListener() {
		backlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			}
		});
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initHouseUse();
			}
		});
		spinner1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initHouseArea();
			}

			
		});
		

		startDate.setOnClickListener(this);
		endDate.setOnClickListener(this);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(startDate.getText().toString().equals("")){
					Toast.makeText(HouseSellTotalStatistic.this, R.string.input_start_data, Toast.LENGTH_SHORT).show();
					return;
				}
				if(endDate.getText().toString().equals("")){
					Toast.makeText(HouseSellTotalStatistic.this, R.string.input_end_data, Toast.LENGTH_SHORT).show();
					return;
				}
				if(spinner.getText().toString().equals("")){
					Toast.makeText(HouseSellTotalStatistic.this, R.string.input_house_use, Toast.LENGTH_SHORT).show();
					return;
				}
				if(spinner1.getText().toString().equals("")){
					Toast.makeText(HouseSellTotalStatistic.this, R.string.input_stat_area, Toast.LENGTH_SHORT).show();
					return;
				}
				new QueryHosueList().execute("1");
			}
		});
	}
	class QueryHosueList extends AsyncTask<String, Void, String> {
		String url="";
		String startDateText;
		String endDateText;
		Dialog mDialog;
		@Override
		protected String doInBackground(String... params) {
			Parameter parameter = new Parameter();
			startDateText=startDate.getText().toString();
			endDateText=endDate.getText().toString();
			if(spinner.getText().toString().equals("住宅类,非住宅类")){
				parameter.setHouseuse("13");
			}else if (spinner.getText().toString().equals("住宅类")) {
				parameter.setHouseuse("1");
			}else if (spinner.getText().toString().equals("非住宅类")){
				parameter.setHouseuse("3");
			}
			parameter.setStartdate(startDateText);
			parameter.setEnddate(endDateText);
			parameter.setHousearea(houseAreaId);
			url = HttpHelper.getUrl(HouseConfig.METHOD_HOUSESELLTOTALLIST, C.Url, parameter);
			return HttpHelper.getWebData(url);
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject success = new JSONObject(result);
				System.out.println("111success"+success);
				if(success.getBoolean("success")){
					dItems.clear();
					if(mDialog!=null){
						mDialog.dismiss();;
					}
					System.out.println("------------------------进入进入----------------------------");
//					if(!success.getString("data").equals("[]")){
//						JSONArray jsonArray = success.getJSONArray("data");
//						for(int i=0;i<jsonArray.length();i++){
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							HashMap<String,Object> map=new HashMap<String,Object>();
//							map.put("dateText", jsonObject.get("countryname").toString()+"("+startDateText+getResources().getString(R.string.untils)+endDateText+")");
//							map.put("areaText", jsonObject.get("area").toString()+getResources().getString(R.string.unit_wpfm));
//							map.put("coverText", jsonObject.get("taoshuo").toString()+getResources().getString(R.string.unit_t));
//							map.put("moneyText", jsonObject.get("totalprices").toString()+getResources().getString(R.string.unit_wy));
//							map.put("averageText", jsonObject.get("average").toString()+getResources().getString(R.string.unit_ypfm));
//							dItems.add(map);
//						}
//						SimpleAdapter sa=new SimpleAdapter(HouseSellTotalStatistic.this, dItems, R.layout.house_trade_item, new String[]{"dateText","areaText","coverText","moneyText","averageText"}, new int[]{R.id.dateText,R.id.areaText,R.id.coverText,R.id.moneyText,R.id.averageText});
//						lv.setAdapter(sa);
//					}else{
//						HashMap<String,Object> map=new HashMap<String,Object>();
//						map.put("hintItem", getResources().getString(R.string.no_query_data));
//						dItems.add(map);
//						SimpleAdapter sa=new SimpleAdapter(HouseSellTotalStatistic.this, dItems, R.layout.hint_item, new String[]{"hintItem"}, new int[]{R.id.hintItem});
//						lv.setAdapter(sa);
//					}
				}
			} catch (Exception e) {
				if(mDialog!=null){
					mDialog.dismiss();;
				}
				e.printStackTrace();
			}
			
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			mDialog = ViewUtil.showWaitingView(HouseSellTotalStatistic.this, "正在加载...");
			super.onPreExecute();
		}
	}
	
	private void initHouseArea() {
		if(arrayhouseArea==null){
			return;
		}
		dialog = new AlertDialog.Builder(this).setTitle("请选择统计地区").setMultiChoiceItems(arrayhouseArea, arrayFruitSelectedArea,new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String info="";
				for(int i=0;i<arrayFruitSelectedArea.length;i++){
					if(arrayFruitSelectedArea[i]){
						if(!info.equals("")){
							info+= ","+arrayhouseArea[i];
							houseAreaId+=","+houseAreaIds.get(i);
						}else{
							info+= arrayhouseArea[i];
							houseAreaId+=houseAreaIds.get(i);
						}
					}
				}
				spinner1.setText(info);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create();
		dialog.show();
	}
	private void initHouseUse() {
		
		dialog = new AlertDialog.Builder(this).setTitle("请选择房屋用途").setMultiChoiceItems(arrayHouseUse, arrayFruitSelected,new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String info="";
				for(int i=0;i<arrayFruitSelected.length;i++){
					if(arrayFruitSelected[i]){
						if(!info.equals("")){
							info+= ","+arrayHouseUse[i];
						}else{
							info+= arrayHouseUse[i];
						}
					}
				}
				spinner.setText(info);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create();
		dialog.show();
	}
	
	class QueryHouseArea extends AsyncTask<String, Void, String> {
		String url="";
		@Override
		protected String doInBackground(String... params) {
			try {
				return HttpHelper.requestData(HouseConfig.METHOD_GETCITY, C.Url, null);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject success = new JSONObject(result);
				if(!success.getString("city").equals("[]")){
					JSONArray jsonArray = success.getJSONArray("city");
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String xdm=jsonObject.get("xzqhxqDm").toString();
						String cityName = jsonObject.get("mc").toString();
						houseArea.add(cityName);//
						houseAreaIds.add(xdm);//
					}
					arrayhouseArea=new String[houseArea.size()];
					arrayFruitSelectedArea=new boolean[houseArea.size()];
					
					for(int i=0;i<houseArea.size();i++){
						arrayhouseArea[i]=houseArea.get(i);
						arrayFruitSelectedArea[i]=false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	class QueryHouseUse extends AsyncTask<String, Void, String>{
		private String url = "";
		@Override
		protected String doInBackground(String... params) {
			Parameter parameter = new Parameter();
			parameter.setHouseuse(HouseConfig.HOUSEUSE);
			url=HttpHelper.getUrl(HouseConfig.METHOD_HOUSEUSE, C.Url, parameter);
			return HttpHelper.getWebData(url);
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject success = new JSONObject(result);
				if(success.getBoolean("success")&&!success.getString("data").equals("[]")){
					JSONArray jsonArray = success.getJSONArray("data");
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String name = jsonObject.get("name").toString();
						houseUse.add(name);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		thisDate=(EditText) v;
		Calendar d = Calendar.getInstance(Locale.CHINA);
		Date myDate=HouseUtil.stringFormatDate(thisDate.getText().toString());
		//创建一个Date实例
		d.setTime(myDate);
		//设置日历的时间，把一个新建Date实例myDate传入
		int year=d.get(Calendar.YEAR);
		int month=d.get(Calendar.MONTH);
		int day=d.get(Calendar.DAY_OF_MONTH);
		//获得日历中的 year month day
		DatePickerDialog dlg=new DatePickerDialog(this,this,year,month,day);
		//新建一个DatePickerDialog 构造方法中         
		//（设备上下文，OnDateSetListener时间设置监听器，默认年，默认月，默认日）
		dlg.show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		thisDate.setText(Integer.toString(year) + "-" +
				Integer.toString(monthOfYear+1) + "-" +
				Integer.toString(dayOfMonth));
	}
	
	/**
	 * 返回上一级
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
		}
		return false;
	}
}
