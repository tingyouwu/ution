package com.wty.ution.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public abstract class BaseBackgroundTask extends AsyncTask<Object,Integer,String>{
	protected Context context;
	public BaseBackgroundTask(Context context) {
	    this.context = context;
	}
	
	//是否原子性任务
	private boolean isAtomicity = true;
	
	@Override
	protected String doInBackground(Object... params) {

		return null;
	}

	public void startTask(Context context) {
		startTask(context,null);
	}

	public void startTask(Context context, Object[] params) {
		this.context = context;
		if (params == null) {
			params = new Object[] {};
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeOnExecutor(TaskManager.getInstance().getParallelTaskPool(), params);
		} else {
			execute(params);
		}
	}

	public boolean isAtomicity() {
		return isAtomicity;
	}

	public void setAtomicity(boolean isAtomicity) {
		this.isAtomicity = isAtomicity;
	}

	
	@Override
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    
	}
	
	public void onSuccess(){}
	
	public void onFailed(String reason){}
}
