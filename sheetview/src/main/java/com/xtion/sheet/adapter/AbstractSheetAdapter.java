package com.xtion.sheet.adapter;

import android.support.v7.widget.RecyclerView;

import com.xtion.sheet.DataSet;
import com.xtion.sheet.SheetRecyclerView;
import com.xtion.sheet.ViewSet;
import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetRowModel;

/**
 * Created by Administrator on 2015-9-29.
 */
public abstract class AbstractSheetAdapter extends RecyclerView.Adapter {
    protected ViewSet viewSet;
    protected DataSet dataSet;
    protected SheetRecyclerView parent;

    //current focus item pos in the adapter
    private int currentFocusPos = -1;

    AbstractSheetAdapter(SheetRecyclerView parent,DataSet dataSet,ViewSet viewSet){
        this.dataSet = dataSet;
        this.parent = parent;
        this.viewSet = viewSet;
    }

    public abstract void onItemHolderClick(RecyclerView.ViewHolder viewHolder);

    public abstract Object getData(int position);

    interface OnCellClickListener{
        void onClick(ISheetColumnModel column, ISheetRowModel row, String value);
    }

    public interface OnRowClickListener{
        void onClick(ISheetRowModel columnModel);
    }

    public interface OnColumnClickListener{
        void onClick(ISheetColumnModel columnModel);
    }

    public int getCurrentFocusPos() {
        return currentFocusPos;
    }

    public void setCurrentFocusPos(int currentFocusPos) {
        this.currentFocusPos = currentFocusPos;
    }



}

