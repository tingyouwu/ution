package com.wty.ution.receiver;

import android.content.Context;
import android.content.Intent;

import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.ui.activity.glass.GlassInfoActivity;

/**
 * 功能描述：跟眼镜有关的广播
 * @author wty
 **/
public class GlassesObserver {

	/**
	 * 刷新眼镜列表
	 * @param context
	 */
	public static void notifyList(Context context){
        Intent i = new Intent(BroadcastConstants.REFRESH_GLASSES_LIST);
        context.sendBroadcast(i);
	}

    /**
     * 刷新眼镜统计数字
     * @param context
     */
    public static void notifyCount(Context context){
        Intent i = new Intent(BroadcastConstants.REFRESH_GLASSES_COUNT);
        context.sendBroadcast(i);
    }

    /**
     * 刷新眼镜详情页面
     * @param context
     */
    public static void notifyInfo(Context context,GlassesDALEx dalex){
        Intent i = new Intent(BroadcastConstants.REFRESH_GLASSES_INFO);
        i.putExtra(GlassInfoActivity.TAG,dalex);
        context.sendBroadcast(i);
    }
}