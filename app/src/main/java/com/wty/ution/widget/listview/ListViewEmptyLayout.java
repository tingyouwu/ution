package com.wty.ution.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;

public class ListViewEmptyLayout extends LinearLayout {

	
	TextView tv;
	ImageView iv;
	public ListViewEmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_listview_empty, this);
		tv = (TextView)findViewById(R.id.listview_empty_text);
		iv = (ImageView)findViewById(R.id.listview_empty_img);
	}

    public void setNormalEmpty(String text,int resource){
        iv.setImageResource(resource);
        tv.setText(text);
    }

	public void setNormalEmpty(String text){
		iv.setImageResource(R.drawable.img_empty1);
		tv.setText(text);
	}
	
	public void setSearchEmpty(String text){
		iv.setImageResource(R.drawable.img_search_empty);
		tv.setText(text);
	}
	
}
