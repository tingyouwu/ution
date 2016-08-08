package com.wty.ution.task;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.ExecutorService;

public class SimpleTask extends AsyncTask<String, Integer, Object> {

    @Override
	protected Object doInBackground(String... params) {
		return null;
	}

    public void startTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//asynctask 在Api11后属于串行执行，需要重新修改线程池
            executeOnExecutor(TaskManager.getInstance().getParallelTaskPool());
        } else {
            execute();
        }
    }

	public void startTask(ExecutorService pool,String...params){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeOnExecutor(pool, params);
		} else {
			execute(params);
		}
	}

}
