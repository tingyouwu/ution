package com.wty.ution.ui.listener.baidu;

/**
 * 功能描述：百度地图选择位置回调接口
 */
public interface OnMapResultListener {
    void onMapViewResult(double latitude, double longitude, String address, String messageFormat);
}
