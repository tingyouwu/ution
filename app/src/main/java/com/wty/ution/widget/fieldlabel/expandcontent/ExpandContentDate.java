package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;

public class ExpandContentDate extends ExpandContentBase {
	public final static int FieldType = 7;
	public ExpandContentDate(Context context) {
		super(context);
		setClickMode(true);
		setViewByType(Type_Date);
		setDefaultDate(new Date());
	}
	
	@Override
	protected String makeMessagePrefix() {
		return "请选择"; 
	}
	
	@Override
	public void setFieldValue(String value) {
        if(TextUtils.isEmpty(value)){
            edt_content.setText("");
            tv_content.setText("");
            return;
        }

		super.setValue(value);
		setDefaultDate(value);
	}

    @Override
    public void clearFieldValue() {
        super.clearFieldValue();
        setDefaultDate(new Date());
    }
}