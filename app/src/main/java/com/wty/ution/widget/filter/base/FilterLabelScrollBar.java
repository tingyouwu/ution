package com.wty.ution.widget.filter.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wty.ution.R;
import com.wty.ution.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 功能描述：筛选控件横向滑动条
 * @author Amberllo
 *
 */
public class FilterLabelScrollBar extends HorizontalListView implements IFilterEvent,OnItemClickListener{
	private ScrollBarAdapter adapter;
	private List<IFilterModel> options = new ArrayList<IFilterModel>();
	private String eventId;
	private FilterEventBus eventBus;
	
	private boolean operateAble = false;

    private Map<String,Boolean> disableModel = new HashMap<String,Boolean>();//人工禁止操作

	public FilterLabelScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(false);
		eventId = UUID.randomUUID().toString();
		adapter = new ScrollBarAdapter(options);
		setAdapter(adapter);
		setOnItemClickListener(this);
		notifyVisable();

	}

	public void setOptions(List<IFilterModel> options) {
		this.options = options;
		adapter.models = options;
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}
	
	class ScrollBarAdapter extends  BaseAdapter{
		List<IFilterModel> models;
 		public ScrollBarAdapter(List<IFilterModel> models) {
 			this.models = models;
		}
		
		@Override
		public int getCount() {
			return models.size();
		}

		@Override
		public IFilterModel getItem(int position) {
			return models.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = new Holder();
			if( convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filterlabel_scrollbar, null);
				holder.tv_name = (TextView)convertView.findViewById(R.id.item_flb_scroll_itemname);
				holder.tv_label = (TextView)convertView.findViewById(R.id.item_flb_scroll_label);
				holder.iv_cancel = (ImageView)convertView.findViewById(R.id.item_flb_scroll_cancel);
				convertView.setTag(holder);
				
				convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if(hasFocus)Log.v("性能调试", "scrollbar item 获得焦点");
					}
				});
			} else {
				holder = (Holder)convertView.getTag();
			}
			IFilterModel model = getItem(position);
			holder.tv_name.setText(model.getTextValue());
			holder.tv_label.setText(model.getLabel());
			if(operateAble){
                if(disableModel.containsKey(model.getFilterId())){
                    holder.iv_cancel.setVisibility(View.GONE);
                }else{
                    holder.iv_cancel.setVisibility(View.VISIBLE);
                }

			}else{
				holder.iv_cancel.setVisibility(View.GONE);
			}
			return convertView;
		}
		
		class Holder{
			TextView tv_label;
			TextView tv_name;
			ImageView iv_cancel;
		}
	}

	@Override
	public void onViewShow(String viewid, boolean show) {
		if(show){
			this.operateAble = true;
		}else{
			this.operateAble = false;
		}
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onButtonEvent(String buttonid, boolean selected) {
	}

	@Override
	public void onSubmit(final List<IFilterModel> filters) {
		((Activity)getContext()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                options.clear();
                if (filters != null && filters.size() != 0) {
                    options.addAll(filters);
                    adapter.notifyDataSetChanged();
                }
                notifyVisable();

            }
        });
		
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public void onCancel() {
		((Activity)getContext()).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
                if(disableModel.size()==0){
                    options.clear();
                    adapter.notifyDataSetChanged();
                    notifyVisable();
                }else{
                    Iterator<IFilterModel> it = options.iterator();
                    while (it.hasNext()) {
                        IFilterModel f = it.next();
                        if(!disableModel.containsKey(f.getFilterId())){
                            it.remove();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

				
			}
		});
		
	}

	@Override
	public void onCancel(final IFilterModel filter) {
        if(disableModel.containsKey(filter.getFilterId()))return;

		((Activity)getContext()).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Iterator<IFilterModel> it = options.iterator();
				while (it.hasNext()) {
					IFilterModel f = it.next();
					if(f.getFilterId().equals(filter.getFilterId())){
						it.remove();
					}
				}
				
				adapter.notifyDataSetChanged();
				notifyVisable();
				
			}
			
		});
		
	}
	
	
	@Override
	public void onOrderEvent(IFilterModel model) {
		
	}

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;
    }

	@Override
	public void onSubmit(final IFilterModel filter) {
		((Activity)getContext()).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Map<String,IFilterModel> contains = new HashMap<String,IFilterModel>();
				for(IFilterModel f:options){
					contains.put(f.getFilterId(), f);
					if(f.getFilterId().equals(filter.getFilterId())){
						f.setTextValue(filter.getTextValue());
						f.setValue(filter.getValue());
					}
				}
				if(!contains.containsKey(filter.getFilterId())){
					options.add(filter);
				}
				adapter.notifyDataSetChanged();
				notifyVisable();
			}
		});
		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(!operateAble)return;
        IFilterModel model = (IFilterModel) getItemAtPosition(position);
        if(disableModel.containsKey(model.getFilterId()))return;
		if(model!=null && eventBus!=null && operateAble){
			eventBus.onCancel(getEventId(),model);
		}
		options.remove(model);
		adapter.notifyDataSetChanged();
		notifyVisable();
	}
	
	private void notifyVisable(){
		
		if(this.options.size()==0){
			setVisibility(View.GONE);
		}else{
			setVisibility(View.VISIBLE);
		}

	}

    public void setDisableModel(String filterId){
        disableModel.put(filterId,true);
    }

}