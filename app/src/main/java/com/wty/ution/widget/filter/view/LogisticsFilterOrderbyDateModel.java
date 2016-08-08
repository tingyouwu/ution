package com.wty.ution.widget.filter.view;


import android.content.Context;

import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.widget.filter.base.AbstractFilterModel;

public class LogisticsFilterOrderbyDateModel extends AbstractFilterModel {

	private static final long serialVersionUID = 1L;

	public LogisticsFilterOrderbyDateModel(Context context) {
		super(context, LogisticsDALEx.XWDATE, InputMode.Select, "发货时间");
		
	}
	
	@Override
	public void onSelect(SelectCallback callback) {
		super.onSelect(callback);
	}

	@Override
	public String toSql() {
		return " datetime("+filterid+") desc";
	}
	
}