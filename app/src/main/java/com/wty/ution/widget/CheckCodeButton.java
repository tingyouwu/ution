package com.wty.ution.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wty.ution.R;


public class CheckCodeButton extends LinearLayout {

	Button button;
	public CheckCodeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.layout_btn_checkcode, this);
		button = (Button)findViewById(R.id.btn_checkcode);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		button.setOnClickListener(l);
	}
	
	public void startTimer(){
		codeTimer.start();
	}
	
	public void stopTimer(){
		codeTimer.stop();
	}
	
	int TIMESTAMP = 1000;
	private CodeTimer codeTimer = new CodeTimer();

	public class CodeTimer {
		boolean isRunning = false;

		CountDownTimer mTimer = new CountDownTimer(60 * TIMESTAMP, TIMESTAMP) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				Log.d("CountDownTimer", "remain time = " + millisUntilFinished);
				button.setText("   " + millisUntilFinished / TIMESTAMP + "秒...   ");
				button.setClickable(false);
			}
			
			@Override
			public void onFinish() {
				isRunning = false;
				Log.d("CountDownTimer", "onFinish");
				button.setClickable(true);
				button.setText("获取验证码");
			}
		};
		

		public void start() {
			if (!isRunning) {

				isRunning = true;
				mTimer.start();
			}
		}

		public void stop() {
			if (isRunning) {
				Log.d("CountDownTimer", "stop");
				if (mTimer != null) {
					mTimer.cancel();
					mTimer = null;
				}
				isRunning = false;
			}
		}

	}

}
