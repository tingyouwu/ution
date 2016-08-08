package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.text.TextUtils;

import com.wty.ution.util.CommonUtil;

import java.util.Date;

public class ExpandContentDateTime extends ExpandContentDate {
	public final static int FieldType = 8;
	public ExpandContentDateTime(Context context) {
		super(context);
		setClickMode(true);
		setViewByType(Type_DateTime);
		setDefaultDate(new Date());
	}
	
	@Override
	public void setFieldValue(String value) {
		if(TextUtils.isEmpty(value)){
			edt_content.setText("");
			tv_content.setText("");
			return;
		}

		
		String time =  CommonUtil.dateToFormat("yyyy-MM-dd HH:mm", value);
		super.setValue(time);
	}
	
}