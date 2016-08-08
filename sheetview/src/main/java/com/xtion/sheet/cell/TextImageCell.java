package com.xtion.sheet.cell;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;
import com.xtion.sheet.model.ISheetRowModel;

/**
 * Created by Administrator on 2015-10-8.
 */
public class TextImageCell extends Cell {
    private String text;
    private TextView textView;
    private ImageView imageView;
    private View root;

    public TextImageCell(Context context, AlphaAdapter adapter) {
        super(context, adapter);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cell_text_image,this);
        textView = (TextView)findViewById(R.id.cell_text_image_tv);
        imageView = (ImageView)findViewById(R.id.cell_text_image_iv);
        root = findViewById(R.id.cell_text_image_root);
    }


    @Override
    public void setTextColor(int color) {
        textView.setTextColor(color);
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
        String text = (String) mAdapter.getData(position);
        setText(text);

        if(getAdapter() instanceof AlphaAdapter){
            ISheetRowModel rowModel = ((AlphaAdapter)this.getAdapter()).getRowModel(position);
            if("1".equals(rowModel.getCellValue().get("xwneeduploadfile"))){
                imageView.setVisibility(View.VISIBLE);
            }
        }


    }
}
