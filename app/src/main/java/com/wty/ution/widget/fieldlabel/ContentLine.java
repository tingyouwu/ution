package com.wty.ution.widget.fieldlabel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wty.ution.R;

public class ContentLine extends LinearLayout{

	public ContentLine(Context context) {
		super(context);
		init(context);
	}

	public ContentLine(Context contxt,AttributeSet attr){
		super(contxt,attr);
		init(contxt);
	}
	
	private void init(Context context){
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//		params.height = CommonUtil.dip2px(context, 1); 
		params.height = 1;
		
		View view = new View(context);
		view.setBackgroundResource(R.color.list_line);
		view.setLayoutParams(params);
		this.addView(view);
	}
	
}
