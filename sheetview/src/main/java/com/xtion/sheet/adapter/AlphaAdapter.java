package com.xtion.sheet.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;


import com.xtion.sheet.DataSet;
import com.xtion.sheet.SheetRecyclerView;
import com.xtion.sheet.ViewSet;
import com.xtion.sheet.cell.Cell;
import com.xtion.sheet.holder.SheetCellHolder;
import com.xtion.sheet.cell.SheetCellFactory;
import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetRowModel;

import java.util.Map;

/**
 * Created by Administrator on 2015-9-29.
 */
public class AlphaAdapter extends AbstractSheetAdapter {

//    private boolean fixedCellRect;

//    private AdapterView.OnItemClickListener mOnItemClickListener;
    private OnCellClickListener mOnItemClickListener;

    //存储cells的width 和 height,怕影响效率，我们用原生的数组
    //this is the data set that determine the cells' width and height
    //adapter and layoutmanager should never touch each other directly
//    private int[] columnsWidth;
//    private int[] rowsHeight;



    public AlphaAdapter(SheetRecyclerView parent,DataSet dataSet,ViewSet viewSet) {

        //1.get some initial data from dataSet
        super(parent,dataSet,viewSet);
//        this.fixedCellRect = dataSet.isFixedCellRect();

        //2.generate more data
        // /data set of all the cell's height and width in the sheet
//        columnsWidth = dataSet.getColumnsWidth();
//        rowsHeight = dataSet.getRowsHeight();
    }

    //function to handle click events
    public void setOnItemClickListener(OnCellClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {

            int position = itemHolder.getAdapterPosition();
            int columnCount = dataSet.getColumnCount();
            int row = position / columnCount;
            int column = position % columnCount;

            ISheetRowModel rowModel = dataSet.getRowModels().get(row);
            ISheetColumnModel columnModel = dataSet.getColumnModels().get(column);
            String value;

            String columnid = columnModel.getColumnId();
            Map<String,String> rowValues = rowModel.getCellValue();
            if(rowValues==null || TextUtils.isEmpty(columnid)
                    || !rowValues.containsKey(columnid)){
                value = "";
            }else{
                value = rowValues.get(columnid);
            }
            mOnItemClickListener.onClick(columnModel,rowModel,value);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set the view's margins, paddings and layout parameters

        Cell cell = SheetCellFactory.getInstance().createCell(viewType,this,parent);

        SheetCellHolder viewHolder = new SheetCellHolder(cell,this);
        return viewHolder;
    }


    /*
        When attempting to supply the LayoutManager with a new view,

         1.a Recycler will first check the scrap heap for a matching position/id; if one exists, it will
         be returned without re-binding to the adapter data.

         If no matching view is found,
         2.the Recycler will instead pull a suitable view from the recycle
         pool and bind the necessary data to it from the adapter:

        RecyclerView.Adapter.bindViewHolder()


         In cases where no valid views exist in the recycle pool, a new view will be created instead
         (i.e. RecyclerView.Adapter.createViewHolder() is invoked) before being bound, and returned:

        RecyclerView.Adapter.createViewHolder()

        we reset the data on onBindViewHolder
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ///we get item type here
        int columnCount = dataSet.getColumnCount();
        int row = position / columnCount;
        int column = position % columnCount;
        SheetCellHolder holder1 = (SheetCellHolder) holder;

        holder1.getCell().setData(position);
        holder1.getCell().setWH(getColumnsWidth()[column], getRowsHeight()[row]);

    }

    @Override
    public int getItemCount() {
        return this.dataSet.getTotalCellCounts();
    }


    //get data from dataSet
    public ISheetRowModel getRowModel(int position) {

        int columnCount = dataSet.getColumnCount();
        int row = position / columnCount;
        return dataSet.getRowModels().get(row);
    }

    //get data from dataSet
    @Override
    public Object getData(int position) {

        int columnCount = dataSet.getColumnCount();
        int row = position / columnCount;
        int column = position % columnCount;

        ISheetRowModel rowModel = dataSet.getRowModels().get(row);
        ISheetColumnModel columnModel = dataSet.getColumnModels().get(column);

        String columnid = columnModel.getColumnId();
        Map<String,String> rowValues = rowModel.getCellValue();

        if(rowValues==null || TextUtils.isEmpty(columnid)
                || !rowValues.containsKey(columnid)){
            return "";
        }
        return rowValues.get(columnid);
    }



    public int[] getColumnsWidth() {
        return dataSet.getColumnsWidth();
    }

    public int[] getRowsHeight() {
        return dataSet.getRowsHeight();
    }


    public interface OnCellClickListener{
        void onClick(ISheetColumnModel column, ISheetRowModel row, String value);
    }
}

