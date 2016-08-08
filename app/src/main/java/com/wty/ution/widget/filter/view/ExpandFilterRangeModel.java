package com.wty.ution.widget.filter.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.filter.FilterRangeActivity;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.fieldlabel.LabelEditText;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterRangeOptionModel;


public class ExpandFilterRangeModel extends AbstractFilterModel {

	private static final long serialVersionUID = 1L;
	private int requestCode = 20008;
	private SelectCallback callback;

    private int inputMode;

    private FilterRangeOptionModel selected;

    public ExpandFilterRangeModel(Context context, FieldDescriptDALEx descript) {
        super(context,descript,InputMode.Select);
        this.inputMode = LabelEditText.Type_Int;
        init();
    }

	public ExpandFilterRangeModel(Context context, FieldDescriptDALEx descript, int inputMode) {
		super(context,descript,InputMode.Select);
        this.inputMode = inputMode;
		init();
	}

    private void init(){
        BaseActivity activity = (BaseActivity)context;
        activity.registerOnActivityResultListener(new OnActivityResultListener(activity, requestCode) {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(resultCode == Activity.RESULT_OK ){

                    String fieldname = data.getStringExtra(FilterRangeActivity.Tag_Field);
                    if(!filterid.equals(fieldname))return;
                    selected = (FilterRangeOptionModel) data.getSerializableExtra(FilterRangeActivity.Tag_Selected);
                    if(selected!=null && callback!=null){
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
		Intent i = new Intent(context,FilterRangeActivity.class);
		i.putExtra(FilterRangeActivity.Tag_Title, label);
        i.putExtra(FilterRangeActivity.Tag_Label, label);
        i.putExtra(FilterRangeActivity.Tag_Field, filterid);
        i.putExtra(FilterRangeActivity.Tag_InputMode, inputMode);
		if(selected!=null){
			i.putExtra(FilterRangeActivity.Tag_Selected, selected);
		}
		((BaseActivity)context).startActivityForResult(i, requestCode);
	}

	@Override
	public String toSql() {
		if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(filterid)){
			return "";
		}else{
			String[] valueArray = value.split(",");
			if(valueArray.length==1){
				return filterid+" = "+value;
			}else if(valueArray[0].equals(valueArray[1])){
				return filterid+" = "+value;
			}else{
				return filterid+" between "+valueArray[0]+" and "+valueArray[1];
			}
		}
	}

    @Override
    public void clearValue() {
        super.clearValue();
        selected = null;
    }
}