package com.wty.ution.widget.titlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.widget.titlemenu.TitleMenuModel.TitleMenuEvent;

public class TitleMenuItemView extends RelativeLayout implements View.OnClickListener{

	private ImageView iv_unread;
	private ImageView iv_select;
	private TextView tv_text;
	private TextView tv_total;
	private TitleMenuEvent event;
	private TitleMenuItemView(Context context,Builder builder){
		super(context);
		init();
		setBuilder(builder);
	}
	
	public TitleMenuItemView(Context context,AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setBuilder(Builder builder){
		setText(builder.text);
		setUnread(builder.unread);
		setTotal(builder.total,builder.ignoreTotal);
		setSelected(builder.selected);
		setOnSelectEvent(builder.event);
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_titlemenu_item, this);
		iv_unread = (ImageView)findViewById(R.id.titlemenu_item_notice);
		tv_text = (TextView)findViewById(R.id.titlemenu_item_text);
		tv_total = (TextView)findViewById(R.id.titlemenu_item_total);
		iv_select = (ImageView)findViewById(R.id.titlemenu_item_selete);
	}

	public void setUnread(int unread){
		if(unread==0){
			iv_unread.setVisibility(View.GONE);
		}else{
			iv_unread.setVisibility(View.GONE);
		}
	}
	
	public void setTotal(int total,boolean ignoreTotal){
		tv_total.setText("("+total+")");
		if(ignoreTotal){
			tv_total.setVisibility(View.GONE);
		}else{
			tv_total.setVisibility(View.VISIBLE);
		}
	}
	
	public void setSelected(boolean selected){
		if(selected){
			tv_total.setTextColor(getResources().getColor(R.color.white));
			tv_text.setTextColor(getResources().getColor(R.color.white));
			iv_select.setVisibility(View.VISIBLE);
		}else{
			tv_total.setTextColor(getResources().getColor(R.color.white));
			tv_text.setTextColor(getResources().getColor(R.color.white));
			iv_select.setVisibility(View.GONE);
		}
	}
	
	public void setText(String text) {
		this.tv_text.setText(text);
	}

	public void setOnSelectEvent(TitleMenuEvent event) {
		this.event = event;
	}

	@Override
	public void onClick(View v) {
		if(event!=null){
			event.onSelect();
		}
	}
	
	
	public static class Builder {
		public Context context;
		public TitleMenuEvent event;
		public String text;
		public int total;
		public int unread;
		public boolean selected; 
		public boolean ignoreTotal;
		public Builder(Context context) {
			this.context = context;
		}
		
		public Builder setText(String text){
			this.text = text;
			return this;
		}
		
		public Builder setEvent(TitleMenuEvent event){
			this.event = event;
			return this;
		}
		
		public Builder setTotal(int total){
			this.total = total;
			return this;
		}
		
		public Builder setTotal(int total,boolean ignoreTotal){
			this.total = total;
			this.ignoreTotal = ignoreTotal;
			return this;
		}
		
		public Builder setUnread(int unread){
			this.unread = unread;
			return this;
		}
		
		public Builder setSelected(boolean selected){
			this.selected = selected;
			return this;
		}
		
		public TitleMenuItemView create(){
			return new TitleMenuItemView(context,this);
		}
	}
}