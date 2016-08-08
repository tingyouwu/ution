package com.wty.ution.widget.filter.report;

import android.content.Context;
import android.util.AttributeSet;

import com.wty.ution.data.dalex.ExpandinfoDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.filter.ExpandFilterFactory;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterLabelConditionView;
import com.wty.ution.widget.filter.base.FilterLabelItemView;
import com.wty.ution.widget.filter.base.IFilterModel;

import java.util.List;


public class ReportGlassFilterLabelConditionView extends FilterLabelConditionView {

	public ReportGlassFilterLabelConditionView(Context context) {
		super(context);
	}
    public ReportGlassFilterLabelConditionView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

	@Override
	public void resetView(){

		Context context = getContext();
		filters.clear();

		List<FieldDescriptDALEx> descripts = FieldDescriptDALEx.get().queryByEntityregname(ExpandinfoDALEx.Name_Glass);
		for(FieldDescriptDALEx descript:descripts){
			if(descript.getXwsearchisvisible() == 1){
				AbstractFilterModel model = ExpandFilterFactory.initModel(context, descript);
				if(model!=null){
					filters.add(model);
				}
			}
		}

		container.removeAllViews();
		items.clear();
		for(IFilterModel model: filters){
			FilterLabelItemView item = new FilterLabelItemView(getContext(), eventBus,eventId);
			item.setModel(model);
			items.put(model.getFilterId(), item);
			container.addView(item);
		}

	}

}