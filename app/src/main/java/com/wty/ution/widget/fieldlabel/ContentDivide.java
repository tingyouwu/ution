package com.wty.ution.widget.fieldlabel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;


public class ContentDivide extends LinearLayout{
	public final static int FieldType = 10;
	public TextView tv;

	public ContentDivide(Context context) {
		super(context);
		init(context);
	}

	public ContentDivide(Context context,AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundResource(R.drawable.bg_alpha);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		View view = new View(context);
		view.setLayoutParams(params);

        tv = new TextView(context);
        tv.setTextColor(getResources().getColor(R.color.gray_font_1));
        tv.setTextSize(14);
        tv.setPadding(20,10,0,10);

		addView(view);
        addView(tv);
	}
}
