package com.xtion.sheet.cell;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;


/**
 * Created by Administrator on 2015-10-9.
 *
 * 1.auto fit width and height
 *
 *
 *
 */
public class EditTextCell extends Cell {
    private String text;
    private TextView textView;

    public EditTextCell(Context context, AlphaAdapter adapter) {
        super(context, adapter);
        textView = (TextView)findViewById(R.id.textView);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(isFocused()) {
//                    textView.measure(0, 0);
//                    int maxWidth = textView.getMeasuredWidth();

                    //100dp - 4dp = 96dp
                }
//
//                Log.d("Thomas", "current w,h : " + textView.getMeasuredWidth() + "," + textView
//                        .getMeasuredHeight() + " root: "  + root.getHeight() + "," + root.getWidth());

            }
        });

    }

    @Override
    public void setWH(int width, int height) {

    }

    @Override
    public void setTheme() {
//        setBgColor(Color.WHITE);
//        setTextColor(Color.MAGENTA);

    }

    @Override
    public void setBgColor(int color) {

    }

    @Override
    public void setTextColor(int color) {

    }

    /**
     * adapter onBindViewHolder的时候会调用setData
     */
    @Override
    public void setData(int position) {
//        String text = this.getAdapter().getData(position);
//        textView.setText(text);

        //every time change the text , the Layoutmanager would refresh the
        //cell, we just change the height here dynamically

    }
}
