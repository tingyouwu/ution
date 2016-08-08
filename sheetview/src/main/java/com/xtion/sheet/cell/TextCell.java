package com.xtion.sheet.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AbstractSheetAdapter;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;

/**
 * Created by Administrator on 2015-10-8.
 */
public class TextCell extends Cell{
    private TextView textView;
    private View root;

    public TextCell(Context context, AbstractSheetAdapter adapter) {
        super(context,adapter);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_text,this);
        textView = (TextView)findViewById(R.id.cell_text_tv);
        root = findViewById(R.id.cell_text_root);
    }

    @Override
    public void setWH(int width, int height) {
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = width;
        params.height = height;
    }

    @Override
    public void setBgColor(int color) {
        root.setBackgroundColor(color);
    }

    @Override
    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    @Override
    public void setTheme() {

    }

    public void setText(String text) {
        textView.setText(text);
    }

    public TextView getTextView() {
        return textView;
    }

    @Override
    public void setData(int position) {
        String text = (String)this.getAdapter().getData(position);
        setText(text);
    }

}
