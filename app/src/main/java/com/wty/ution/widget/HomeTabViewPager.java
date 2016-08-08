package com.wty.ution.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写onInterceptTouchEvent方法
 * 实现禁止拖动功能
 * @author wty
 *
 */
public class HomeTabViewPager extends ViewPager {

	private boolean disableShuffle = true;

	public HomeTabViewPager(Context context) {
		super(context);
	}

	public HomeTabViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * true表示禁止拖动
	 * @param disableShuffle
	 */
	public void setDisableShuffle(boolean disableShuffle) {
		this.disableShuffle = disableShuffle;
	}
	
	
	@Override
	public boolean dispatchTrackballEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(disableShuffle) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(disableShuffle) {
			return false;
		}
		return super.onTouchEvent(ev);
	}
}
