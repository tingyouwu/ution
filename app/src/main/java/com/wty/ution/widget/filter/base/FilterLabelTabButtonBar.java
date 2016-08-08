package com.wty.ution.widget.filter.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wty.ution.R;

/**
 * 功能描述：过滤button布局
 **/
public class FilterLabelTabButtonBar extends LinearLayout{

	private LinearLayout root;

	public FilterLabelTabButtonBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_filterlabel_tabbar, this);
		root = (LinearLayout)findViewById(R.id.filterlabel_tabbar_layout);
		root.removeAllViews();
	}

	public void addButton(FilterLabelButton button){

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,1);
		root.addView(button,params);
		root.addView(getLine());
		
	}

	/**
	 * 功能描述：添加一条竖线
	 **/
	private View getLine(){
		View line = new View(getContext());
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1,ViewGroup.LayoutParams.MATCH_PARENT);
		line.setBackgroundResource(R.color.list_line);
		line.setLayoutParams(params);
		return line;
	}
	
}