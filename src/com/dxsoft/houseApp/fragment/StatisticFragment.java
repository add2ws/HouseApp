package com.dxsoft.houseApp.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dxsoft.houseApp.R;
import com.dxsoft.houseApp.activity.MenuListDetailActivity;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.data.HouseSharePreference;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HttpHelper;

public class StatisticFragment extends Fragment {

	public static String tagName = "StatisticFragment";

	private RelativeLayout loadingView;
	private ListView listView;
	private Integer usersid;

	// 初始化图片数组
	private int[] imgArry = new int[] { R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6 };
	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistic, container, false);
		listView = (ListView) view.findViewById(R.id.lv_statistic);
		loadingView = (RelativeLayout) view.findViewById(R.id.rl_loading);
		usersid = Integer.valueOf(HouseSharePreference.getStringData(this.getActivity(), HouseConfig.KEY_USERSID));
		new MenuItemList().execute();
		return view;
	}
	
	public void reloadData() {
		loadingView.setVisibility(View.VISIBLE);//显示加载图标
		new MenuItemList().execute();
	}

	class MenuItemList extends AsyncTask<String, Void, String> {
		private String url = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			Parameter queryEntity = new Parameter();
			queryEntity.setUserSid(Integer.valueOf(usersid));
			queryEntity.setpSid(HouseConfig.F_PSID);
			url = HttpHelper.getUrl(HouseConfig.METHOD_MENUITEMLIST, C.Url, queryEntity);
			return HttpHelper.getWebData(url);

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject success = new JSONObject(result);
				if (success.getBoolean("success") && !success.getString("data").equals("[]")) {
					list.clear();
					JSONArray jsonArray = success.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String sid = jsonObject.get("sid").toString();
						String name = jsonObject.get("name").toString();
						map.put("img_menu_bef", imgArry[i]);
						map.put("id", sid);
						map.put("listName", name);
						map.put("img_menu_end", R.drawable.arrow);
						int sortId;
						try {
							sortId = jsonObject.getInt("sortId");
						} catch (JSONException e) {
							sortId = 999;
						}
						map.put("sortId", sortId);
						list.add(map);
					}
					Collections.sort(list, new Comparator<Map<String, Object>>() {

						@Override
						public int compare(Map<String, Object> a, Map<String, Object> b) {

							return (Integer) a.get("sortId") - (Integer) b.get("sortId");
						}
					});
					SimpleAdapter sa = new SimpleAdapter(StatisticFragment.this.getActivity(), list, R.layout.menuitem, new String[] { "img_menu_bef", "listName", "img_menu_end" }, new int[] { R.id.img_menu_bef, R.id.listName, R.id.img_menu_end });
					listView.setAdapter(sa);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							String sid = list.get(arg2).get("id").toString();
							String listName = list.get(arg2).get("listName").toString();
							Intent intent = new Intent(StatisticFragment.this.getActivity(), MenuListDetailActivity.class);
							intent.putExtra(HouseConfig.KEY_USERSID, usersid);
							intent.putExtra(HouseConfig.KEY_ITEMID, sid);
							intent.putExtra(HouseConfig.KEY_TITLE, listName);
							StatisticFragment.this.startActivity(intent);
							StatisticFragment.this.getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
						}
					});
					;

				} else {
					Toast.makeText(StatisticFragment.this.getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			loadingView.setVisibility(View.GONE);//隐藏加载图标
			super.onPostExecute(result);
		}

	}

}
