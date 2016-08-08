package com.wty.ution.widget.fieldlabel.expandcontent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.RegionSelectActivity;
import com.wty.ution.widget.OnActivityResultListener;


public class ExpandContentRegion extends ExpandContentBase {
	public final static int FieldType = 11;
	private OnActivityResultListener activityResultListener;
	public ExpandContentRegion(Context context) {
		super(context);
		setClickMode(true);
		setViewByType(Type_Tree);
		setContentListener(regionClickListener);
		
		try {
			int requestCode = AppContext.Constant.ActivityResult_FieldLabel_RegionSelect;
			BaseActivity activity = (BaseActivity) getContext();
			activityResultListener = new OnActivityResultListener(activity,requestCode) {
				
				@Override
				public void onActivityResult(int requestCode, int resultCode, Intent data) {
					String regionid = UtionObjectCache.getInstance().getSelectedRegionid();
					String fieldname = UtionObjectCache.getInstance().getRegionFieldName();
					if(!TextUtils.isEmpty(regionid) && descript.getXwfieldname().equals(fieldname)){
						setFieldValue(regionid);
						UtionObjectCache.getInstance().cleanRegionCache();
					}
				}
			};
			activity.registerOnActivityResultListener(activityResultListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String makeMessagePrefix() {
		return "请选择"; 
	}
	
	OnClickListener regionClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Activity activity = (Activity) getContext();
            Intent i = new Intent(activity, RegionSelectActivity.class);
            int regionid = RegionInfoDALEx.defaultId;
            i.putExtra("regionid", regionid);
            i.putExtra("fieldname", descript.getXwfieldname());
            activityResultListener.startActivity(i);
        }
    };
	
}