package com.dxsoft.houseApp.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.dxsoft.houseApp.R;

public class CreatedReportListAdapter extends SimpleAdapter {
	

	private static String[] fields = { "name", "createTime" };
	private static int[] resIds = { R.id.tv_name, R.id.tv_createtime };
	
	public CreatedReportListAdapter(Context context, List<Map<String, Object>> data) {
		super(context, data, R.layout.view_reportbase_list_item, fields, resIds);
	}

}
