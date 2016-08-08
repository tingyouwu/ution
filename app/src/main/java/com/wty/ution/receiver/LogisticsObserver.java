package com.wty.ution.receiver;

import android.content.Context;
import android.content.Intent;

import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.ui.activity.logistics.LogisticsInfoActivity;

/**
 * 功能描述：跟物流有关的广播
 * @author wty
 **/
public class LogisticsObserver {

	/**
	 * 刷新物流列表
	 * @param context
	 */
	public static void notifyList(Context context){
        Intent i = new Intent(BroadcastConstants.REFRESH_LOGISLIST);
        context.sendBroadcast(i);
	}

    /**
     * 刷新物流统计数字
     * @param context
     */
    public static void notifyCount(Context context){
        Intent i = new Intent(BroadcastConstants.REFRESH_LOGISCOUNT);
        context.sendBroadcast(i);
    }

    /**
     * 刷新物流详情页面
     * @param context
     */
    public static void notifyInfo(Context context,LogisticsDALEx dalex){
        Intent i = new Intent(BroadcastConstants.REFRESH_LOGISINFO);
        i.putExtra(LogisticsInfoActivity.TAG,dalex);
        context.sendBroadcast(i);
    }
}