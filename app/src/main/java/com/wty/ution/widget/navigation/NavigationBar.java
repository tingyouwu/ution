package com.wty.ution.widget.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wty.ution.R;
import com.wty.ution.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航控件
 * @author 姚珺
 *
 */
public class NavigationBar extends HorizontalScrollView implements OnClickListener{

	private List<NavigateAble> itemList;
	private OnNavigationListener onNavigationListener;
	
	private int color;
	private int textSize;
	private LayoutParams textParams, arrowParams;
	private LinearLayout itemContainer;
	private int dividerLength;
	
	public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(getContext()).inflate(R.layout.layout_navigationbar, this);
		itemContainer = (LinearLayout)findViewById(R.id.item_container);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar, 0, 0);
		color = ta.getInteger(R.styleable.NavigationBar_NavigationTextColor, context.getResources().getColor(R.color.blue_crm));
		textSize = ta.getInteger(R.styleable.NavigationBar_NavigationTextSize, 13);
		ta.recycle();
		
		dividerLength = CommonUtil.dip2px(getContext(), 8);
		textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		textParams.setMargins(CommonUtil.dip2px(getContext(), 8), 0, 0, 0);
		
		arrowParams = new LayoutParams(CommonUtil.dip2px(getContext(), 14), CommonUtil.dip2px(getContext(), 14));
//		arrowParams.setMargins(CommonUtil.dip2px(getContext(), 8), 0, 0, 0);
	}
	
	private void inflteItem(NavigateAble item) {
		TextView label = new TextView(getContext());
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		label.setTextColor(color);
		label.setLayoutParams(textParams);
		label.setOnClickListener(this);
		label.setText(item.getNavigateLabel());
		label.setTag(item);
		label.setPadding(dividerLength, dividerLength+5, dividerLength, dividerLength+5);

		if(item == itemList.get(0)) {
			itemContainer.addView(label);
		} else {
			ImageView arrow = new ImageView(getContext());
			arrow.setLayoutParams(arrowParams);
//			arrow.setPadding(dividerLength, 0, 0, 0);
			arrow.setImageResource(R.drawable.navigationbar_arrow);
			itemContainer.addView(arrow);
			itemContainer.addView(label);
		}
		doScroll();
	}

	private void doScroll() {
		this.post(new Runnable() {

			@Override
			public void run() {
				NavigationBar.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}
		});
	}
	
	public void addNavigationItem(NavigateAble item) {
		if(itemList == null) 
			itemList = new ArrayList<NavigateAble>();
		itemList.add(item);
		inflteItem(item);
	}
	
	public NavigateAble getNavigationItemAtPosition(int position) {
		return itemList.get(position);
	}
	
	public OnNavigationListener getOnNavigationListener() {
		return onNavigationListener;
	}

	public void setOnNavigationListener(OnNavigationListener onNavigationListener) {
		this.onNavigationListener = onNavigationListener;
	}

	public interface OnNavigationListener {
		void onNavigationItemSelect(NavigateAble item);
	}

	@Override
	public void onClick(View v) {
		NavigateAble item = (NavigateAble)v.getTag();
		
		if (itemContainer.indexOfChild(v) != itemContainer.getChildCount() - 1) {
			int position = itemContainer.indexOfChild(v);
			for (int i = itemContainer.getChildCount() - 1; i > position; i--)
				itemContainer.removeViewAt(i);

			doScroll();
			if (onNavigationListener != null)
				onNavigationListener.onNavigationItemSelect(item);
		}
	}
}
