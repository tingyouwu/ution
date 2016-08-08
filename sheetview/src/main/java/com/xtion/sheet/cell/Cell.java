package com.xtion.sheet.cell;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xtion.sheet.adapter.AbstractSheetAdapter;

/**
 * Created by apple on 16/5/5.
 */
public abstract class Cell extends LinearLayout {
    protected AbstractSheetAdapter mAdapter;

    public Cell(Context context,AbstractSheetAdapter mAdapter) {
        super(context);
        this.mAdapter = mAdapter;
    }

    public AbstractSheetAdapter getAdapter() {
        return mAdapter;
    }

    public abstract void setWH(int width, int height);

    public View getRoot() {
        return this;
    }

    //interface for extendtion 支持不同字体颜色和背景
    //几乎每种cell都有bg color和 text color，可能image仅仅是显示一张图片而已
    //支持以后的theme设置
    public abstract void setTheme();
    public abstract void setBgColor(int color);
    public abstract void setTextColor(int color);

    //get data from dataSet which is referenced in the adapter, then set data
    //cell have no absolute relation with the position, so setData should be
    //invoiked in onBindViewHolder() in the adapter
    public abstract void setData(int position);

}
