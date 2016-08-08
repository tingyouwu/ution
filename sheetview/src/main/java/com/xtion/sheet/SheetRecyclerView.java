package com.xtion.sheet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by 廖东明 on 2015-9-28.
 *
 * custom the recyclerView to support some features:
 * 1.slow down the fling to avoid the scrolling to fast lead to blank bug.
 *
 */
public class SheetRecyclerView extends RecyclerView {

//    private int cellTextColor;
//    private int cellBgColor;
    public SheetRecyclerView(Context context) {
        super(context);
//        init(context,null);
    }

    public SheetRecyclerView(Context context, AttributeSet attrs) {

        super(context, attrs);
//        init(context, attrs);
    }

    public SheetRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        init(context,attrs);
    }

//    private void init(Context context,AttributeSet attrs){
//        cellTextColor = Color.parseColor("#000000");
//        if(attrs!=null){
//            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SheetRecyclerView);
//            cellTextColor = ta.getColor(R.styleable.SheetRecyclerView_cellFontColor, Color.parseColor("#000000"));
//            ta.recycle();
//        }
//
//    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        ////////////////////////////////////////////////////
        ////avoid scroll to quickly lead to blank
        //Thomas 可能还有其他解决方法
        velocityY = (int)(velocityY * 0.5);
        velocityX = (int)(velocityX * 0.5);
        Log.v("MyRecyclerView", "it is fling");
        return super.fling(velocityX, velocityY);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

//    public int getCellTextColor(){
//        return cellTextColor;
//    }
//
//    public void setCellTextColor(int color){
//        this.cellTextColor = color;
//    }

}
