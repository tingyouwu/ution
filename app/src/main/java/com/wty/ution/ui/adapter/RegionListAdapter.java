package com.wty.ution.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wty.ution.R;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;

import java.util.List;

public class RegionListAdapter extends BaseAdapter {
	private Context context;
	//列表数据源
	private List<RegionInfoDALEx> data;

    public RegionListAdapter(Context context, List<RegionInfoDALEx> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public RegionInfoDALEx getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_region_selected,null);
			holder = new ViewHolder();
			holder.init(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		RegionInfoDALEx region = getItem(position);
		holder.setValue(region);


		return view;
	}

	class ViewHolder {
		
		TextView tv_region;

		public void init(final View view) {
			tv_region = (TextView) view.findViewById(R.id.item_region_text);
		}

		public void setValue(final RegionInfoDALEx dalex) {
		    tv_region.setText(dalex.getRegionname());
		}
	}
	
}