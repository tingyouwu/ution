package com.wty.ution.widget;

import android.content.Intent;
import com.wty.ution.ui.activity.BaseActivity;

public abstract class OnActivityResultListener {
	public int requestCode;
	private BaseActivity activity;
	public OnActivityResultListener(BaseActivity activity, int requestCode) {
		this.requestCode = requestCode;
		this.activity = activity;
	}
	
	public abstract void onActivityResult(int requestCode, int resultCode,Intent data);
	
	public void startActivity(Intent i) {
		activity.startActivityForResult(i, requestCode);
	}
}