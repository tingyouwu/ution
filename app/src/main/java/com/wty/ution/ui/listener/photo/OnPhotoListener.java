package com.wty.ution.ui.listener.photo;

import android.graphics.Bitmap;

import com.wty.ution.data.dalex.FileDALEx;

/**
 * 功能描述：图片操作回调接口
 */
public interface OnPhotoListener {
    public void onCropCamera(Bitmap bitmap, FileDALEx fileDALEx);
    public void onCropAlbum(Bitmap bitmap, FileDALEx fileDALEx);
    public void onSelectedAlbum(String[] ids, String[] uris);
    public void onCamera(String pic_name, String uri);
}
