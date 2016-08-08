package com.wty.ution.widget.listview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.wty.ution.R;


public class ScrollRefreshListView extends CustomListView{
	
	private boolean isScrollFootLoading;
	protected boolean footLoadingAble = true;
	private LinearLayout footViewContainer;
	private View defaultFootView;
	OnScrollFixItemListener onScrollFixItemListener;
    private int itemPosition;
    public void setFixItem(int itemPosition){
        this.itemPosition = itemPosition+1;
    }
    
    public void setOnScrollFixItemListener(OnScrollFixItemListener onScrollFixItemListener){
        this.onScrollFixItemListener = onScrollFixItemListener;
    }
	
    public ScrollRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
     // 底部正在加载
		View ft = LayoutInflater.from(context).inflate(R.layout.mylistview_foot, null);
		this.addFooterView(ft);
		footViewContainer = (LinearLayout) ft.findViewById(R.id.foot_layout);
        if(!isInEditMode()){
            footViewContainer.addView(getDefaultFootView());
            footViewContainer.setVisibility(View.GONE);
            isScrollFootLoading = false;
        }

    }
    
    public interface OnScrollFixItemListener{
        void onScrollFixItem();
        void onLeaveFixItem();
    }
    
    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
    
    	super.onScroll(arg0, firstVisiableItem, visibleItemCount, totalItemCount);
		if(onScrollFixItemListener!=null){
			if(firstVisiableItem >= itemPosition){
				onScrollFixItemListener.onScrollFixItem();
			}else {
				onScrollFixItemListener.onLeaveFixItem();
			}
		}
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
        switch(scrollState){  
        case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
        	//this.getAdapter() 得到的 adapter并不是用戶設置的，而是包含了header的
            if (getLastVisiblePosition() == (this.getAdapter().getCount() - 1)) {
            	System.out.println("--------------------滚动到底部了--------------------");
            	
            	if(footLoadingAble && !isScrollFootLoading && state != REFRESHING){
            		if(onScrollFootListener!=null){
            			boolean empty = !onScrollFootListener.loadFromLocal();
            			if(empty){
            				
            				new AsyncTask<String, Integer, Boolean>() {
            					@Override
            					protected void onPreExecute() {
            						isScrollFootLoading = true;
            						footViewContainer.setVisibility(View.VISIBLE);
            					}
            					
            					protected void onPostExecute(Boolean result) {
            						isScrollFootLoading = false;
            						footViewContainer.setVisibility(View.GONE);
            					}

								@Override
								protected Boolean doInBackground(String... params) {
									//返回在线查询结果
									try {
										return onScrollFootListener.loadFromService();
									} catch (Exception e) {
										e.printStackTrace();
										return true;
									}
									
								}
							}.execute();
            			}
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
            break;  
        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动  
            break;  
        }
    }

    public void setOnScrollFootListener(OnScrollFootListener onScrollFootListener) {
        this.onScrollFootListener = onScrollFootListener;
    }

    public void setOnScrollHeadListener(OnScrollHeadListener onScrollHeadListener) {
        this.onScrollHeadListener = onScrollHeadListener;
    }
    
    private OnScrollFootListener onScrollFootListener;
    private OnScrollHeadListener onScrollHeadListener;
    
    public interface OnScrollFootListener{
    	boolean loadFromLocal();
    	boolean loadFromService();
    }
    
    public interface OnScrollHeadListener{
		void onScrollHead();
    }

    public void onScrollFootLoadComplete(){
		footViewContainer.setVisibility(View.GONE);
		isScrollFootLoading = false;
	}
	
	public boolean isFootLoadingAble() {
		return footLoadingAble;
	}

    public LinearLayout getFootViewLayout(){
        return footViewContainer;
    }

	public void setFootLoadingAble(boolean footLoadingAble) {
		this.footLoadingAble = footLoadingAble;
		if(!footLoadingAble){
			footViewContainer.setVisibility(View.GONE);
		}
	}

    public void setCustomFootView(View customFootView){
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customFootView.setLayoutParams(lp);
        footViewContainer.removeAllViews();
        footViewContainer.addView(customFootView);
    }

    public View getDefaultFootView(){
        if(defaultFootView==null){
            defaultFootView = LayoutInflater.from(context).inflate(R.layout.layout_foot, null);
        }
        return defaultFootView;
    }

}