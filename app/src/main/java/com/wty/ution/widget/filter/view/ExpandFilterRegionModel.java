package com.wty.ution.widget.filter.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.RegionInfoActivity;
import com.wty.ution.ui.activity.RegionSelectActivity;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterOptionModel;


public class ExpandFilterRegionModel extends AbstractFilterModel {

	private static final long serialVersionUID = 1L;
	private SelectCallback callback;
	private OnActivityResultListener activityResultListener;
	public ExpandFilterRegionModel(Context context, FieldDescriptDALEx descript) {
		super(context,descript, InputMode.Select);
		
		int requestCode = AppContext.Constant.ActivityResult_FieldLabel_RegionSelect;
		BaseActivity activity = (BaseActivity) context;
		activityResultListener = new OnActivityResultListener(activity,requestCode) {
			
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {
				try {
					String regionid = UtionObjectCache.getInstance().getSelectedRegionid();
					String fieldname = UtionObjectCache.getInstance().getRegionFieldName();
					
					if(!TextUtils.isEmpty(regionid) && callback!=null){
						RegionInfoDALEx region = RegionInfoDALEx.get().queryById(Integer.valueOf(regionid));
						String regionName = region.getNamepath();
						callback.onResult(new FilterOptionModel(regionName, regionid));
						callback = null;
						UtionObjectCache.getInstance().cleanRegionCache();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		activity.registerOnActivityResultListener(activityResultListener);
		
	}
	
	@Override
	public void onSelect(SelectCallback callback) {
		super.onSelect(callback);
		this.callback = callback;
		
		Activity activity = (Activity) context;
		Intent i = new Intent(activity, RegionSelectActivity.class);
        int regionid = RegionInfoDALEx.defaultId;
        i.putExtra("title", label);
        i.putExtra("regionid", regionid);
        activityResultListener.startActivity(i);
		
	}

	@Override
	public String toSql() {
		if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(filterid)){
			return "";
		}else{
			return filterid+" = '"+value+"'";
		}
	}
}