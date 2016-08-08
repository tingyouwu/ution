package com.wty.ution.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wty.ution.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchView extends LinearLayout{
	
	public static int SEARCH_EMPTY = 0xb;
	public static int SEARCH_CHANGE = 0xa;

	Activity activity;
	OnSearchListener searchListener;

	@Bind(R.id.search_operatorlayout) View layout_operator;
	@Bind(R.id.search_operator) TextView tv_operator;
	@Bind(R.id.search_loading) ProgressBar pb_loading;
	@Bind(R.id.search_content) EditText editText;
	@Bind(R.id.search_btn_del) ImageButton clearButton;

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SearchView(Context context) {
		this(context,null);
	}


	private void init(Context context){
		
		this.activity = (Activity) context;
		// 以下代码实现动态加载xml布局文件
		LayoutInflater li;
		// 获取LAYOUT_INFLATER_SERVICE，实例化LayoutInflater，实现动态加载布局
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.layout_search, this);
		ButterKnife.bind(view,activity);
		editText.clearFocus();
		editText.addTextChangedListener(watcher);
		
		clearButton.setVisibility(View.GONE);
		clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                editText.setText("");
            }
        });
	}

	public EditText getEditText(){
	    return editText;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what==SEARCH_CHANGE){

				final String searchContent = editText.getText().toString();
				if(searchContent!=null && searchContent.length()!=0){
				    clearButton.setVisibility(View.VISIBLE);
				}
				if(searchListener!=null){
				    searchListener.onSearchChange(searchContent);
				}
			}else if(msg.what==SEARCH_EMPTY){
				clearButton.setVisibility(View.GONE);
				if(searchListener!=null){
					searchListener.onSearchEmpty();
				}

			}

		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (0 < count || 0 < before) {
				 handler.sendEmptyMessageDelayed(SEARCH_CHANGE, 200);
			}
			if (start == 0 && count == 0) {
				handler.sendEmptyMessageDelayed(SEARCH_EMPTY,100);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {
		}
	};

	public void setOnSearchListener(OnSearchListener listener){
		this.searchListener = listener;
	}

	public interface OnSearchListener{
		public void onSearchChange(String content);
		public void onSearchEmpty();
	}

	public void clearText(){editText.setText("");}

	public void setHint(String hint){
		editText.setHint(hint);
	}

    public void setTextButton(String text,final OnClickListener listener){
        layout_operator.setVisibility(View.VISIBLE);
        tv_operator.setOnClickListener(listener);
        tv_operator.setText(text);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textview, int code, KeyEvent key) {
                if (code == EditorInfo.IME_ACTION_SEARCH) {
                    if(listener!=null)listener.onClick(textview);
                }
                return false;
            }
        });
    }

	public void HideTextButton() {
		layout_operator.setVisibility(View.GONE);
	}

//	public TextView getTextButton(){
//		return tv_operator;
//	}
	
	public void setMaxLength(int max){
		InputFilter[] filters = {new LengthFilter(max)};  
		getEditText().setFilters(filters); 
	}
	
	public void setLoading(boolean isLoading){
		if(isLoading){
			this.tv_operator.setVisibility(View.GONE);
			this.pb_loading.setVisibility(View.VISIBLE);
		}else{
			this.tv_operator.setVisibility(View.VISIBLE);
			this.pb_loading.setVisibility(View.GONE);
		}
		
	}
	
	
	
}
