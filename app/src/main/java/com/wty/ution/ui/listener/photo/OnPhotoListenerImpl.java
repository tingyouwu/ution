package com.wty.ution.ui.listener.photo;

/**
 * 功能描述：图片操作回调接口
 */
public class OnPhotoListenerImpl{
    String labelId;
    OnPhotoListener listener;
    public OnPhotoListenerImpl(String labelId,OnPhotoListener listener) {
        this.labelId = labelId;
        this.listener = listener;
    }

    public String getLabelId(){
        return this.labelId;
    }

    public OnPhotoListener getListener(){
        return listener;
    }
}
