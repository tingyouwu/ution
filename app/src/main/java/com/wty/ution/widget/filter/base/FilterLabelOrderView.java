package com.wty.ution.widget.filter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.wty.ution.R;

import java.util.List;
import java.util.UUID;

public class FilterLabelOrderView extends LinearLayout implements OnClickListener,FilterLabelContainerView.IContainerView {

	private LinearLayout btn_updatetime;
	private LinearLayout btn_createtime;
	private View alphaView;
	private String viewid;
	
	private IFilterModel createtime,onlive;
	private FilterEventBus eventBus;
	private String eventId;
	
	public FilterLabelOrderView(Context context) {
		super(context);
		
//		createtime = new CustomerFilterOrderbyCreatetimeModel(getContext());
//		onlive = new CustomerFilterOrderbyOnliveModel(getContext());
		
		viewid = UUID.randomUUID().toString();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_filterlabel_order, this);
		
		btn_updatetime = (LinearLayout)findViewById(R.id.filterlabel_order_update);
		btn_createtime = (LinearLayout)findViewById(R.id.filterlabel_order_create);
		alphaView = findViewById(R.id.filterlabel_order_alphaview);
		
		alphaView.setOnClickListener(this);
		btn_updatetime.setOnClickListener(this);
		btn_createtime.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filterlabel_order_update:
			if(eventBus==null)return;
			eventBus.onOrderEvent(getEventId(),onlive);
			eventBus.onViewShow(getEventId(),getViewId(),false);
			break;
		case R.id.filterlabel_order_create:
			if(eventBus==null)return;
			eventBus.onOrderEvent(getEventId(),createtime);
			eventBus.onViewShow(getEventId(),getViewId(),false);
			break;
		case R.id.filterlabel_order_alphaview:
			if(eventBus==null)return;
			eventBus.onViewShow(getEventId(),getViewId(),false);
			break;
		default:
			break;
		}
	}

	@Override
	public String getViewId() {
		return viewid;
	}

    @Override
    public void onRegister(FilterEventBus eventBus) {
        this.eventBus = eventBus;
    }

	@Override
	public void resetView() {
	}

	@Override
	public void onViewShow(String viewid, boolean show) {
	}

	@Override
	public void onButtonEvent(String buttonid, boolean selected) {
	}

	@Override
	public void onSubmit(IFilterModel filter) {
	}

	@Override
	public void onSubmit(List<IFilterModel> filters) {
	}

	@Override
	public void onCancel(IFilterModel filter) {
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onOrderEvent(IFilterModel model) {
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public void setEventId(String eventId){
		this.eventId = eventId;
	}
}