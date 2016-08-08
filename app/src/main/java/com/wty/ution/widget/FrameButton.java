package com.wty.ution.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.wty.ution.R;


public class FrameButton extends Button {

    public FrameButton(Context context){
        super(context);
    }
    
	public FrameButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		onNormal();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onTab();
			break;
		case MotionEvent.ACTION_MOVE:
			onTab();
			break;
		case MotionEvent.ACTION_SCROLL:
			onTab();
			break;
		case MotionEvent.ACTION_UP:
			onNormal();
			break;
		default:
			onNormal();
			break;
		}
		return super.onTouchEvent(event);
	}
	
	
	public void onNormal(){
		this.setBackgroundResource(R.drawable.btn_frame_r5);
		this.setTextColor(this.getResources().getColor(R.color.blue_crm));
	}
	
	public void onTab(){
		this.setBackgroundResource(R.drawable.btn_frame_r5_press);
		this.setTextColor(this.getResources().getColor(R.color.white));
	}
}
