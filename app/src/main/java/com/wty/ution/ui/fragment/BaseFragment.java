package com.wty.ution.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.wty.ution.base.AppContext;
import com.wty.ution.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseFragment extends Fragment {

	protected ActionBar actionBar;

	protected SharedPreferences preferences;
	protected BaseActivity activity;

	private boolean init = false;

	public BaseFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (activity == null) {
			activity =  (BaseActivity) getActivity();
		}
		if (activity != null) {
			preferences = activity.getSharedPreferences(AppContext.PREFERENCES_CRM, Activity.MODE_APPEND);
			actionBar =activity.getSupportActionBar();
		}
		
	}
	
	public void initFragmentActionBar(){
	}

	public void setActivity(BaseActivity activity){
		this.activity = activity;
		this.actionBar = activity.getSupportActionBar();
	}
	
	private List<String> broadcastActions = new ArrayList<String>();
	public void registerBroadCast(String[] actions){
		broadcastActions.addAll(Arrays.asList(actions));
	}
	
	public List<String> getBroadCastList(){
		return broadcastActions;
	}
	
	public void onBroadCast(Intent intent){}
		
	public void onActivityResult(int requestCode, int resultCode,Intent intent){}
	
	public boolean onBackClick(){
		return true;
	}
	
	protected boolean isBackground = true;
	public void setIsBackground(boolean isBackground){
		this.isBackground = isBackground;
	}
	
	public boolean isBackground(){
		return isBackground;
	}
	
	public boolean isInit(){
		return init;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.init = true;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		this.init = false;
	}
}
