package com.wty.ution.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.ui.activity.logistics.LogisticsInfoActivity;
import com.wty.ution.util.CommonUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 物流信息adapter
 **/
public class LogisticsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	protected Context context;
	public List<LogisticsDALEx> data;
	protected int viewResource;
	private MyItemLongClickListener mLongClickListener;

	public LogisticsListAdapter(Context context, List<LogisticsDALEx> data) {
		this.context = context;
		this.data = data;
		setViewResource(R.layout.item_logistics_list);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public Object getItem(int position){
		return data.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(viewResource,null);
		return new LogisticsListViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		final LogisticsDALEx dalex = (LogisticsDALEx)getItem(position);
		LogisticsListViewHolder viewHolder = (LogisticsListViewHolder)holder;
		viewHolder.item_logistics_source.setText(RegionInfoDALEx.get().queryById(dalex.getXwsource()).getNamepath());
		viewHolder.item_terminal_place.setText(RegionInfoDALEx.get().queryById(dalex.getXwdestination()).getNamepath());
		viewHolder.item_logistics_cost.setText(dalex.getXwprice());
		viewHolder.item_logistics_date.setText(CommonUtil.dateToYYYYMMdd(dalex.getXwdate()));
		viewHolder.item_logistics_time.setVisibility(View.GONE);
		if(position!=0){
			LogisticsDALEx predalex = (LogisticsDALEx)getItem(position-1);
			String datePre = CommonUtil.dateToYYYYMMdd(predalex.getXwdate());
			String dateNow = CommonUtil.dateToYYYYMMdd(dalex.getXwdate());
			viewHolder.initHeadView(datePre, dateNow);
		} else {
			viewHolder.showHeadView();
		}

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LogisticsInfoActivity.class);
				intent.putExtra(LogisticsInfoActivity.TAG,dalex);
				context.startActivity(intent);
			}
		});

		viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(mLongClickListener != null){
					mLongClickListener.onItemLongClick(v, position);
				}
				return true;
			}
		});
	}

	class LogisticsListViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.item_logistics_source) TextView item_logistics_source;
		@Bind(R.id.item_terminal_place) TextView item_terminal_place;
		@Bind(R.id.item_logistics_cost) TextView item_logistics_cost;
		@Bind(R.id.item_logistics_time) TextView item_logistics_time;
		@Bind(R.id.item_logistics_date) TextView item_logistics_date;
		@Bind(R.id.item_logistics_timelayout) View item_logistics_timelayout;
		@Bind(R.id.item_logistics_marginView) View item_logistics_marginView;

		View itemView;

		public LogisticsListViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			ButterKnife.bind(this, itemView);
		}

		public void initHeadView(String datePro, String dateNow) {
			if(item_logistics_timelayout != null && item_logistics_marginView != null) {
				if (!datePro.equals(dateNow)) {
					item_logistics_timelayout.setVisibility(View.VISIBLE);
					item_logistics_marginView.setVisibility(View.GONE);
				} else {
					item_logistics_timelayout.setVisibility(View.GONE);
					item_logistics_marginView.setVisibility(View.VISIBLE);
				}
			}
		}

		public void showHeadView() {
			item_logistics_timelayout.setVisibility(View.VISIBLE);
			item_logistics_marginView.setVisibility(View.GONE);
		}

	}

	public void setViewResource(int viewResource){
		this.viewResource=viewResource;
	}

	public void setOnItemLongClickListener(MyItemLongClickListener listener){
		this.mLongClickListener = listener;
	}

	public interface MyItemLongClickListener {
		public void onItemLongClick(View view,int postion);
	}
}
