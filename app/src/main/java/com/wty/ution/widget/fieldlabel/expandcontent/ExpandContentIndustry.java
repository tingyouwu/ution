package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.dalex.basedata.IndustryinfoDALEx;
import com.wty.ution.widget.fieldlabel.SingleOptionItem;

import java.util.List;


public class ExpandContentIndustry extends ExpandContentSingleSelect {
	public final static int FieldType = 15;
	public ExpandContentIndustry(Context context) {
		super(context);
	}

	@Override
	public void setFieldDescript(FieldDescriptDALEx descript) {
		super.setFieldDescript(descript);
		List<IndustryinfoDALEx> industrys = IndustryinfoDALEx.get().queryAll();
		if(industrys.size()==0)return;
		selectItems.add(new SingleOptionItem("无","",""));
		for (IndustryinfoDALEx industry : industrys) {
			selectItems.add(new SingleOptionItem(industry.getXwindustryname(),industry.getXwindustryname(),industry.getXwindustryid()));
		}
		
	}
	
}
