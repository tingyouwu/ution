package com.wty.ution.widget.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.ution.R;
import com.wty.ution.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author wty
 * 下拉刷新
 */
public class CustomListView extends DispatchTouchListView implements OnScrollListener {

	protected final static int RELEASE_To_REFRESH = 0;
	protected final static int PULL_To_REFRESH = 1;
	protected final static int REFRESHING = 2;
	protected final static int DONE = 3;
	protected final static int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LinearLayout headView;
	@Bind(R.id.head_tipsTextView) TextView tipsTextview;
	@Bind(R.id.head_lastUpdatedTextView) TextView lastUpdatedTextView;
	@Bind(R.id.head_arrowImageView) ImageView arrowImageView;
	@Bind(R.id.head_progressBar)  ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	protected int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private boolean isRefreshable;
	protected Context context;
	private Animation mRotateAnimation;
	private String refreshTipsText;
	private String lastUpdateTime;

	public CustomListView(Context context) {
		super(context);
		init(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	private void init(Context context) {
		this.context = context;
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setCacheColorHint(context.getResources().getColor(android.R.color.transparent));

		headView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mylistview_head, null);
		ButterKnife.bind(this,headView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		mRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
		LinearInterpolator lin = new LinearInterpolator();
		mRotateAnimation.setInterpolator(lin);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		if(!isInEditMode()){
			lastUpdateTime = CommonUtil.getTime();
		}
		setLastUpdateTime(lastUpdateTime);
		refreshTipsText = context.getString(R.string.scrollview_to_refreshing_label);
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
	    setFirstVisiableItem(firstVisiableItem);
	}

	public void setFirstVisiableItem(int firstVisiableItem) {
		this.firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {

					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					}

				}
				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText(context.getString(R.string.scrollview_to_refresh_release_label));

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				tipsTextview.setText(context.getString(R.string.scrollview_to_refresh_pull_label));
			} else {
				tipsTextview.setText(context.getString(R.string.scrollview_to_refresh_pull_label));
			}
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			arrowImageView.setImageResource(R.drawable.rotateimg);
			arrowImageView.startAnimation(mRotateAnimation);
			tipsTextview.setText(refreshTipsText);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			tipsTextview.setText(context.getString(R.string.scrollview_to_refresh_pull_label));
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void forbidRefresh(){
		isRefreshable = false;
	}
	
	public interface OnRefreshListener {
		void onRefresh();
	}

	public void onRefreshComplete(boolean changeUpdateTime){
		
		state = DONE;
		if(changeUpdateTime){
			lastUpdatedTextView.setText(context.getString(R.string.scrollview_to_refresh_lasttime_label) + CommonUtil.getTime());
		}
		changeHeaderViewByState();
	}
	
	public void onRefreshComplete() {
		onRefreshComplete(true);
	}

	private void onRefresh() {
		if (refreshListener != null) {
			
			boolean isNetworkAvailable = CommonUtil.isNetworkAvailable(context);
			if(!isNetworkAvailable){
				Toast.makeText(context, context.getString(R.string.network_failed), Toast.LENGTH_SHORT).show();
				this.onRefreshComplete(false);
			}else{
				refreshListener.onRefresh();
			}
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		super.setAdapter(adapter);
	}

	public void setFirstVisableItem(int firstVisibleItem, int visibleItemCount) {
		// TODO Auto-generated method stub
		this.firstItemIndex = firstVisibleItem;
	}
	
	public void setLastUpdateTime(String time){
		if(TextUtils.isEmpty(time)){
			lastUpdatedTextView.setVisibility(View.GONE);
		}else{
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setText(context.getString(R.string.scrollview_to_refresh_lasttime_label) + time);
		}
	}
	
	public void setRefreshTipsText(String text){
		refreshTipsText = text;
	}
}
