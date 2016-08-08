package com.wty.ution.widget.filter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.widget.filter.base.FilterLabelContainerView.IContainerView;

import java.util.List;
import java.util.UUID;

/**
 * 功能描述：过滤button,每个button对应一个containerView 控制显示隐藏
 **/
public class FilterLabelButton extends RelativeLayout implements View.OnClickListener,IFilterEvent{
	private String buttonid;
	private TextView txt;
	private ImageView icon;

	private int[] mSelectedResource = new int[2];
	private int[] mSelectedColor = new int[2];
	
	private String mText = "";
	private boolean selected = false;
	private IContainerView contentView;
	
	private FilterEventBus eventBus;
	
	private boolean isOrderType = false;
	
	private FilterLabelButton(Context context, Builder builder) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_filterlabel_button, this);
		this.mSelectedColor = builder.mSelectedColor;
		this.mSelectedResource = builder.mSelectedResource;
		this.mText = builder.mText;
		this.selected = false;
		this.contentView = builder.contentView;
		this.buttonid = UUID.randomUUID().toString();
		this.isOrderType = builder.isOrderType;
		initView();
	}
	
	private void initView() {
		
		txt = (TextView)findViewById(R.id.filterlabel_button_text);
		icon = (ImageView)findViewById(R.id.filterlabel_button_image);
		
		setText(mText);
		setSelected(selected);
		setOnClickListener(this);

	}
	
	public void setContentView(IContainerView contentView){
		this.contentView = contentView;
	}
	
	public String getText() {
		return mText;
	}

	public void setText(String mText) {
		this.mText = mText;
		txt.setText(mText);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			txt.setTextColor(getResources().getColor(mSelectedColor[1]));
			icon.setImageResource(mSelectedResource[1]);
			
		} else {
			txt.setTextColor(getResources().getColor(mSelectedColor[0]));
			icon.setImageResource(mSelectedResource[0]);
		}
	}

	public IContainerView getContentView(){
		return contentView;
	}
	
	public String getButtonId(){
		return buttonid;
	}
	public static class Builder{
		public Context context;
		public IContainerView contentView;
		public String mText;
		public int[] mSelectedResource = {R.drawable.conditionsearch_gray_sort,R.drawable.conditionsearch_blue_sort};
		public int[] mSelectedColor = {R.color.gray_font_2,R.color.blue_crm}; 
		public boolean isOrderType = false;
		public Builder(Context context) {
			this.context = context;
		}
		
		public Builder setText(String text){
			this.mText = text;
			return this;
		}
		
		public Builder setContentView(IContainerView contentView){
			this.contentView = contentView;
			return this;
		}
		
		public Builder setTextColor(int selected,int unselected){
			mSelectedColor[1] = selected;
			mSelectedColor[0] = unselected;
			return this;
		}
		
		public Builder setImage(int selected,int unselected){
			mSelectedResource[1] = selected;
			mSelectedResource[0] = unselected;
			return this;
		}
		
		public Builder setOrderType(boolean isOrder){
			isOrderType = isOrder;
			return this;
		}
		
		public FilterLabelButton create(){
			return new FilterLabelButton(context,this); 
		}
	}
	
	@Override
	public void onClick(View v) {
		
		boolean select = !isSelected();
		System.out.println("button click "+buttonid+" select = "+select);
		
		setSelected(select);
		if(eventBus == null)return;
		//告诉外部tab哪一个button被选中
		eventBus.onButtonEvent(getEventId(), buttonid, select);
		if(contentView!=null){
			eventBus.onViewShow(getEventId(), contentView.getViewId(), select);
		}
	}


	@Override
	public void onButtonEvent(String buttonid, boolean selected) {
		System.out.println("button "+this.buttonid+" onButtonEvent :"+buttonid+" is "+selected);
		if(!this.buttonid.equals(buttonid)){
			//点击的不是自己，都要至反
			setSelected(false);
		}
	}

	@Override
	public void onSubmit(List<IFilterModel> filters) {
		
	}

	@Override
	public void onSubmit(IFilterModel filters) {
		
	}
	
	@Override
	public String getEventId() {
		return buttonid;
	}

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;
        if(contentView!=null){
            contentView.onRegister(bus);
        }
    }

    @Override
	public void onViewShow(String viewid, boolean show) {
		
		if(show){
			//窗口显示某页面
			boolean isViewEquel = contentView!=null && viewid.equals(contentView.getViewId()); 
			if(isViewEquel){
				//自己的页面显示
				setSelected(true);
			}else{
                setSelected(false);
			}
		}else{
			//窗口关闭
			setSelected(false);
		}
		
		
	}
	
	@Override
	public void onCancel() {
		if(isOrderType){
			//初始文字
			txt.setText(mText); 
		}
	}

	@Override
	public void onCancel(IFilterModel filter) {
	}

	@Override
	public void onOrderEvent(IFilterModel model) {
		if(isOrderType){
			txt.setText(model.getLabel());
		}
	}

}	