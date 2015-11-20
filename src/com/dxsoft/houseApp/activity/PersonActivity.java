package com.dxsoft.houseApp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.util.MyAdapter;
import com.dxsoft.houseApp.R;

public class PersonActivity extends Activity {
	private String listname;
	private TextView title;
	
	private RelativeLayout backlayout;
	
	private String TAG = this.getClass().getName();
	private Button button;
	private Button button1;
	private List<String> list = new ArrayList<String>();  
	private AlertDialog dialog;
	private EditText editText;
	private EditText editTextType;
	private String[] dataCity;
	private String[] dataCity_type= new String[]{"住宅类","非住宅类"};
	private boolean[] dataCity_type_selected= new boolean[]{false, false};
	private boolean[] arrayFruitSelected = new boolean[] {false, false,false, false, false};
	//日期控件
	private EditText startText;
	private EditText endText;
	private Calendar cdar=Calendar.getInstance(); 
	private int mYear=cdar.get(Calendar.YEAR);
	private int mMonth=cdar.get(Calendar.MONTH);
	private int mDay=cdar.get(Calendar.DATE);
	
	private HashMap<Integer, Object> hashMap= new HashMap<Integer, Object>();
	
//private List<String> Selected= new ArrayList<String>();
	private String selected = "";

	
	private ListView  listView;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		initData();
		initView();
		setListener();
	}
	
	private void initView() {
		editText = (EditText) this.findViewById(R.id.et_text);
		editTextType=(EditText) findViewById(R.id.et_text_type);
		startText = (EditText) findViewById(R.id.startDate);
		endText = (EditText) findViewById(R.id.endDate);
		button = (Button) findViewById(R.id.chaxun);
		/*button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			     startActivity(new Intent(PersonActivity.this, House_ksActivity.class));
			 	finish();
			}
		});*/
		listView =  (ListView) findViewById(R.id.listview);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new readData().execute(new Parameter());
			}
		});
		startText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onCreateDialog(HouseConfig.STARTDATE_DIALOG_ID).show();
			}			
		});
		endText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onCreateDialog(HouseConfig.ENDDATE_DIALOG_ID).show();
			}
			
		});
		editText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showDialogCity();
					}
		});
		editTextType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogType();
			}
		});
	}
	//初始化数据
	private void initData() {
		Intent intent = this.getIntent();
		listname=intent.getStringExtra(HouseConfig.KEY_TITLE);
		title=(TextView) findViewById(R.id.title);
		title.setText(listname);
		backlayout=(RelativeLayout) findViewById(R.id.housebacklayout);
		//加载城市数据
		new GetData().execute(new Parameter());
	}
	
	//初始化事件监听器
	private void setListener() {
		
		backlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			}
		});
	}
	
	//加载城市数据
	public void showDialogCity(){
		dialog = new AlertDialog.Builder(PersonActivity.this)  
		.setTitle("请选择地区") 
		.setMultiChoiceItems(dataCity,arrayFruitSelected,new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			String s = "";
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
				String s = "";
				selected = "";
				for (int i = 0; i < arrayFruitSelected.length; i++) {
					 if (arrayFruitSelected[i] == true)
				       {
						 s +=dataCity[i]+ ",";
						 selected += hashMap.get(i) + ",";
				       }
				}
				editText.setText(s.substring(0, s.length()-1));
				selected.substring(0, selected.length()-1);
			}
				
			
		})               
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				editText.setText("");
			}
		}).create(); 
		dialog.show();
	}
	
	//加载房屋用途
	public void showDialogType(){
		dialog = new AlertDialog.Builder(PersonActivity.this)  
		.setTitle("请选择类型") 
		.setMultiChoiceItems(dataCity_type,dataCity_type_selected,new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//住房类型
				String s_countrytype = "";
				for (int i = 0; i < dataCity_type_selected.length; i++) {
					 if (dataCity_type_selected[i] == true)
				       {
						 s_countrytype +=dataCity_type[i] + ",";
				        //Toast.makeText(PersonActivity.this, UnitType[i], 3000).show();
				       }
				}

				 editTextType.setText(s_countrytype.substring(0, s_countrytype.length()-1));
				 
			}
				
			
		})                 
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				editTextType.setText("");
			}
		}).create(); 
		dialog.show();
	}
	//客服端获取值传给服务端后台
	class readData extends AsyncTask<Parameter, Void, String>{

		private String url = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		
		@Override
		protected String doInBackground(Parameter... param) {
			// TODO Auto-generated method stub
			Parameter p= new Parameter();
			p.setCountry(selected);
			int countryType = 0;
			if(editTextType.getText().toString().equals("住宅类,非住宅类")){
				p.setCountryType(countryType);
			}else if (editTextType.getText().toString().equals("住宅类")) {
				countryType=1;
				p.setCountryType(countryType);
			}else if (editTextType.getText().toString().equals("非住宅类")){
				countryType=2;
				p.setCountryType(countryType);
			}
			//Toast.makeText(PersonActivity.this, editTextType.getText().toString(), 3000).show();
			p.setDate(startText.getText().toString());
			p.setDate1(endText.getText().toString());
			System.out.println(p);
			try {
				return HttpHelper.requestData(HouseConfig.METHOD_GETLIST, C.Url, p);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject json1= new JSONObject(result);
				JSONArray jsonArray= json1.getJSONArray("data");
				List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
				for(int i=0;i<jsonArray.length();i++){
					HashMap<String,Object> map=new HashMap<String,Object>();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String roomNum = jsonObject.get("roomNum").toString();
					String area = jsonObject.get("area").toString();
					String date = jsonObject.get("registerDate").toString();
					map.put("roomNum",roomNum);
					map.put("area",area);
					map.put("date",date);
					list.add(map);
				}
				
				MyAdapter adp= new MyAdapter(PersonActivity.this, list,
		        		R.layout.item_list, new String[]{"roomNum","area","date"}, new int[]{R.id.list_num_zhi,R.id.list_area_zhi,R.id.list_year_zhi});
		        listView.setAdapter(adp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	//获取城市数据
	class GetData extends AsyncTask<Parameter, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		
		@Override
		protected String doInBackground(Parameter... arg0) {
			
			try {
				return HttpHelper.requestData(HouseConfig.METHOD_GETCITY, C.Url, null);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List list= new ArrayList();
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("city");
				for(int i=0;i<jsonArray.length();i++){
					String city = jsonArray.getJSONObject(i).getString("mc");
					String mc_dm=jsonArray.getJSONObject(i).getString("xzqhxqDm");
					hashMap.put(i, mc_dm);
					list.add(city);
				}
				//讲 集合转成数组
				dataCity = (String[]) list.toArray(new String[list.size()]); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**  
     * 结束时间
     */  
    private void startDateDisplay(){  
       startText.setText(new StringBuilder().append(mYear).append("-")  
               .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")  
               .append((mDay < 10) ? "0" + mDay : mDay));   
    } 
    
    /**  
     * 结束时间
     */  
    private void endeDateDisplay(){  
       endText.setText(new StringBuilder().append(mYear).append("-")  
               .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")  
               .append((mDay < 10) ? "0" + mDay : mDay));   
    }  
    /**   
     * 日期控件的事件   
     */    
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {    
    
       public void onDateSet(DatePicker view, int year, int monthOfYear,    
              int dayOfMonth) {    
           mYear = year;    
           mMonth = monthOfYear;    
           mDay = dayOfMonth;   
           startDateDisplay();
       }    
    };   
    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {    
        
        public void onDateSet(DatePicker view, int year, int monthOfYear,    
               int dayOfMonth) {    
            mYear = year;    
            mMonth = monthOfYear;    
            mDay = dayOfMonth;   
            endeDateDisplay();
        }    
     };  
    
    @Override    
    protected Dialog onCreateDialog(int id) {  
       switch (id) {    
       case HouseConfig.STARTDATE_DIALOG_ID:   
           return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,    
                  mDay);    
       case HouseConfig.ENDDATE_DIALOG_ID:   
	        return new DatePickerDialog(this, mDateSetListener1, mYear, mMonth,    
	               mDay);
	    } 
       return null; 
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
