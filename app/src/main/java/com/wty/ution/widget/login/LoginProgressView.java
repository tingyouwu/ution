package com.wty.ution.widget.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wty.ution.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginProgressView extends LinearLayout{
	
	@Bind(R.id.login_progressbar)
	ProgressBar pb_progress;
	@Bind(R.id.login_progresstext)
	TextView tv_progress;
	
	private int progress = 0;
	
	public LoginProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoginProgressView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.layout_login_progress, this);
		ButterKnife.bind(this);
	}
	
	public void setProgress(int p){
		pb_progress.setProgress(p);
		this.progress = p;
	}
	
	public void addProgress(int p){
		this.progress = progress+p;
		pb_progress.setProgress(progress);
	}
	
	public void setText(String text){
		tv_progress.setText(text);
	}
	
}
