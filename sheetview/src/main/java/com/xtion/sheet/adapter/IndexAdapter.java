package com.xtion.sheet.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import com.xtion.sheet.DataSet;
import com.xtion.sheet.SheetRecyclerView;
import com.xtion.sheet.ViewSet;
import com.xtion.sheet.holder.IndexViewHolder;
import com.xtion.sheet.model.ISheetCellModel;
import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetRowModel;


/**
 * Created by Administrator on 2015-10-13.
 */
public class IndexAdapter extends AbstractSheetAdapter {
    private int adapterType = VERTICAL_TYPE;

    private OnColumnClickListener mOnColumnClickListener;
    private OnRowClickListener mOnRowClickListener;

    public static final int VERTICAL_TYPE = 0;
    public static final int HORIZONTAL_TYPE = 1;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_EDITTEXT = 2;

    private int count;
    //存储cells的width 和 height
    //this is the data set that determine the cells' width and height
    //adapter and layoutmanager should never touch each other directly
//    private int[] lines;

//    private int cellWidth;
//    private int cellHeight;

//    private int mainListColumnCount = 0;

    public IndexAdapter(SheetRecyclerView parent, DataSet dataSet, ViewSet viewSet, int type) {
        super(parent,dataSet,viewSet);

        this.adapterType = type;
        if(adapterType == VERTICAL_TYPE) {
            this.count = dataSet.getRowCount();
        } else {
            this.count = dataSet.getColumnCount();
        }

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        //根据位置来确定类型！
        int type = TYPE_TEXT;

        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set the view's margins, paddings and layout parameters
        return new IndexViewHolder(parent.getContext(), this);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

        //get the item value
        ISheetCellModel model = getItem(position);
        String text = model.getCellText();

        Log.d("Thomas", "onBindViewHolder is invoked");
        //根据位置position来决定显示的data，同样我们也要根据position来决定其他data
        IndexViewHolder viewHolder = (IndexViewHolder) holder;
        viewHolder.setValue(text);

        if(adapterType == VERTICAL_TYPE){
            //纵向，取第一行的宽度
            int width = dataSet.getIndexWidth();
            int height = dataSet.getRowsHeight()[position];
            viewHolder.setWH(width,height);

            int bgColor = viewSet.rowBgColor;
            int textColor = viewSet.rowTextColor;
            viewHolder.setColor(bgColor,textColor);

        }else{
            //横向，取第一列的高度
            int width = dataSet.getColumnsWidth()[position];
            int height = dataSet.getIndexHeight();
            viewHolder.setWH(width,height);

            int bgColor = viewSet.columnBgColor;
            int textColor = viewSet.columnTextColor;
            viewHolder.setColor(bgColor,textColor);
        }
    }

    public void onItemHolderClick(RecyclerView.ViewHolder itemHolder){
        int position = itemHolder.getAdapterPosition();
        if (adapterType == VERTICAL_TYPE && mOnRowClickListener != null) {
            mOnRowClickListener.onClick((ISheetRowModel) getItem(position));
        } else if (mOnColumnClickListener != null) {
            mOnColumnClickListener.onClick((ISheetColumnModel) getItem(position));
        }
    }

    @Override
    public Object getData(int position) {
        return getItem(position).getCellText();
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    public void setOnRowClickListener(OnRowClickListener mOnRowClickListener) {
        this.mOnRowClickListener = mOnRowClickListener;
    }

    public void setOnColumnClickListener(OnColumnClickListener mOnColumnClickListener) {
        this.mOnColumnClickListener = mOnColumnClickListener;
    }

    public ISheetCellModel getItem(int position){
        ISheetCellModel model;
        if(adapterType == VERTICAL_TYPE) {
            model = dataSet.getRowModels().get(position);
        }else{
            model = dataSet.getColumnModels().get(position);
        }
        return model;
    }

}
