package com.wty.ution.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.util.FileUtils;
import com.wty.ution.widget.OtherToast;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.sample.HackyViewPager;

@SuppressLint("NewApi")
public class ImagePreviewActivity extends Activity implements OnClickListener{
	
	public static final String URIS = "uris";
	public static final String ISLOCAL = "isLocal";
	public static final String DEFAULTPOSITION = "defaultPosition";
	public static final String SAVEABLE = "saveAble";
	public static final String FULLSCREEN = "fullscreen";
	
	private HackyViewPager viewPager;

	private MyPagerAdapter awesomeAdapter;
	
	private String[] uris;
	private boolean isLocal;

	private int defaultPosition;
	private boolean fullscreen;
	private boolean saveAble;
	private ImageView img_save;
	private ImageLoader imageLoader;
	private LinearLayout backContainer;
	private TextView pageIndex;
	private PageChangeListener pageChangeListener;
	
	private CountDownTimer disMissTimer = new CountDownTimer(5000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			ImagePreviewActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					backContainer.animate().alpha(0).setDuration(400)
							.setListener(new AnimatorListenerAdapter() {
								public void onAnimationEnd(Animator animation) {
									backContainer.setVisibility(View.GONE);
								}
							});
				}
			});
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_prewview);
		
		imageLoader = ImageLoader.getInstance();
		uris = getIntent().getStringArrayExtra(URIS);
		isLocal = getIntent().getBooleanExtra(ISLOCAL, false);
		defaultPosition = getIntent().getIntExtra(DEFAULTPOSITION, 0);
		saveAble = getIntent().getBooleanExtra(SAVEABLE, true);
		fullscreen = getIntent().getBooleanExtra(FULLSCREEN, false);
		viewPager= (HackyViewPager) findViewById(R.id.image_prewview_viewpager);
		backContainer = (LinearLayout) findViewById(R.id.item_prewview_backcontainer);
		backContainer.setAlpha(0);
		backContainer.setOnClickListener(this);
		pageIndex = (TextView) findViewById(R.id.item_prewview_pageidex);
		pageIndex.setText(1+ "/" +uris.length);
		viewPager.setOffscreenPageLimit(1);
		
		
		if(uris!=null && uris.length!=0){
			awesomeAdapter = new MyPagerAdapter(this,uris,isLocal);
			viewPager.setAdapter(awesomeAdapter);
			viewPager.setCurrentItem(defaultPosition);
			pageChangeListener = new PageChangeListener(uris);
			viewPager.addOnPageChangeListener(pageChangeListener);
		}
		img_save = (ImageView) findViewById(R.id.item_prewview_save);
		img_save.setOnClickListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		backContainer.setVisibility(View.VISIBLE);
		backContainer.animate().alpha(1f).setDuration(400).setListener(null);
		disMissTimer.cancel();
		disMissTimer.start();
		return super.dispatchTouchEvent(ev);
	}

	private class MyPagerAdapter extends PagerAdapter {
		
		String[] uris ;
		Context context;
		boolean isLocal;
		public MyPagerAdapter(Context context,String[] uris,boolean isLocal) {
			this.context = context;
			this.uris = uris;
			this.isLocal = isLocal;
		}
		
		@Override
		public int getCount() {
			return uris.length;
		}

		@Override
		public Object instantiateItem(View collection, final int position) {
			View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_prewview, null);
			final PhotoView imageView = (PhotoView) view.findViewById(R.id.item_prewview_image);
			final LinearLayout layout_progress = (LinearLayout) view.findViewById(R.id.item_prewview_layoutprogress);
			final TextView tv_progress = (TextView) view.findViewById(R.id.item_prewview_tvprogress);
			img_save.setVisibility(View.GONE);
			
			if(fullscreen && !uris[position].startsWith("headimg")){
				imageView.setScaleType(ScaleType.FIT_CENTER);
			}
			
			tv_progress.setVisibility(View.GONE);
			ImageLoadingListener listener = new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_progress.setVisibility(View.VISIBLE);
							img_save.setVisibility(View.GONE);
						}
					});
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_progress.setVisibility(View.GONE);
							img_save.setVisibility(View.GONE);
							imageView.setImageResource(R.drawable.img_photoprew_failed);
						}
					});
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_progress.setVisibility(View.GONE);
							if(saveAble){
								img_save.setVisibility(View.VISIBLE);
							}else{
								img_save.setVisibility(View.GONE);
							}
						}
					});
					
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_progress.setVisibility(View.GONE);
							img_save.setVisibility(View.GONE);
						}
					});
					
				}
			};
			
			String url = uris[position];
			if(!TextUtils.isEmpty(url)){
				if(url.startsWith("headimg")){
					//默认头像
				}else if(!isLocal){
					//本地文件
					url = Scheme.DETAIL.wrap(url);
				}else{
					//本地路径
					url = Scheme.FILE.wrap(url);
				}
				
				imageLoader.displayImage(url, imageView,listener);
			}
			((ViewPager) collection).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
	
	class PageChangeListener implements OnPageChangeListener {
		
		private String[] uris;
		private int position;
		public PageChangeListener(String[] uris) {
			this.uris = uris;
		}
		
		@Override
		public void onPageSelected(int position) {
			this.position = position;
			pageIndex.setText(position + 1 + "/" + uris.length);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		
		public String getImageUri(){
			if(position >= uris.length){
				System.out.println("数组越界了");
				return "";
			}
			return uris[position];
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.item_prewview_save){
			String path = "";
			String fileName = "";
			
			String url = pageChangeListener.getImageUri();
			
			if(url.startsWith("headimg")){
				 String img = Scheme.HEADIMG.crop(url);
				 fileName = img;
				 saveAssetImage("headimg/"+img, fileName);
				 return;
				 
			}else if(!isLocal){
				url = Scheme.DETAIL.wrap(url);
				File file = imageLoader.getDiskCache().get(url);
				
				path = file .getPath();
				fileName = file.getName(); 
			}else{
				//本地路径
//				url = Scheme.FILE.wrap(url);
				
				String[] nameArray = url.split("/");
				
				path = url;
				fileName = nameArray[nameArray.length-1];
			}
			saveImage(path,fileName);
			
		} else
			ImagePreviewActivity.this.finish();
		
	}
	
	private void saveImage(final String imagePath,String fileName){
		if(TextUtils.isEmpty(imagePath) || TextUtils.isEmpty(fileName))return;
		String[] nArray = fileName.split("\\.");
		//文件带后缀名，把后缀名修改成png
		fileName = nArray[0]+".png";
		
		final String targetPath = AppContext.SAVEIMAGEPATH+"/"+fileName;
		
		new AsyncTask<String, Integer, Boolean>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			};
			
			@Override
			protected Boolean doInBackground(String... params) {
				File dir = new File(AppContext.PATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				dir = new File(AppContext.SAVEIMAGEPATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				File source = new File(imagePath);
				if(!source.exists()){
					return false;
				}
				
				FileUtils.Copy(source, targetPath);
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				File target = new File(targetPath);
				if(result && target.exists()){
                    new OtherToast(ImagePreviewActivity.this).show("图片已保存至相册");
					//刷新相册
					Uri localUri = Uri.fromFile(target);
					Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

					sendBroadcast(localIntent);
				}else{
                    new OtherToast(ImagePreviewActivity.this).show("保存图片失败");
				}
			}
		}.execute();
	}
	
	private void saveAssetImage(final String source,String fileName){
		final String targetPath = AppContext.SAVEIMAGEPATH+"/"+fileName;
		
		new AsyncTask<String, Integer, Boolean>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			};
			
			@Override
			protected Boolean doInBackground(String... params) {
				File dir = new File(AppContext.PATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				dir = new File(AppContext.SAVEIMAGEPATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				FileUtils.CopyAsset(AppContext.getContext(), source,targetPath);
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				File target = new File(targetPath);
				if(result && target.exists()){
                    new OtherToast(ImagePreviewActivity.this).show("图片已保存至相册");
					//刷新相册
					Uri localUri = Uri.fromFile(target);
					Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

					sendBroadcast(localIntent);
				}else{
                    new OtherToast(ImagePreviewActivity.this).show("保存图片失败");
				}
			}
		}.execute();
	}
	
}