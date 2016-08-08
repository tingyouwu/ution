package com.wty.ution.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.wty.ution.R;
import com.wty.ution.widget.listview.ScrollListViewAdapter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumIndexAdapter extends ScrollListViewAdapter {
	private List<AlbumIndexItem> aibumList;
	private Context context;
	private ViewHolder holder;
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer,ViewHolder> cacheViews = new HashMap<Integer,ViewHolder>();
	ImageLoader imageloader;
	public AlbumIndexAdapter(List<AlbumIndexItem> list, Context context ) {
		this.aibumList = list;
		this.context = context;
		imageloader = ImageLoader.getInstance();
	}
	
	@Override
	public int getCount() {
		return aibumList.size();
	}

	@Override
	public AlbumIndexItem getItem(int position) {
		return aibumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;   
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.item_album_index, null);
			holder = new ViewHolder();
			holder.iv = (ImageView)convertView.findViewById(R.id.item_albumindex_image);
			holder.tv = (TextView)convertView.findViewById(R.id.item_albumindex_name);
			holder.cb = (CheckBox)convertView.findViewById(R.id.item_albumindex_cb);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		/** 通过ID 获取缩略图*/
		holder.iv.setImageResource(R.drawable.bg_gray_rd);
		AlbumIndexItem item = getItem(position);
		if(!isBusy()){
			imageloader.displayImage(Scheme.FILE.wrap(item.getBitmap()), holder.iv);
		}
		holder.tv.setText(item.getName()+" ( "+ item.getCount()+" )");
		holder.cb.setChecked(true);
		holder.cb.setClickable(false);
		if(item.isSelected()){
		    holder.cb.setVisibility(View.VISIBLE);
		}else{
		    holder.cb.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		ImageView iv;
		TextView tv;
		CheckBox cb;
	}

	@Override
	public void loadVisableImage(int firstVisPosition, int lastVisPosition) {
		super.loadVisableImage(firstVisPosition, lastVisPosition);
		for(int i=firstVisPosition;i<lastVisPosition;i++){
			ViewHolder holder = cacheViews.get(i);
			AlbumIndexItem photoItem = getItem(i);
			imageloader.displayImage(Scheme.FILE.wrap(photoItem.getBitmap()), holder.iv);
		}
	}
	
}
