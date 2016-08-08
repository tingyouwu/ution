package com.wty.ution.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.wty.ution.R;
import com.wty.ution.ui.activity.ImagePreviewActivity;
import com.wty.ution.widget.listview.ScrollListViewAdapter;


public class AlbumChoiceAdapter extends ScrollListViewAdapter {
	private Context context;
	
	private AlbumIndexItem album;
	//是否支持多选
	private boolean isMutipleChoice = false;
	//多选框选中事件
	private OnCheckBoxSelectedListener onCheckBoxSelectedListener;

	private GridView gridView;
	
	ImageLoader imageloader;
	DisplayImageOptions options;

	public AlbumChoiceAdapter(Context context, AlbumIndexItem album, boolean isMutipleChoice,GridView gridView) {
		this.context = context;
		this.album = album;
		this.isMutipleChoice = isMutipleChoice;
		imageloader = ImageLoader.getInstance();
		this.gridView = gridView;
		this.gridView.setOnScrollListener(new PauseOnScrollListener(imageloader,true,true));
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true) // default
				.cacheOnDisk(false) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				.build();
	}

	@Override
	public int getCount() {
	    return album.getBitList().size();
	}

	@Override
	public AlbumPhotoItem getItem(int position) {
	    return album.getBitList().get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_album_choice,null);
			holder = new ViewHolder();
			holder.init(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		AlbumPhotoItem photoItem = getItem(position);
		holder.setValue(view, photoItem, position);
		return view;
	}

	class ViewHolder {

		ImageView  iv_photo;  //  照片
		CheckBox cb_selected; //  多选控件
		LinearLayout cb_layout;// 多选框加大点击区域
		public void init(View view) {
			this.iv_photo = (ImageView) view.findViewById(R.id.item_albumselected_photo);
			this.cb_selected = (CheckBox) view.findViewById(R.id.item_albumselected_cb);
			this.cb_layout = (LinearLayout) view.findViewById(R.id.item_albumselected_cblayout);
			
		}

		public void setValue(View view, final AlbumPhotoItem photoItem,final int position) {
		    
		    iv_photo.setImageResource(R.drawable.bg_gray_rd);
			imageloader.displayImage(Scheme.FILE.wrap(photoItem.getPath()), iv_photo,options);
		    
			if (isMutipleChoice) {
			    cb_selected.setVisibility(View.VISIBLE);
			    cb_layout.setVisibility(View.VISIBLE);
			    cb_selected.setChecked(photoItem.isSelect());
				cb_selected.setClickable(false);
				cb_layout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
					    
					    boolean isCheck = !cb_selected.isChecked();
					    
					    if(onCheckBoxSelectedListener!=null){
                            if(onCheckBoxSelectedListener.validate(isCheck)){
                                onCheckBoxSelectedListener.onSelected(position, isCheck);
                                //通过条件
                                cb_selected.setChecked(isCheck);
                                photoItem.setSelect(isCheck);
                            }
                        }else{
                            //没有限制
                            cb_selected.setChecked(isCheck);
                            photoItem.setSelect(isCheck);
                        }
						
						
					}
				});
				view.setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View arg0) {

                        Intent intent = new Intent();
            			intent.setClass(context, ImagePreviewActivity.class);
            			intent.putExtra("defaultPosition", 0);
            			intent.putExtra("uris", new String[]{photoItem.getPath()});
            			intent.putExtra("isLocal", true);
            			intent.putExtra("saveAble", false);
            			context.startActivity(intent);
                        
                    }
                });
			} else {
			    cb_selected.setVisibility(View.GONE);
			    cb_layout.setVisibility(View.GONE);
			}

		}
	}

	public void setOnCheckBoxSelectedListener(OnCheckBoxSelectedListener listener){
		this.onCheckBoxSelectedListener = listener;
	}
	
	//外部控件监听checkBox状态事件
	public interface OnCheckBoxSelectedListener{
		public void onSelected(int position, boolean isCheck);
		public boolean validate(boolean isCheck);
	}
	
}
