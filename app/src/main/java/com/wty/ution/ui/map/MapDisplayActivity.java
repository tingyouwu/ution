package com.wty.ution.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapDisplayActivity extends MapBaseActivity implements OnGetGeoCoderResultListener {
	
//	double latitude;
//	double longitude;
//	String address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSearch.setOnGetGeoCodeResultListener(this);
		getParams();
		//隐藏提交按钮
		getDefaultNavigation().getRightButton().hide();
	}

	private void getParams(){
		Intent i = getIntent();
		String info = i.getStringExtra(Tag_LocationInfo);

		if(TextUtils.isEmpty(info))return;
		locationInfo =  LocationInfo.pares(info);
		if(locationInfo!=null && locationInfo.addressOnly){
			//只有地址。通过地址反编译坐标并显示在地图上
			mSearch.geocode(new GeoCodeOption().city("").address(locationInfo.address));
		}else{
			//地图移动到坐标点上，显示传入的地址在label上
			showOverlay(locationInfo.latitude, locationInfo.longitude, locationInfo.address);
		}

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			//没有检索到结果
			onToast(text_getAddressFailed);
			return;
		}
		LatLng location = result.getLocation();
        String address = result.getAddress();
		showOverlay(location.latitude, location.longitude, address);
		this.locationInfo = LocationInfo.get(location.latitude, location.longitude, result.getAddress());

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			//没有检索到结果
			onToast(text_getAddressFailed);
			return;
		}
		LatLng location = result.getLocation();
        String address = result.getAddress()+"("+result.getBusinessCircle()+")";
		showOverlay(location.latitude, location.longitude, address);
		this.locationInfo = LocationInfo.get(location.latitude, location.longitude, address);
	}
}