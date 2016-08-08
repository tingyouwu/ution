package com.wty.ution.ui.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.wty.ution.R;
import com.wty.ution.ui.activity.MultiMediaActivity;

import SweetAlert.SweetAlertDialog;


public abstract class MapBaseActivity extends MultiMediaActivity {
	
	protected final String text_getAddress = "正在获取地址...";
	protected final String text_getAddressFailed = "地址无法显示";
	protected final String text_getLocationFailed = "定位失败";
	public final static String Tag_LocationInfo = "LocationInfo";
	public final static String Tag_LocationResult = "LocationResult";
	
	protected MapView mMapView;
	protected BaiduMap mBaiduMap;
	protected GeoCoder mSearch;
	
	protected LocationInfo locationInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_base);
		getDefaultNavigation().setTitle("地图");
        getDefaultNavigation().setRightButton("提交", new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (locationInfo != null) {
                    submit();
                }
            }
        });
		mMapView = (MapView)findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16));
		mSearch = GeoCoder.newInstance();
	}
	
	@Override
	protected boolean submit() {

        SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("确定选择当前位置?");
        dialog.setCancelText("取消");
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                Intent i = new Intent();
				i.putExtra(Tag_LocationResult, locationInfo.getResult());
				setResult(Activity.RESULT_OK,i);
				finish();
            }
        });
        dialog.show();
		return true;
	}

	/**
	 * 图钉设置在坐标点上，气泡上的文本显示输入的文本
	 * @param latitude
	 * @param longitude
	 * @param address
	 */
	public void showOverlay(double latitude, double longitude,String address){
		
		View view_overlay = LayoutInflater.from(this).inflate(R.layout.layout_map_popview, null);
		TextView tv = (TextView)view_overlay.findViewById(R.id.map_address);
		tv.setText(address);
		
		//定义Maker坐标点  
		LatLng point = new LatLng(latitude,longitude);
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view_overlay);
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		//在地图上添加Marker，并显示
		mBaiduMap.clear();
		mBaiduMap.addOverlay(option);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		mSearch.destroy();
	}

	
	public static  class LocationInfo{
		public double latitude;
		public double longitude;
		public String address;
		public boolean addressOnly = false;
		
		private LocationInfo() {
		}
		
		public static LocationInfo pares(String info){
			LocationInfo locationInfo = new LocationInfo();
			
			String[] infoArray = info.split(";");
			switch (infoArray.length) {
			case 1:
				//只有地址，或是空串
				locationInfo.address = info;
				locationInfo.addressOnly = true;
				break;
			case 3:
				
				if(TextUtils.isEmpty(infoArray[0]) && TextUtils.isEmpty(infoArray[1]) ){
					locationInfo.address = infoArray[2]; 
					locationInfo.addressOnly = true;
				}else{
					//什么都有
					locationInfo.latitude = Double.valueOf(infoArray[0]);
					locationInfo.longitude = Double.valueOf(infoArray[1]);
					locationInfo.address = infoArray[2]; 
					locationInfo.addressOnly = false;
				}
				
				break;
			default:
				break;
			}
			return locationInfo;
			
		}
		
		public static LocationInfo get(double latitude,double longitude,String address){
			LocationInfo locationInfo = new LocationInfo();
			locationInfo.latitude = latitude;
			locationInfo.longitude = longitude;
			locationInfo.address = address;
			return locationInfo;
		}
		
		public String getResult(){
			String[] array = {""+latitude,""+longitude,address};
			return TextUtils.join(";", array);
		}

        public boolean validate(){
            if(latitude == 4.9E-324 || longitude == 4.9E-324
            || latitude == 0.0 || longitude == 0.0){
                //非法数据
                return false;
            }else if(TextUtils.isEmpty(address)){
                //地址不能为空
                return false;
            }else{
                return true;
            }

        }

	}
	

}
