package com.wty.ution.widget.titlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;


public class TitleMenuLabel extends LinearLayout{
	ImageView img_nav;
	TextView tv_title;

	public TitleMenuLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_titlemenu_label, this);
		
		img_nav = (ImageView)findViewById(R.id.titlemenu_label_nav);
		tv_title = (TextView)findViewById(R.id.titlemenu_label_title);

	}

	public void setTitle(String title){
		tv_title.setText(title);
	}

	public void onShow(){
		img_nav.setImageResource(R.drawable.img_nav_white_up);
	}
	
	public void onNormal(){
		img_nav.setImageResource(R.drawable.img_nav_white_down);
	}
}