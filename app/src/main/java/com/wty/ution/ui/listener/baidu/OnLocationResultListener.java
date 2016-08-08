package com.wty.ution.ui.listener.baidu;

import com.baidu.location.BDLocation;

/**
 * 功能描述：百度定位结果回调接口
 */
public interface OnLocationResultListener {
    void onLocationStart();
    void onLocationResult(BDLocation location);
    void onLocationFailed();
    void onAddressResult(String address);
    void onAddressFailed();
}
