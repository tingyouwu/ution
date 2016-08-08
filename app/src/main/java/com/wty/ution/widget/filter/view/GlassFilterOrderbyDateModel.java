package com.wty.ution.widget.filter.view;


import android.content.Context;

import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.widget.filter.base.AbstractFilterModel;

public class GlassFilterOrderbyDateModel extends AbstractFilterModel {

	private static final long serialVersionUID = 1L;

	public GlassFilterOrderbyDateModel(Context context) {
		super(context, GlassesDALEx.XWDATE, InputMode.Select, "发货时间");
		
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