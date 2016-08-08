package com.wty.ution.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.wty.ution.R;


public class AlphaView extends ImageView {
	private Drawable alphaDrawable;
	private boolean showBkg; // 是否显示背景
	private int choose; // 当前选中首字母的位置
	private int width;
	private int height;
	private String[] ALPHAS;
	private OnAlphaChangedListener listener;
	boolean hasMeasured = false;

	public AlphaView(Context context) {
		this(context,null);
	}

	public AlphaView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public AlphaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAlphaView();
	}

	private void initAlphaView() {
		showBkg = false;
		choose = -1;
		setImageResource(R.drawable.alpha_normal);
		alphaDrawable = getDrawable();
		width = alphaDrawable.getIntrinsicWidth();
		ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                if (hasMeasured == false)
                {
                	//获取到宽度和高度后，可用于计算 
                    height = getMeasuredHeight();
                    alphaDrawable.setBounds(0, 0, width, height);
                    hasMeasured = true;

                }
                return true;
            }
        });
		
		ALPHAS = new String[28];
		ALPHAS[0] = " "; // " "代表搜索
		ALPHAS[27] = "#";
		for (int i = 0; i < 26; i++) {
			ALPHAS[i + 1] = String.valueOf((char) (65 + i));
		}
		setMaxWidth(50);
		setMinimumWidth(25);
		setMinimumHeight(342);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (showBkg) {
			setImageResource(R.drawable.alpha_pressed);
		} else {
			setImageResource(R.drawable.alpha_normal);
		}
		
		if(height == 0){
			height = getMeasuredHeight();
		}
		alphaDrawable = getDrawable();
		alphaDrawable.setBounds(0, 0, width, height);
		canvas.save();
		alphaDrawable.draw(canvas);
		canvas.restore();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		final int oldChoose = choose;
		final int c = (int) (y / height * 28);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < ALPHAS.length) {
					listener.OnAlphaChanged(ALPHAS[c], c);
					choose = c;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c >= 0 && c < ALPHAS.length) {
					listener.OnAlphaChanged(ALPHAS[c], c);
					choose = c;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	// 设置事件
	public void setOnAlphaChangedListener(OnAlphaChangedListener listener) {
		this.listener = listener;
	}

	// 事件接口
	public interface OnAlphaChangedListener {
		public void OnAlphaChanged(String s, int index);
	}

}
