package com.wty.ution.widget.filter.base;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;

public abstract class AbstractFilterModel implements IFilterModel{

	protected InputMode mode;
	protected String label;
	
	protected String textValue;
	
	protected String value;
	protected String filterid;
	
	protected Context context;

    FilterEventBus eventBus;

	public AbstractFilterModel(Context mContext, String filterid, InputMode mode, String label) {
		this.context = mContext;
		this.filterid = filterid;
		this.mode = mode;
		this.label = label;
	}

    public AbstractFilterModel(Context mContext, FieldDescriptDALEx descript, InputMode mode){
        this.context = mContext;
        this.mode = mode;
        this.label = descript.getXwfieldlabel();
        this.filterid= descript.getXwfieldname();
    }

	@Override
	public InputMode getInputMode() {
		return mode;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getTextValue() {
		return textValue;
	}

	@Override
	public String getFilterId() {
		return filterid;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	
	@Override
	public void onSelect(SelectCallback callback) {
	}

    @Override
    public void clearValue() {
        this.value = "";
        this.textValue = "";
    }

    public void onRegister(FilterEventBus eventBus){
        this.eventBus = eventBus;
    }

}