package com.wty.ution.widget.filter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressLint("NewApi")
public class FilterLabelContainerView extends LinearLayout implements IFilterEvent{

	private Map<String,IContainerView> mContentViews = new HashMap<String,IContainerView>();
	
	private String eventId;
	private FilterEventBus eventBus;
	
	public FilterLabelContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		removeAllViews();
		eventId = UUID.randomUUID().toString();
		//注册事件总线
        
	}

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;

    }

    public void addContentView(IContainerView contentView){
		if(mContentViews.get(contentView.getViewId())==null){
			mContentViews.put(contentView.getViewId(), contentView);
			contentView.setEventId(getEventId());
            contentView.onRegister(eventBus);
		}
	}

	@Override
	public void onButtonEvent(String buttonid, boolean selected) {
		
	}

	@Override
	public void onSubmit(List<IFilterModel> filters) {
        for(IContainerView view :mContentViews.values()){
            view.onSubmit(filters);
        }
	}

	@Override
	public void onOrderEvent(IFilterModel filter) {
        for(IContainerView view :mContentViews.values()){
            view.onOrderEvent(filter);
        }
	}

	@Override
	public void onSubmit(IFilterModel filter) {
        for(IContainerView view :mContentViews.values()){
            view.onSubmit(filter);
        }
	}
	
	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public void onViewShow(String viewid, boolean show) {
		IContainerView content = mContentViews.get(viewid);
		removeAllViews();
		if(show && content!=null){
			addView((View)content);
		}
	}

	

	@Override
	public void onCancel(IFilterModel filter) {
        for(IContainerView view :mContentViews.values()){
            view.onCancel(filter);
        }
	}

	@Override
	public void onCancel() {
        for(IContainerView view :mContentViews.values()){
            view.resetView();
        }
		removeAllViews();
	}
	
	public interface IContainerView extends IFilterEvent{
		public void setEventId(String eventId);
		public String getViewId();
		public void onRegister(FilterEventBus eventBus);
		public void resetView();
	}
	
}