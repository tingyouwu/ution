package com.wty.ution.ui.map;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wty.ution.widget.AppMsg;


public class MapPickActivity extends MapDisplayActivity{

	protected SuggestionSearch mSuggestionSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(locationInfo!=null){
			getDefaultNavigation().getRightButton().show();
		}
		AppMsg appMsg = AppMsg.makeText(this, "长按地图选择地点", AppMsg.STYLE_INFO);
		appMsg.show();
		// 初始化搜索模块，注册事件监听
		mBaiduMap.setOnMapLongClickListener(longClickListener);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			@Override
			public void onGetSuggestionResult(SuggestionResult suggestionResult) {
			}
		});
		// 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
		mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
				.keyword("")
				.city(""));
	}
	
	OnMapLongClickListener longClickListener = new OnMapLongClickListener() {
		
		@Override
		public void onMapLongClick(LatLng lat) {
            getDefaultNavigation().getRightButton().hide();
			showOverlay(lat.latitude, lat.longitude, text_getAddress);
			//根据坐标查找地址
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat.latitude, lat.longitude)));
		}
	};
	
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		super.onGetReverseGeoCodeResult(result);
        getDefaultNavigation().getRightButton().show();
	}
	
}
