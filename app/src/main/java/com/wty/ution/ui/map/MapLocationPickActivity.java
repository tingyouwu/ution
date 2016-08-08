package com.wty.ution.ui.map;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.wty.ution.ui.listener.baidu.OnLocationResultListener;

public class MapLocationPickActivity extends MapPickActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOnLocationResultListener(locationResultListener);
		doLocation();
	}
	
	OnLocationResultListener locationResultListener = new OnLocationResultListener() {
		
		private double latitude;
		private double longitude;
		
		@Override
		public void onLocationResult(BDLocation location) {
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
		}
		
		@Override
		public void onLocationFailed() {
			onToast(text_getLocationFailed);
		}
		
		@Override
		public void onAddressResult(String address) {
			showOverlay(latitude, longitude, address);
			MapLocationPickActivity.this.locationInfo = LocationInfo.get(latitude, longitude, address);
			getDefaultNavigation().getRightButton().show();
		}
		
		@Override
		public void onAddressFailed() {
			onToast(text_getLocationFailed);
		}

		@Override
		public void onLocationStart() {
//			setLocation("正在定位...", true);
			this.latitude = 0;
			this.longitude = 0;
		}
	};
	
}
