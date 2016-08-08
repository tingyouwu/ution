package com.wty.ution.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;

import com.wty.ution.album.AlbumIndexActivity;
import com.wty.ution.album.AlbumIndexItem;
import com.wty.ution.album.AlbumPhotoItem;
import com.wty.ution.base.AppContext;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.MultiMediaActivity;
import com.wty.ution.widget.MenuDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PhotoUtils {

    // 设置获取图片的字段?
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME, // 显示的名字
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE, // 经度
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.BUCKET_ID, // dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
    };

    public static String getPhotoPath(String pic_name) {
        return getBasePath() + "/" + pic_name + ".dat";
    }

    public static String getBasePath() {
        String lastAccount = AppContext.getInstance().getLastAccount();
        return AppContext.IMAGEPATH  +"/"+ lastAccount;
    }

    public static String getDetailPhoto(String url, int ex){
        
        String[] uArray = url.split("/");
        String filename = uArray[uArray.length-1];
        String host = AppContext.Api.API_FileHost;
        long ts = System.currentTimeMillis()/1000;
        String secretkey = AppContext.FileSecretKey;
        String baseUrl = String.format("/b/%s?ts=%d&ex=%d",new Object[]{filename,ts,ex});
        String si = MD5Util.stringToMD5(baseUrl + secretkey);
        return host+baseUrl+"&si="+si;
    }
    
    public static String getDetailPhoto(String url){
        
        String[] uArray = url.split("/");
        String filename = uArray[uArray.length-1];
        String host = AppContext.Api.API_FileHost;
        long ts = System.currentTimeMillis()/1000;
        int ex = 300;
        String secretkey = AppContext.FileSecretKey;
        String baseUrl = String.format("/b/%s?ts=%d&ex=%d",new Object[]{filename,ts,ex});
        String si = MD5Util.stringToMD5(baseUrl+secretkey);
        return host+baseUrl+"&si="+si;
    }

    public static String getThumPhoto(String url,int sc){
        String[] uArray = url.split("/");
        String filename = uArray[uArray.length-1];
        
        String host = AppContext.Api.API_FileHost;
        long ts = System.currentTimeMillis()/1000;
        int ex = 300;
        String secretkey = AppContext.FileSecretKey;
        String baseUrl = String.format("/bs/%s?ts=%d&sc=%d&ex=%d",new Object[]{filename,ts,sc,ex});
        String si = MD5Util.stringToMD5(baseUrl+secretkey);
        return host+baseUrl+"&si="+si;
    }
    

    /**
     * 通过文件名获取bitmap对象
     * 
     * @param path
     * @param REQUIRED_SIZE
     * @return
     */
    public static Bitmap decodeUriAsBitmap(String path, final int REQUIRED_SIZE) {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                return null;
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(f.getAbsolutePath(), o);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            o = null;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过文件名获取bitmap对象
     * 
     * @param path
     * @param REQUIRED_SIZE
     * @param isRound
     * @return
     */
    public static Bitmap decodeUriAsBitmapListThum(String path, int REQUIRED_SIZE,boolean isRound) {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                return null;
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(f.getAbsolutePath(), o);
            
            // Find the correct scale value. It should be the power of 2.

            // 原图片宽高
            int picWidth = o.outWidth, picHeight = o.outHeight;
            int temWidth = o.outWidth, temHeight = o.outHeight;
            int scale = 1;
            while (true) {
                if (temHeight / 2 < REQUIRED_SIZE || temWidth / 2 < REQUIRED_SIZE)
                    break;
                temWidth /= 2;
                temHeight /= 2;
                scale *= 2;
            }

            // 图片显示高宽
            float realHeight = 0, realWidth = 0;
            // 界面可显示的图片高宽最大、最小值
            float minHeight = 150, maxHeight = 250, minWidth = 150, maxWidth = 240;
            if (picHeight >= picWidth) {// 长图
                if (picHeight <= minHeight) {
                    realHeight = minHeight;
                    realWidth = picWidth * (minHeight / picHeight);
                } else if (picHeight <= maxHeight) {
                    realHeight = picHeight;
                    realWidth = picWidth;
                } else {
                    realHeight = maxHeight;
                    realWidth = picWidth * (maxHeight / picHeight);
                }

            } else {// 宽图
                if (picWidth <= minWidth) {
                    realWidth = minWidth;
                    realHeight = picHeight * (minWidth / picHeight);
                } else if (picWidth <= maxWidth) {
                    realWidth = picWidth;
                    realHeight = picHeight;
                } else {
                    realWidth = maxWidth;
                    realHeight = picHeight * (maxWidth / picWidth);
                }
            }

            o = null;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            if (picHeight < minHeight) {
                bitmap = big(bitmap, minHeight / picHeight);
            } else if (picWidth < minWidth) {
                bitmap = big(bitmap, minWidth / picWidth);
            }
            if(isRound){
            	return	getRoundedCornerBitmap(bitmap);
            }else{
            	return bitmap;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap sourceBitmap) {

        try {

            Bitmap targetBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
                    sourceBitmap.getHeight(), Config.RGB_565);

            // 得到画布
            Canvas canvas = new Canvas(targetBitmap);

            // 创建画笔
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            // 值越大角度越明显
            float roundPx = 5;
            float roundPy = 5;

            Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
            RectF rectF = new RectF(rect);

            // 绘制
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPy, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(sourceBitmap, rect, rect, paint);

            return targetBitmap;

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 保存bitmap到sd卡filePath文件中 如果有，则删除
     * 
     * @param bmp
     *            　bitmap
     * @param filePath
     *            图片名
     * @return
     */
    public static boolean saveBitmap2file(Bitmap bmp, String filePath) {
        if (bmp == null) {
            return false;
        }
        CompressFormat format = CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            stream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

    public static Bitmap big(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if(bitmap!=resizeBmp &&  bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
        return resizeBmp;
    }

    public static Bitmap bigNoRecycle(Bitmap bitmap, float scale) {
        // 不需要回收bitmap，等待ImageLoader自己回收
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
    
    public static String cropPhotoid(String photoName){
    	if(TextUtils.isEmpty(photoName))return "";
    	String[] pArray = photoName.split("\\.");
    	if(pArray.length == 1){
    		//没有后缀名
    		return photoName;
    	}else{
    		//有后缀名
    		return pArray[pArray.length-2];
    	}
    }
    
    public static String cropPhotoPath(String photoPath){
    	if(TextUtils.isEmpty(photoPath))return "";
    	String belongs = "file://";
		if (photoPath.startsWith(belongs)) {
			photoPath = photoPath.substring(belongs.length());
		}
		return photoPath;
    }

    /**
     * 根据原位图生成一个新的位图，并将原位图所占空间释放
     *
     * @param srcBmp 原位图
     * @return 新位图
     */
    public Bitmap copy(Bitmap srcBmp) {
        Bitmap destBmp = null;
        try {
            // 创建一个临时文件
            File file = new File(AppContext.PATH + "temppic/tmp.txt");
            if (file.exists()) {// 临时文件 ， 用一次删一次
                file.delete();
            }
            file.getParentFile().mkdirs();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, width * height * 4);
            // 将位图信息写进buffer
            srcBmp.copyPixelsToBuffer(map);
            // 释放原位图占用的空间
            srcBmp.recycle();
            // 创建一个新的位图
            destBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            map.position(0);
            // 从临时缓冲中拷贝位图信息
            destBmp.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();
            file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
            destBmp = null;
            return srcBmp;
        }
        return destBmp;
    }

    /**
     * 拍照，返回临时照片名字，不包含后缀名
     * @return tempImage
     * @param isCrop
     *          true 拍照后需要裁剪
     */
    public static String cropCameraPhoto(boolean isCrop,MultiMediaActivity activity) {
        int MaxLimit = 10;
        int retianMB = FileUtils.freeSpaceOnSd();
        if(retianMB<MaxLimit){
            activity.onToastErrorMsg("存储空间不足"+MaxLimit+"M，未能获取图片");
            return "";
        }
        String tempImage = null;
        try {
            tempImage = UUID.randomUUID().toString();
            File root = new File(AppContext.IMAGEPATH);
            if(!root.exists()){
                root.mkdir();
            }
            File directory = new File(PhotoUtils.getBasePath());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File picFile = new File(PhotoUtils.getPhotoPath(tempImage));
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            Uri photoUri = Uri.fromFile(picFile);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            if (android.os.Build.VERSION.SDK_INT > 13) {// 4.0以上
                cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);// 低质量
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if(isCrop){
                activity.startActivityForResult(cameraIntent,AppContext.Constant.ActivityResult_Camera_crop_with_data);
            }else{
                activity.startActivityForResult(cameraIntent,AppContext.Constant.ActivityResult_Camera_with_data);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    /**
     * 功能描述：从相册中选择图片
     * @param isCrop
     *          true 选择照片后裁剪
     * @param isMutiChoice
     *          true 是否是多选照片
     * @param uris
     *          已经选择的照片uri
     **/
    public static void selectedPhotoFromAlbum(MultiMediaActivity activity,boolean isMutiChoice,int limit,String[] uris,boolean isCrop){
        Intent i = new Intent(activity,AlbumIndexActivity.class);
        i.putExtra(AlbumIndexActivity.Tag_isMutiChoice,isMutiChoice);
        i.putExtra(AlbumIndexActivity.Tag_maxlimit,limit);
        i.putExtra(AlbumIndexActivity.Tag_uris, uris);
        if(isCrop){
            // 选择照片后裁剪
            activity.startActivityForResult(i, AppContext.Constant.ActivityResult_AlbumIndexCrop);
        }else{
            activity.startActivityForResult(i, AppContext.Constant.ActivityResult_AlbumIndex);
        }
    }

    /**
     * 功能描述：裁剪照片
     * @param uri 照片uri
     * @param outputX 裁剪后照片宽
     * @param outputY 裁剪后照片高
     * @param pic_name 照片名字
     **/
    public static void cropImageUri(MultiMediaActivity activity,Uri uri, int outputX, int outputY, int requestCode,String pic_name) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUtils.getPhotoPath(pic_name));
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scaleUpIfNeeded", "true");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 功能描述：拍照后裁剪图片
     * @param pic_name 图片名字
     **/
    public static void cropImageAfterCamera(MultiMediaActivity activity,String pic_name){
        Bitmap bitmap = PhotoUtils.decodeUriAsBitmap(PhotoUtils.getPhotoPath(pic_name), 200);
        int degree = PhotoUtils.readPictureDegree(PhotoUtils.getPhotoPath(pic_name));
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        // 旋转
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        PhotoUtils.saveBitmap2file(bitmap, PhotoUtils.getPhotoPath(pic_name));
        Uri uri = Uri.fromFile(new File(PhotoUtils.getPhotoPath(pic_name)));
        PhotoUtils.cropImageUri(activity, uri, 250, 250, AppContext.Constant.ActivityResult_Crop_Small_Picture, pic_name);
    }

    /**
     * 功能描述：拍照后直接返回
     * @param pic_name 图片名字
     **/
    public static void handleImageAfterCamera(String pic_name){
        Bitmap bitmap = PhotoUtils.decodeUriAsBitmap(PhotoUtils.getPhotoPath(pic_name), 400);
        int degree = PhotoUtils.readPictureDegree(PhotoUtils.getPhotoPath(pic_name));
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        // 旋转
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        PhotoUtils.saveBitmap2file(bitmap, PhotoUtils.getPhotoPath(pic_name));
    }

    /**
     * 方法描述：按相册获取图片信息
     * @author: wty
     * @param selectedUris 已经选择的照片
     */
    public static List<AlbumIndexItem> getPhotoAlbum(Context context,List<String> selectedUris) {

        List<AlbumIndexItem> indexList = new ArrayList<AlbumIndexItem>();
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, MediaStore.Images.Media._ID + " desc ");
        Map<String, AlbumIndexItem> countMap = new HashMap<String, AlbumIndexItem>();
        AlbumIndexItem pa;
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String path = cursor.getString(1);
            String id = cursor.getString(3);
            String dir_id = cursor.getString(4);
            String dir = cursor.getString(5);

            boolean isSelected = selectedUris.contains(path);

            if (!countMap.containsKey(dir_id)) {
                pa = new AlbumIndexItem();
                pa.setName(dir);
                pa.setDir_id(dir_id);
                pa.setBitmap(path);
                pa.setCount("1");
                pa.getBitList().add(new AlbumPhotoItem(Integer.valueOf(id),name, path,isSelected));
                if(isSelected){
                    pa.setSelected(true);
                }
                countMap.put(dir_id, pa);
            } else {
                pa = countMap.get(dir_id);
                pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
                pa.getBitList().add(new AlbumPhotoItem(Integer.valueOf(id),name, path,isSelected));
            }
        }

        if(cursor != null && !cursor.isClosed())
            cursor.close();

        Iterable<String> it = countMap.keySet();
        for (String key : it) {
            indexList.add(countMap.get(key));
        }
        return indexList;
    }
}