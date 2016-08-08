package com.wty.ution.widget.listview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ScrollListViewAdapter extends BaseAdapter{
	private boolean isBusy = false;
	
	public synchronized void  setBusy(boolean isBusy){
		this.isBusy=isBusy;
	}
	
	public synchronized boolean isBusy(){
		return isBusy;
	}
	
	@Override
	public int getCount() {
		return 0;
	}


	@Override
	public Object getItem(int arg0) {
		return null;
	}


	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
	}

	
	public void loadVisableImage(int firstVisPosition,int lastVisPosition){
		
	}

}
