package com.wty.ution.task;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.data.model.RegionInfoModelData;
import com.wty.ution.util.CommonUtil;

public class RegionInfoTask extends BaseBackgroundTask{
	// 该context为ApplicationContext,不能用于弹窗
	private boolean isSuccess = false;
	private String reason = "";
	
	public RegionInfoTask(Context context) {
	    super(context);
	}

	@Override
	protected String doInBackground(Object... params) {
	    String result = null;
		try {
			if (RegionInfoDALEx.get().queryAll().size() == 0) {
				long t = System.currentTimeMillis();
				String json = CommonUtil.loadJsonFile(context, "RegionInfo.json");
				Log.d("regionTask", "-------read json file!---------"+(System.currentTimeMillis()-t));
				Gson gson = new Gson();
				RegionInfoModelData regionInfoEntity = gson.fromJson(json,RegionInfoModelData.class);
				Log.d("regionTask", "-------read json file!---------"+(System.currentTimeMillis()-t));
				RegionInfoDALEx.get().save(regionInfoEntity.getResponse_params());
				Log.d("regionTask", "-------save!---------"+(System.currentTimeMillis()-t));
				
			}
			setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			setReason("解析地址区域信息失败");
			setSuccess(false);
		}
		return result;
	}

	@Override
	public void setAtomicity(boolean isAtomicity) {

	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

    public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
