package com.xtion.sheet.mvp;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xtion.sheet.R;

/**
 * Created by Administrator on 2015-9-29.
 *
 * OuterLayout may have different style
 *
 */
public class OuterBarView extends LinearLayout{

    private View searchLayout;
    private SheetPresenter presenter;
    private onEditTextChangedListener mListener;
    //private Rtx rtx;
    private Activity activity;

    private EditText textInput;
    private Button button;

    public OuterBarView(Activity activity, SheetPresenter presenter) {
        super(activity);
        this.activity = activity;
        this.presenter = presenter;
        setOrientation(LinearLayout.VERTICAL);
    }

    public void addContentView(InnerSheetView sheet) {

        addView(sheet);
    }

    private void startMainActivity() {
//        Intent intent = new Intent(activity, MainActivity.class);
//        activity.startActivity(intent);
    }

    private View initSearchView() {

        searchLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.foldllist_item, null);
        textInput = (EditText)searchLayout.findViewById(R.id.foldlist_search_view);
        button = (Button)searchLayout.findViewById(R.id.foldlist_barcode_view);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mListener = (onEditTextChangedListener) presenter;
                } catch (ClassCastException e) {
                    throw new ClassCastException(presenter.toString() + " must implement " +
                            "onEditTextChangedListener");
                }
                mListener.onEditTextChanged(s.toString());
            }
        });
        return searchLayout;
    }

    public void setTextInput(String text) {

        final InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(textInput, InputMethodManager.SHOW_IMPLICIT);

        if(textInput!=null && textInput.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        textInput.setText(text);
    }

    public View getSearchLayout() {
        if(searchLayout==null){
            searchLayout = initSearchView();
        }
        return searchLayout;
    }

    public LinearLayout getLayout() {
        return this;
    }

    //SheetPresenter must implement this interface
    public interface onEditTextChangedListener{
        public void onEditTextChanged(String text);
    }
}
