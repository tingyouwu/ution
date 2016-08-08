package com.wty.ution.widget.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.report.ReportGlassSellDetailActivity;
import com.wty.ution.ui.report.ReportLogisticsDetailActivity;
import com.wty.ution.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class CountReportListView extends NoScrollListView {

	public CountReportListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		data = new ArrayList<Report>();
		data.add(new Report(R.drawable.img_report_accomplishment, "物流信息表", ReportLogisticsDetailActivity.class));
		data.add(new Report(R.drawable.img_report_newoppor, "眼镜销售信息表", ReportGlassSellDetailActivity.class));
//		data.add(new Report(R.drawable.img_report_newsigned, "新增签约", ReportLogisticsDetailActivity.class));
		
		setAdapter(new ReportListAdapter());
		setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getContext(),data.get(position).intentClass);
				getContext().startActivity(i);
			}
		});
	}
	List<Report> data;
	
	class Report{
		public Report(int icon,String label,Class<? extends BaseActivity> intentClass) {
			this.icon = icon;
			this.label = label;
			this.intentClass = intentClass;
		}
		
		Class<? extends BaseActivity> intentClass;
		int icon;
		String label;
	}
	
	
	class ReportListAdapter extends BaseAdapter{

		public ReportListAdapter() {
		}
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Report getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.item_count_list,null);
				holder = new ViewHolder();
				holder.init(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			Report report = getItem(position);
			holder.setValue(report,position);
			return view;
		}
		
		class ViewHolder{
			TextView tv_label;
			ImageView iv_icon;
			View v_line;
			
			public void init(View view){
				tv_label = (TextView)view.findViewById(R.id.item_report_name);
				iv_icon = (ImageView)view.findViewById(R.id.item_report_img);
				v_line = view.findViewById(R.id.item_report_line);
			}
			
			public void setValue(Report report,int position){
				tv_label.setText(report.label);
				iv_icon.setImageResource(report.icon);
				if(position == (data.size()-1))
					v_line.setVisibility(View.GONE);
				else
					v_line.setVisibility(View.VISIBLE);
			}
		}
	}
	
}
