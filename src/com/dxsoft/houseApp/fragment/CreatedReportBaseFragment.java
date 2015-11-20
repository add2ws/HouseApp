package com.dxsoft.houseApp.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dxsoft.houseApp.activity.ReportBasePreviewActivity;
import com.dxsoft.houseApp.base.C;
import com.dxsoft.houseApp.base.HouseConfig;
import com.dxsoft.houseApp.data.HouseSharePreference;
import com.dxsoft.houseApp.entity.Parameter;
import com.dxsoft.houseApp.util.HttpHelper;
import com.dxsoft.houseApp.R;

public class CreatedReportBaseFragment extends android.support.v4.app.Fragment {

	private final String TAG = this.getClass().getName();

	private ListView listView;
	private RelativeLayout loadingFrame;
	private TextView tvMore;
	private RelativeLayout moreLoadingFrame;

	private List<Map<String, Object>> listViewData;
	private SimpleAdapter adapter;

	private String userSid;
	private int curPage = 1;
	private int pageSize = 12;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_created_reportbase, container, false);
		listView = (ListView) rootView.findViewById(R.id.lv_my_reportbase);
		View footView = inflater.inflate(R.layout.view_load_more, null);
		listView.addFooterView(footView);
		tvMore = (TextView) footView.findViewById(R.id.tv_more);
		moreLoadingFrame = (RelativeLayout) footView.findViewById(R.id.rl_loading);

		loadingFrame = (RelativeLayout) rootView.findViewById(R.id.rl_loading);

		Parameter param = new Parameter();

		userSid = HouseSharePreference.getStringData(this.getActivity(), HouseConfig.KEY_USERSID);
		if (!TextUtils.isEmpty(userSid)) {
			param.setUserSid(Integer.valueOf(userSid));
			param.setPage(curPage);
			param.setRows(pageSize);
			new AsyncGetData().execute(param);
		}

		this.setListener();
		return rootView;
	}

	public void reloadData() {
		curPage = 1;
		Parameter p = new Parameter();
		p.setUserSid(Integer.valueOf(userSid));
		p.setPage(curPage);
		p.setRows(pageSize);
		new AsyncGetData().execute(p);
		loadingFrame.setVisibility(View.VISIBLE);
		
	}
	
	private void setListener() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
				if (position < listViewData.size()) {

					int sid = (Integer) listViewData.get(position).get("sid");
					String name = (String) listViewData.get(position).get("name");
					Intent intent = new Intent(CreatedReportBaseFragment.this.getActivity(), ReportBasePreviewActivity.class);
					intent.putExtra("sid", sid);
					intent.putExtra("name", name);
					CreatedReportBaseFragment.this.getActivity().startActivity(intent);
					CreatedReportBaseFragment.this.getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

				} else {// 点击了更多按钮
					if (tvMore.getText().equals("已无更多数据")) return;
					
					tvMore.setVisibility(View.GONE);
					moreLoadingFrame.setVisibility(View.VISIBLE);
					Parameter p = new Parameter();
					p.setUserSid(Integer.valueOf(userSid));
					p.setPage(++curPage);
					p.setRows(pageSize);
					new AsyncGetData().execute(p);
				}
			}
		});

	}

	/**
	 * 异步请求数据
	 */
	class AsyncGetData extends AsyncTask<Parameter, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Parameter... param) {
			String result = null;

			try {
				result = HttpHelper.requestData(HouseConfig.METHOD_MY_REPORTBASE, C.Url, param[0]);
			} catch (IOException e) {
				return "error";
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if (TextUtils.isEmpty(result)) {
				Toast.makeText(CreatedReportBaseFragment.this.getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
				return;
			} else if (result.equals("error")) {
				Toast.makeText(CreatedReportBaseFragment.this.getActivity(), "服务器故障，请稍后再试", Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				if (curPage == 1) {
					listViewData = new ArrayList<Map<String, Object>>();
					loadingFrame.setVisibility(View.GONE);
				}
				int total = 0;
				JSONObject jObj = new JSONObject(result);
				total = jObj.getInt("total");
				JSONArray arry = jObj.getJSONArray("rows");
				for (int i = 0; i < arry.length(); i++) {
					JSONObject obj = arry.getJSONObject(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("sid", obj.get("sid"));
					map.put("name", obj.getString("name"));
					map.put("createTime", "创建时间：" + obj.getString("createTime"));
					listViewData.add(map);
				}
				
				if (curPage == 1) {
					String[] fields = { "name", "createTime" };
					int[] resIds = { R.id.tv_name, R.id.tv_createtime };
					adapter = new SimpleAdapter(CreatedReportBaseFragment.this.getActivity(), listViewData, R.layout.view_reportbase_list_item, fields, resIds);
					listView.setAdapter(adapter);
					
				} else if (curPage > 1) {
					adapter.notifyDataSetChanged();
					tvMore.setVisibility(View.VISIBLE);
					moreLoadingFrame.setVisibility(View.GONE);
				}
				
				if (curPage >= (total  +  pageSize  - 1) / pageSize) {
					tvMore.setText("已无更多数据");
				} else {
					tvMore.setText("点击加载更多");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

	}

}
