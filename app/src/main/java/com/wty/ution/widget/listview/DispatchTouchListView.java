package com.wty.ution.widget.listview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.wty.ution.R;

/**
 * @author wty
 * 继承于ListView，并设置了ListView分割线的高度（setDivider setDividerHeight）
 *主要重写了dispatchTouchEvent，重新决定事件的分发顺序,CRM没有使用到，可以看成和普通ListView没什么太大区别;
 **/
public class DispatchTouchListView extends ListView {
	
	private static final String namespace = "http://schemas.android.com/apk/res/android";
	
	private OnDispatchTouchListener dispatchTouchListener;
	
	public DispatchTouchListView(Context context) {
		super(context);
		initDivide();
	}
	public DispatchTouchListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String dividerHeight = attrs.getAttributeValue(namespace,"dividerHeight");
		String divider = attrs.getAttributeValue(namespace,"divider");
		if(dividerHeight==null || divider==null){
			initDivide();
		}
	}
	
	private void initDivide(){
		setDivider(new ColorDrawable(getResources().getColor(R.color.list_line)));
		setDividerHeight(1);  
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(this.dispatchTouchListener!=null){
			if(!dispatchTouchListener.dispatchTouchEvent(ev)){
				return super.dispatchTouchEvent(ev);
			}
			
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void setDispatchTouchListener(OnDispatchTouchListener dispatchTouchListener) {
		this.dispatchTouchListener = dispatchTouchListener;
	}


	public interface  OnDispatchTouchListener{
		public boolean dispatchTouchEvent(MotionEvent ev);
	}
}
