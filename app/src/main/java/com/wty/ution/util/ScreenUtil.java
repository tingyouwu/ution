package com.wty.ution.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {
	public static int SCREEN_WIDTH = 460;// 屏幕宽度

	public static int getSCREEN_WIDTH(Context context) {
		if (context != null && SCREEN_WIDTH == 460) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			Activity a = (Activity) context;
			a.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			SCREEN_WIDTH = displayMetrics.widthPixels;// 初始化屏幕宽度
		}
		return SCREEN_WIDTH;
	}
}
