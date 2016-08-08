package com.wty.ution.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.wty.ution.album.AlbumIndexActivity;
import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionApplication;
import com.wty.ution.data.dalex.FileDALEx;
import com.wty.ution.location.LocationService;
import com.wty.ution.ui.listener.baidu.OnGrandListener;
import com.wty.ution.ui.listener.baidu.OnLocationResultListener;
import com.wty.ution.ui.listener.baidu.OnMapResultListener;
import com.wty.ution.ui.listener.photo.OnPhotoListener;
import com.wty.ution.ui.listener.photo.OnPhotoListenerImpl;
import com.wty.ution.ui.map.MapBaseActivity;
import com.wty.ution.util.PhotoUtils;
import com.wty.ution.widget.MenuDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author wty
 * 多功能activity
 */
public class MultiMediaActivity extends BaseActivity{

    private final int SDK_PERMISSION_REQUEST = 127;
    //临时照片文件名
    private String pic_name = "";
    protected FileDALEx fileDALEx;
    private String permissionInfo;
    private LocationService locationService;
    private double latitude;
    private double longitute;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        if(paramBundle!=null){

            pic_name = paramBundle.getString("pic_name");
            fileDALEx = (FileDALEx) paramBundle.getSerializable("fileDALEx");
            if (AppContext.getInstance().getLastAccount() == null)
                System.out.println("lastAccount为空");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /**
             *  拍照返回，跳转至裁剪
             **/
            case AppContext.Constant.ActivityResult_Camera_crop_with_data: // 拍照回来后裁剪
                try {
                    Thread.sleep(100);
                    if (resultCode == RESULT_OK) {
                        PhotoUtils.cropImageAfterCamera(this,pic_name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            /**
             * 裁剪返回
             **/
            case AppContext.Constant.ActivityResult_Crop_Small_Picture:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if(PhotoUtils.saveBitmap2file(bitmap, PhotoUtils.getPhotoPath(pic_name))){
                            if(listener!=null){
                                fileDALEx = FileDALEx.get().createFileDALEx(pic_name, PhotoUtils.getPhotoPath(pic_name));
                                listener.onCropCamera(bitmap,fileDALEx);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            /**
             *  拍照直接返回
             **/
            case AppContext.Constant.ActivityResult_Camera_with_data: // 拍照回来
                try {
                    if (resultCode == RESULT_OK) {
                        PhotoUtils.handleImageAfterCamera(pic_name);
                        if(listener!=null){
                            listener.onCamera(pic_name+".dat",PhotoUtils.getPhotoPath(pic_name));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            /**
             * 相册选择返回
             **/
            case AppContext.Constant.ActivityResult_AlbumIndex:
                if(resultCode == RESULT_OK){
                    try {
                        String[] ids = data.getStringArrayExtra(AlbumIndexActivity.Tag_ids);
                        String[] uris = data.getStringArrayExtra(AlbumIndexActivity.Tag_uris);
                        String labelId = data.getStringExtra(AlbumIndexActivity.Tag_labelId);
                        if(listener!=null){
                            listener.onSelectedAlbum(ids,uris);
                        }

                        for(OnPhotoListenerImpl photpImpl:photoListenerImpls){
                            if(photpImpl.getLabelId().equals(labelId)){
                                photpImpl.getListener().onSelectedAlbum(ids,uris);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            /**
             * 相册返回，跳转至裁剪
             **/
            case AppContext.Constant.ActivityResult_AlbumIndexCrop:
                if(resultCode == RESULT_OK){
                    try {
                        String[] uris = data.getStringArrayExtra("uris");
                        Uri uri = Uri.fromFile(new File(uris[0]));
                        pic_name = UUID.randomUUID().toString();
                        PhotoUtils.cropImageUri(this, uri, 250, 250, AppContext.Constant.ActivityResult_Crop_Small_Picture, pic_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            /**
             * 地图选择返回
             * */
            case AppContext.Constant.ActivityResult_GotoMapview:
                if (resultCode == RESULT_OK) {

                    String messageFormat = data.getStringExtra(MapBaseActivity.Tag_LocationResult);
                    MapBaseActivity.LocationInfo info = MapBaseActivity.LocationInfo.pares(messageFormat);
                    //进入地图选择点后返回
                    if(info!=null){
                        for(OnMapResultListener listener:mapListener){
                            listener.onMapViewResult(info.latitude, info.longitude, info.address, messageFormat);
                        }

                    }
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        if (locationService != null){
            locationService.stop();
            locationService.unregisterListener(bdLocationListener);
        }
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState");
        pic_name = savedInstanceState.getString("pic_name");
        fileDALEx = (FileDALEx) savedInstanceState.getSerializable("fileDALEx");

        if (AppContext.getInstance().getLastAccount() == null)
            System.out.println("lastAccount为空");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pic_name", pic_name);
        if(fileDALEx!=null){
            outState.putSerializable("fileDALEx", fileDALEx);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private List<String> getPersimmions() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "ACCESS_FINE_LOCATION 未授权", Toast.LENGTH_SHORT).show();
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                Toast.makeText(this,"ACCESS_COARSE_LOCATION 未授权",Toast.LENGTH_SHORT).show();
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
        }
        return permissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    private OnGrandListener onGrandListener;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int grant:grantResults){
            if (grant == PackageManager.PERMISSION_GRANTED) {
                if(onGrandListener!=null)onGrandListener.onSuccess();
                // Permission Granted
            } else {
                if(onGrandListener!=null)onGrandListener.onFailed();
                // Permission Denied
            }
        }
    }

    boolean isLocationSuccess = false;
    private OnLocationResultListener onLocationResultListener;
    public void setOnLocationResultListener(OnLocationResultListener onLocationResultListener){
        this.onLocationResultListener = onLocationResultListener;

    }

    /** 定位SDK */
    @TargetApi(Build.VERSION_CODES.M)
    public void doLocation() {
        List<String> permissions = getPersimmions();
        if (permissions.size() > 0) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            onGrandListener = new OnGrandListener() {
                @Override
                public void onSuccess() {
                    doLocation();
                }

                @Override
                public void onFailed() {
                    if(onLocationResultListener!=null){
                        onLocationResultListener.onLocationFailed();
                    }
                    if(locationService!=null){
                        locationService.stop();
                    }
                    onGrandListener = null;
                }
            };
            return;
        }


        try {
            if(locationService==null){
                locationService = ((UtionApplication)getApplication()).locationService;
            }
            locationService.setLocationOption(initLocationOption());
            locationService.registerListener(bdLocationListener);

            locationService.stop();
            Thread.sleep(200);

            isLocationSuccess = false;
            countDownTimer.cancel();
            countDownTimer.start();

            locationService.start();

            if(onLocationResultListener!=null){
                onLocationResultListener.onLocationStart();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CountDownTimer countDownTimer = new CountDownTimer(60*1000,60*1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            //一分钟后自动停止定位
            if(locationService!=null){
                locationService.stop();
                if(!isLocationSuccess && onLocationResultListener!=null){
                    onLocationResultListener.onLocationFailed();
                }
            }
        }
    };

    private LocationClientOption initLocationOption(){
        if(locationService==null)locationService = ((UtionApplication)getApplication()).locationService;
        LocationClientOption bdOptions = locationService.getDefaultLocationClientOption();
        return bdOptions;
    }

    private List<OnMapResultListener> mapListener = new ArrayList<OnMapResultListener>();
    public void setOnMapViewResult(OnMapResultListener mapListener){
        this.mapListener.add(mapListener);
    }

    /**
     * 功能描述：百度定位结果回调
     **/
    private BDLocationListener bdLocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                if(onLocationResultListener!=null && !isLocationSuccess){
                    onLocationResultListener.onLocationFailed();
                    locationService.stop();
                }
                return;
            }
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                Toast.makeText(MultiMediaActivity.this,"gps定位成功",Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                // 运营商信息
//                Toast.makeText(MultiMediaActivity.this,"网络定位成功",Toast.LENGTH_SHORT).show();
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                onToast(new OnDismissCallbackListener("离线定位返回的结果",SweetAlertDialog.WARNING_TYPE));
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                onToast(new OnDismissCallbackListener("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因",SweetAlertDialog.WARNING_TYPE));
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                onToast(new OnDismissCallbackListener("网络不同导致定位失败，请检查网络是否通畅",SweetAlertDialog.WARNING_TYPE));
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                onToast(new OnDismissCallbackListener("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",SweetAlertDialog.WARNING_TYPE));
//            } else if (location.getLocType() == BDLocation.TypeCacheLocation) {
//                onToast(new OnDismissCallbackListener("缓存返回结果",SweetAlertDialog.WARNING_TYPE));
//            }

            latitude = location.getLatitude();
            longitute = location.getLongitude();
            if (latitude == 4.9E-324 || longitute == 4.9E-324 || latitude == 0.0 || longitute == 0.0) {// 过滤非法数据
                if(onLocationResultListener!=null && !isLocationSuccess ){
                    onLocationResultListener.onLocationFailed();
                    locationService.stop();
                }
                return;
            }
            //定位成功；
            if(onLocationResultListener!=null && !isLocationSuccess){
                onLocationResultListener.onLocationResult(location);
            }

            if (!TextUtils.isEmpty(location.getAddrStr())) {
                if(onLocationResultListener!=null){
                    onLocationResultListener.onAddressResult(location.getAddrStr()+"("+ location.getLocationDescribe() +")");
                    isLocationSuccess = true;
                }
            } else {
//				/** 定位成功，但没有返回地址，根据经纬度再次查询位置 */
//				GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
//				mSearch.reverseGeocode(geoPoint);
                //没有地址返回
                if(onLocationResultListener!=null && !isLocationSuccess){
                    onLocationResultListener.onAddressResult("获取地址失败，请检查网络");
                    locationService.stop();
                }
            }

        }
    };

    private OnPhotoListener listener;
    public void setOnPhotoListener(OnPhotoListener listener){
        this.listener=listener;
    }

    List<OnPhotoListenerImpl> photoListenerImpls = new ArrayList<OnPhotoListenerImpl>();
    public void setOnPhotoListener(OnPhotoListener listener,String labelId){
        this.photoListenerImpls.add(new OnPhotoListenerImpl(labelId,listener));
    }

    /**
     * 功能描述：通过相机拍照，拍照完是否需要裁剪
     * @param isCrop
     *         true 需要裁剪
     **/
    public void cameraPhoto(boolean isCrop){
        this.pic_name = PhotoUtils.cropCameraPhoto(isCrop,this);
    }

    /**
     * 弹出选择拍照或选择照片裁剪框
     */
    public void doPickPhotoAction(final boolean isCrop,final boolean isMutiChoice,final String[] uris,final int limit,final String labelId) {
        final Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
        String[] choices= new String[]{"拍照","从相册选择"};
        MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
        MenuDialog dialog = builder.setTitle("选择")
                .setItems(choices, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0://拍照
                                try {

                                    String status = Environment.getExternalStorageState();
                                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                                        // 拍照
                                        pic_name = PhotoUtils.cropCameraPhoto(isCrop,MultiMediaActivity.this);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    pic_name = PhotoUtils.cropCameraPhoto(isCrop,MultiMediaActivity.this);
                                }

                                break;
                            case 1://从相册中选择
                                PhotoUtils.selectedPhotoFromAlbum(MultiMediaActivity.this,isMutiChoice, limit, uris, isCrop);
                                break;
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}