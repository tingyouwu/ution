package com.wty.ution.widget.filter.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.filter.FilterDateSelectActivity;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterOptionModel;
import com.wty.ution.widget.filter.base.IFilterModel;


public class ExpandFilterDateModel extends AbstractFilterModel {

	private static final long serialVersionUID = 1L;
	protected int requestCode = 19996;
	protected IFilterModel.SelectCallback callback;
    protected String begin;
    protected String end;

	public ExpandFilterDateModel(Context context, FieldDescriptDALEx descript) {
		super(context,descript,InputMode.Select);
		init();
	}

    private void init(){
        BaseActivity activity = (BaseActivity)context;
        activity.registerOnActivityResultListener(new OnActivityResultListener(activity, requestCode) {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String fieldname = data.getStringExtra(FilterDateSelectActivity.Tag_Fieldname);
                    if (!filterid.equals(fieldname)) return;
                    FilterOptionModel selected = (FilterOptionModel) data.getSerializableExtra(FilterDateSelectActivity.Tag_Selected);
                    if (selected != null && callback != null) {
                        callback.onResult(selected);
                    }
                }
                callback = null;
            }
        });
    }

	@Override
	public void onSelect(SelectCallback callback) {
		super.onSelect(callback);
		this.callback = callback;
		
		Intent i = new Intent(context,FilterDateSelectActivity.class);
		i.putExtra(FilterDateSelectActivity.Tag_Title, label);
        i.putExtra(FilterDateSelectActivity.Tag_Fieldname, filterid);
		if(!TextUtils.isEmpty(value)){
			i.putExtra(FilterDateSelectActivity.Tag_Selected, new FilterOptionModel(textValue, value));

		}
		
		((BaseActivity)context).startActivityForResult(i, requestCode);
	}

	@Override
	public String toSql() {
		try {
			if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(filterid)){
				return "";
			}else{
				String[] valueArray = value.split(",");

				if(valueArray.length==1 || valueArray[0].equals(valueArray[1])){
					return " date("+filterid+") = date('"+valueArray[0]+"')";
				}else{
					return " date("+filterid+") >= date('"+valueArray[0]+"') and date("+ filterid + ") <= date('"+valueArray[1]+"')";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public void clearValue() {
        super.clearValue();
        this.begin = "";
        this.end = "";
    }

}