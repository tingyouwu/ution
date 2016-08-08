package com.wty.ution.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.wty.ution.R;

/**
 * @author wty
 * 继承于DispatchTourchListView，实现了OnScrollListener(运用onScroll/onScrollStateChanged两个方法动态回调);
重写了ListView的setAdapter方法,Adpater为ScrollListViewAdapter(继承于BaseAdapter)s
 **/
public class ScrollFootListView extends DispatchTouchListView implements OnScrollListener{

	private ScrollListViewAdapter adapter;
	private int start_index;
	private int end_index;
	private boolean isScrollFootLoading;
	private boolean footLoadingAble = true;
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		this.adapter = (ScrollListViewAdapter) adapter;
	}
	LinearLayout footView;
    public ScrollFootListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }
    
    OnScrollFixItemListener onScrollFixItemListener;
    private int itemPosition;
    public void setFixItem(int itemPosition){
        this.itemPosition = itemPosition;
    }
    
    public void setOnScrollFixItemListener(OnScrollFixItemListener onScrollFixItemListener){
        this.onScrollFixItemListener = onScrollFixItemListener;
    }
    
    public interface OnScrollFixItemListener{
        public void onScrollFixItem();
        public void onLeaveFixItem();
    }

	@Override
	public void onScroll(AbsListView listview, int firstVisiableItem, int visibleItemCount,int totalItemCount) {
		if(onScrollFixItemListener!=null){
			if(firstVisiableItem >= itemPosition && onScrollFixItemListener!=null){
				onScrollFixItemListener.onScrollFixItem();
			}else if(onScrollFixItemListener!=null){
				onScrollFixItemListener.onLeaveFixItem();
			}
		}
		start_index = firstVisiableItem;
        end_index = firstVisiableItem + visibleItemCount;
		
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch(scrollState){  
        case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
        	if(adapter!=null){
        		adapter.setBusy(false);
        		adapter.loadVisableImage(start_index,end_index);
        	}
            
            if (getLastVisiblePosition() == (getCount() - 1)) {
                System.out.println("--------------------滚动到底部了--------------------");
                if(onScrollFootListener!=null){
                	if(!isScrollFootLoading && footLoadingAble){
                		footView.setVisibility(View.VISIBLE);
                		onScrollFootListener.onScrollFoot();
                		isScrollFootLoading = true;
                	}
                	
                }
                
            }
            // 判断滚动到顶部
            if (getFirstVisiblePosition() == 0) {
                System.out.println("--------------------滚动到顶部--------------------");
                if(onScrollHeadListener!=null){
                    onScrollHeadListener.onScrollHead();
                }
            }
            
            break;  
        case OnScrollListener.SCROLL_STATE_FLING://滚动状态  
        	if(adapter!=null){
        		adapter.setBusy(true);
        	}
            break;  
        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
        	if(adapter!=null){
        		adapter.setBusy(true);
        	}
            break;  
        }
	}
	
	
	public void onScrollFootLoadComplete(){
		if(footView!=null){
			footView.setVisibility(View.GONE);
		}
		isScrollFootLoading = false;
	}
	
	public boolean isFootLoadingAble() {
		return footLoadingAble;
	}

	public void setFootLoadingAble(boolean footLoadingAble) {
		this.footLoadingAble = footLoadingAble;
		if(footLoadingAble==false && footView!=null){
			footView.setVisibility(View.GONE);
		}
	}
	
    public void setOnScrollFootListener(OnScrollFootLoadingListener onScrollFootListener) {
        this.onScrollFootListener = onScrollFootListener;
     	// 底部正在加载
		View ft = LayoutInflater.from(getContext()).inflate(R.layout.mylistview_foot, null);
		this.addFooterView(ft);
		footView = (LinearLayout) ft.findViewById(R.id.foot_layout);
		footView.setVisibility(View.GONE);
		isScrollFootLoading = false;
    }

    public void setOnScrollHeadListener(OnScrollHeadListener onScrollHeadListener) {
        this.onScrollHeadListener = onScrollHeadListener;
    }

	private OnScrollFootLoadingListener onScrollFootListener;
    private OnScrollHeadListener onScrollHeadListener;
    
    public interface OnScrollFootLoadingListener{
        public void onScrollFoot();
    }
    
    public interface OnScrollHeadListener{
        public void onScrollHead();
    }

}