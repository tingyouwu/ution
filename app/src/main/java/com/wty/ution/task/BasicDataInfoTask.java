package com.wty.ution.task;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wty.ution.data.dalex.ExpandinfoDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.model.BasicDataResponse;
import com.wty.ution.util.CommonUtil;

/**
 * 功能描述：获取基础数据
 **/
public class BasicDataInfoTask extends BaseBackgroundTask{
	// 该context为ApplicationContext,不能用于弹窗

	private boolean isSuccess = false;
	private String reason = "";

	public BasicDataInfoTask(Context context) {
	    super(context);
	}

	@Override
	protected String doInBackground(Object... params) {
	    String result = null;
		try {
			if (FieldDescriptDALEx.get().isTableEmpty()) {
				long t = System.currentTimeMillis();
				String json = CommonUtil.loadJsonFile(context, "basicdatainfo.json");
				Log.d("BasicDataInfoTask", "-------read json file!---------"+(System.currentTimeMillis()-t));
				Gson gson = new Gson();
				BasicDataResponse baseInfoEntity = gson.fromJson(json,BasicDataResponse.class);
				Log.d("regionTask", "-------read json file!---------"+(System.currentTimeMillis()-t));
				ExpandinfoDALEx.get().save(baseInfoEntity.response_params.getResponse_params().expandfieldinfo);
				Log.d("BasicDataInfoTask", "-------save!---------"+(System.currentTimeMillis()-t));
				
			}
			setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			setReason("解析基础数据失败");
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
