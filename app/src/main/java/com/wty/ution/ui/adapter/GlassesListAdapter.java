package com.wty.ution.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.ui.activity.glass.GlassInfoActivity;
import com.wty.ution.util.CommonUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 眼镜销售信息adapter
 **/
public class GlassesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	protected Context context;
	public List<GlassesDALEx> data;
	protected int viewResource;
	private MyItemLongClickListener mLongClickListener;

	public GlassesListAdapter(Context context, List<GlassesDALEx> data) {
		this.context = context;
		this.data = data;
		setViewResource(R.layout.item_glasses_list);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public Object getItem(int position){
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(viewResource, null);
		return new GlassesListViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		final GlassesDALEx dalex = (GlassesDALEx)getItem(position);
		GlassesListViewHolder viewHolder = (GlassesListViewHolder)holder;
		viewHolder.item_glass_name.setText(dalex.getXwtype());
		viewHolder.item_glass_size.setText("["+dalex.getXwsize()+"码]");
		viewHolder.item_glass_sellprice.setText(""+dalex.getXwsellprice());
		viewHolder.item_glass_profit.setText(""+dalex.getXwprofit()+"元");
		viewHolder.item_glass_date.setText(CommonUtil.dateToYYYYMMdd(dalex.getXwdate()));
		if(position!=0){
			GlassesDALEx predalex = (GlassesDALEx)getItem(position-1);
			String datePre = CommonUtil.dateToYYYYMMdd(predalex.getXwdate());
			String dateNow = CommonUtil.dateToYYYYMMdd(dalex.getXwdate());
			viewHolder.initHeadView(datePre, dateNow);
		} else {
			viewHolder.showHeadView();
		}

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, GlassInfoActivity.class);
				intent.putExtra(GlassInfoActivity.TAG,dalex);
				context.startActivity(intent);
			}
		});

		viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mLongClickListener != null) {
					mLongClickListener.onItemLongClick(v, position);
				}
				return true;
			}
		});
	}

	public void setViewResource(int viewResource){
	    this.viewResource=viewResource;
	}

	class GlassesListViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.item_glass_type) TextView item_glass_name;
		@Bind(R.id.item_glass_sell) TextView item_glass_sellprice;
		@Bind(R.id.item_glass_size) TextView item_glass_size;
		@Bind(R.id.item_glass_date) TextView item_glass_date;
		@Bind(R.id.item_glass_profit) TextView item_glass_profit;
		@Bind(R.id.item_glass_timelayout) View item_glass_timelayout;
		@Bind(R.id.item_glass_marginView) View item_glass_marginView;

		View itemView;

		public GlassesListViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			ButterKnife.bind(this, itemView);
		}

		public void initHeadView(String datePro, String dateNow) {
			if(item_glass_timelayout != null && item_glass_marginView != null) {
				if (!datePro.equals(dateNow)) {
					item_glass_timelayout.setVisibility(View.VISIBLE);
					item_glass_marginView.setVisibility(View.GONE);
				} else {
					item_glass_timelayout.setVisibility(View.GONE);
					item_glass_marginView.setVisibility(View.VISIBLE);
				}
			}
		}

		public void showHeadView() {
			item_glass_timelayout.setVisibility(View.VISIBLE);
			item_glass_marginView.setVisibility(View.GONE);
		}

	}

	public void setOnItemLongClickListener(MyItemLongClickListener listener){
		this.mLongClickListener = listener;
	}

	public interface MyItemLongClickListener {
		public void onItemLongClick(View view,int postion);
	}
}
