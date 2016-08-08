package com.wty.ution.task;

import android.content.Context;
import android.text.TextUtils;

import com.wty.ution.data.bmob.BmobLogisticsDALEx;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.listview.ListPage;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * 功能描述：登录应用后增量更新物流数据
 * @author wty
 **/
public class LogisticsListTask extends BaseBackgroundTask{
	private boolean isSuccess = false;
	private String reason = "";
	private ListPage page;
	private boolean isReturn = false;

	public LogisticsListTask(Context context) {
	    super(context);
		page = new ListPage(100);
		page.pageReset();
	}

	@Override
	protected String doInBackground(Object... params) {

		String result = null;
		try {
			String updatetime = (String) params[0];
			BmobQuery<BmobLogisticsDALEx> query = new BmobQuery<BmobLogisticsDALEx>();
			//还没有到最后一页
				query.setLimit(1000);
				if(!TextUtils.isEmpty(updatetime))//增量更新
					query.addWhereGreaterThanOrEqualTo("updateAt", new BmobDate(CommonUtil.getDate(updatetime)));
				query.findObjects(context, new FindListener<BmobLogisticsDALEx>() {
					@Override
					public void onSuccess(final List<BmobLogisticsDALEx> list) {
						setSuccess(true);
						if(list.size()!=0){
							BmobLogisticsDALEx.get().save(list);
						}
						isReturn = true;
					}

					@Override
					public void onError(int i, String s) {
						setReason("获取物流信息列表失败:" + s);
						setSuccess(false);
						isReturn = true;
					}
				});

		} catch (Exception e) {
			e.printStackTrace();
			setReason("获取物流信息列表失败");
			setSuccess(false);
		}
//		while(!isReturn);
		setSuccess(true);
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
