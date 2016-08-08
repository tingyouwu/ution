package com.wty.ution.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.wty.ution.location.LocationService;
import com.wty.ution.task.TaskManager;

import cn.bmob.v3.Bmob;


public class UtionApplication extends Application {

	public static boolean isDebug = false;

    public LocationService locationService;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("CrmApplication Debug", "Application OnCreate");
        AppContext.setContext(getApplicationContext());

		//检查数据库升级
		String lastAccount = AppContext.getInstance().getLastAccount();
		if(!TextUtils.isEmpty(lastAccount)){
			UtionObjectCache.getInstance().getEtionDbFromUserAccunt();
		}
		
		initImageLoader(this.getApplicationContext());
		TaskManager.getInstance();

        locationService = new LocationService(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

		//初始化Bmob功能
		Bmob.initialize(this,AppContext.Bmob_ApplicationId);
	}

    public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
    	DisplayImageOptions options = new DisplayImageOptions.Builder()
//        .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//        .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//        .showImageOnFail(R.drawable.ic_error) // resource or drawable
//        .resetViewBeforeLoading(false)  // default
//        .delayBeforeLoading(1000)
        .cacheInMemory(true) // default
        .cacheOnDisk(true) // default
//        .preProcessor(...)
//        .postProcessor(...)
//        .extraForDownloader(...)
//        .considerExifParams(false) // default
//        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.RGB_565) // default
//        .decodingOptions(...)
//        .displayer(new SimpleBitmapDisplayer()) // default
//        .handler(new Handler()) // default
        .build();
    	
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(options)
				.discCacheFileCount(100) //缓存的文件数量
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//				.writeDebugLogs() // Remove for release app
				.build();
		
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
