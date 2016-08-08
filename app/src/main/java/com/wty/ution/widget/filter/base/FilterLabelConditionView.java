package com.wty.ution.widget.filter.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.wty.ution.R;
import com.wty.ution.widget.filter.base.FilterLabelContainerView.IContainerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public abstract class FilterLabelConditionView extends LinearLayout implements IContainerView,OnClickListener{

	protected LinearLayout container;
	protected List<IFilterModel> filters = new ArrayList<IFilterModel>();
	protected Map<String,FilterLabelItemView> items = new HashMap<String,FilterLabelItemView>();
	
	protected LinearLayout btn_cancel,btn_ok;
	protected String viewid;
	
	protected FilterEventBus eventBus;
	protected String eventId;
	private View alphaview;
	private Context mContext;

    private Map<String,Boolean> disableModel = new HashMap<String,Boolean>();//人工禁止操作

    public FilterLabelConditionView(Context context,AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

	public FilterLabelConditionView(Context context) {
		super(context);
        init(context);
	}

    private void init(Context context){
        mContext = context;
        viewid = UUID.randomUUID().toString();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_filterlabel_condition, this);

        alphaview = findViewById(R.id.filterlabel_alphaview);
        container = (LinearLayout)findViewById(R.id.filterlabel_condition_container);
        btn_ok = (LinearLayout)findViewById(R.id.filterlabel_condition_ok);
        btn_cancel = (LinearLayout)findViewById(R.id.filterlabel_condition_cancel);


        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        alphaview.setOnClickListener(this);

        resetView();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filterlabel_condition_ok:
        case R.id.filterlabel_alphaview:
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
            syncValue();
			if(eventBus == null)return;
			//注册事件总线
			eventBus.onViewShow(getEventId(), getViewId(),false);
			List<IFilterModel> submits = new ArrayList<IFilterModel>();
			for(IFilterModel model:filters){
				if(!TextUtils.isEmpty(model.getValue())){
					submits.add(model);
				}
			}
			eventBus.onSubmit(getEventId(),submits);

			break;

		case R.id.filterlabel_condition_cancel:
			for(IFilterModel filter : filters){
                if(disableModel.containsKey(filter.getFilterId()))continue;
				filter.clearValue();
				items.get(filter.getFilterId()).setModel(filter);
			}
			
			if(eventBus == null)return;
			eventBus.onCancel(getEventId());
			break;

		default:
			break;
		}
	}

	@Override
	public void setEventId(String eventId){
		this.eventId = eventId;
	}
	
	@Override
	public String getViewId() {
		return viewid;
	}

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;
        for(FilterLabelItemView item: items.values()){
            item.setEventBus(eventBus,eventId);
        }
    }

    @Override
	public void onViewShow(String viewid, boolean show) {
        if(!show){
            syncValue();
        }
	}

	@Override
	public void onButtonEvent(String buttonid, boolean selected) {
	}

	@Override
	public void onSubmit(IFilterModel filter) {

        for(IFilterModel f:filters){
            if(f.getFilterId().equals(filter.getFilterId())){

                f.setValue(filter.getValue());
                f.setTextValue(filter.getTextValue());

                items.get(f.getFilterId()).setModel(f);
            }
        }

	}

	@Override
	public void onSubmit(List<IFilterModel> submits) {

        resetView();
        Map<String,IFilterModel> map = new HashMap<String,IFilterModel>();
        for(IFilterModel filter:filters){
            map.put(filter.getFilterId(),filter);
        }

        for(IFilterModel filter:submits){
            map.get(filter.getFilterId()).setValue(filter.getValue());
            map.get(filter.getFilterId()).setTextValue(filter.getTextValue());
            items.get(filter.getFilterId()).setModel(map.get(filter.getFilterId()));
        }


	}

	@Override
	public void onCancel(IFilterModel filter) {
        if(disableModel.containsKey(filter.getFilterId()))return;
		for(IFilterModel f:filters){
			if(f.getFilterId().equals(filter.getFilterId())){
                f.clearValue();
			}
		}

        filter.setValue("");
        filter.setTextValue("");
		items.get(filter.getFilterId()).setModel(filter);
		System.out.println("on Cancel 刷新列表");
		
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

    private void syncValue(){
        for(IFilterModel model:filters){
            IFilterModel itemModel = items.get(model.getFilterId()).getFilterModel();
            model.setValue(itemModel.getValue());
            model.setTextValue(itemModel.getTextValue());
        }
    }

    public List<IFilterModel> getFilters(){
        return filters;
    }

    public Map<String,IFilterModel> getFilterMap(){
        Map<String,IFilterModel> result = new HashMap<String,IFilterModel>();
        for(IFilterModel f:filters){
            result.put(f.getFilterId(),f);
        }
        return result;
    }

    public void setDisableModel(String filterId){
        disableModel.put(filterId,true);
    }
}