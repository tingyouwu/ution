package com.xtion.sheet;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;


import com.xtion.sheet.model.ISheetCellModel;
import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetRowModel;
import com.xtion.sheet.mvp.InnerSheetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-10-8.
 */
public final class DataSet {


    private Activity activity;//上下文
    //核心成员属性
    private String label;//左上角小标题
    private List<ISheetColumnModel> columnModels = new ArrayList<>();//列数据
    private List<ISheetRowModel> rowModels = new ArrayList<>();//行数据

    private int sheetStyle;//显示风格，FULL, TOP, LEFT, NONE


    //辅助计算属性，重新设置行列时需要重新计算
    private DataSortHelper dataSortHelper;
    private int totalCellCounts;   //总的表格的单元格数量
    private int columnCount;       //列数
    private int rowCount;          //行数
    private int[] columnsWidth;//计算后每一列的宽度
    private int[] rowsHeight;//计算后每一行的高度
    private boolean averageCellRect; //是否根据宽度平铺
    private boolean enableSort;//排序总开关
    private int indexWidth; //index宽
    private int indexHeight;//index高

    private DataSet(Activity activity,Builder builder){
        this.activity = activity;

        if(builder == null)return;
        this.columnModels = builder.columnModel;
        this.rowModels = builder.rowModel;
        this.label = builder.label;
        this.averageCellRect = builder.averageCellRect;
        this.enableSort = builder.enableSort;
        this.indexWidth = builder.indexWidth;
        this.indexHeight = builder.indexHeight;
        sheetStyle = builder.sheetStyle;
        reset();

    }

    //根据行列，计算出必要的高度宽度行数列数
    private void reset(){
        this.rowCount = rowModels.size();
        this.columnCount = columnModels.size();
        this.totalCellCounts = rowCount*columnCount;
        this.dataSortHelper = new DataSortHelper(this);

        //平铺模式
        if(isAverageCellRect()){
            float cellWDp;
            float cellHDp;
            //重新计算宽高
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float sceenWidth = displayMetrics.widthPixels / displayMetrics.density + 0.5f;
            if(sheetStyle == InnerSheetView.STYLE_FULL || sheetStyle == InnerSheetView.STYLE_LEFT){
                //根据屏幕宽度，计算宽高`
                cellWDp = (sceenWidth- ISheetCellModel.DEFAULT_CELL_WIDTH ) / columnCount;
            }else{
                //没有侧边栏，直接用屏幕宽度除列数算出每列宽度
                cellWDp = sceenWidth / columnCount;
            }

            cellHDp = ISheetCellModel.DEFAULT_CELL_HEIGHT;


            //转换为像素
            int pxCellWidth = dip2px(cellWDp);
            int pxCellHeight = dip2px(cellHDp);

            if(columnCount!=0){
                this.columnsWidth = new int[columnCount];
                for(int i = 0;i < columnCount; i++) {
                    this.columnsWidth[i] = pxCellWidth;
                }
            }else{
                this.columnsWidth = new int[]{pxCellWidth};
            }

            if(rowCount!=0){
                this.rowsHeight = new int[rowCount];
                for(int j = 0;j < rowCount; j++) {
                    this.rowsHeight[j] = pxCellHeight;
                }
            }else{
                this.rowsHeight = new int[]{pxCellHeight};
            }

        }else{
            //非平铺模式
            //根据每个cloumnModel的宽度
            int pxCellWidth = dip2px(ISheetCellModel.DEFAULT_CELL_WIDTH);
            int pxCellHeight = dip2px(ISheetCellModel.DEFAULT_CELL_HEIGHT);

            if(columnCount!=0){
                this.columnsWidth = new int[columnCount];
                for(int i = 0;i < columnCount; i++) {
                    this.columnsWidth[i] = dip2px(columnModels.get(i).getColumnWidth());
                }
            }else{
                this.columnsWidth = new int[]{pxCellWidth};
            }

            if(rowCount!=0){
                this.rowsHeight = new int[rowCount];
                for(int j = 0;j < rowCount; j++) {
                    this.rowsHeight[j] = pxCellHeight;
                }
            }else{
                this.rowsHeight = new int[]{pxCellHeight};
            }
        }

    }

    /**
     * 功能描述：获取item需要显示的样式
     **/
    public int getItemViewType(int position){
        int columnCount = getColumnCount();
        int column = position % columnCount;
        ISheetColumnModel columnModel = getColumnModels().get(column);
        return columnModel.getViewType();
    }

//    public void updateCellRect(int position, String text) {
//
//        if(!isAverageCellRect()) //如果是固定cell大小的，不处理
//            return;
//
//        //special case
//        int row = position / columnCount;
//        int column = position % columnCount;
//
//        //2.update the cell rect
//        //just testing
//        int height = getTextHeight(text, 96, 15);
//        Log.d("Thomas", "text height is : " + height);
//
//        height += 60;//offset testing
//
//        if(rowsHeight[row] < height) {
//            rowsHeight[row] = height;
//        }
//
//        if(height > initCellHeight) {
//            rowsHeight[row] = height;
//        } else {/// 字体高度小于最小行高
//            if(rowsHeight[row] > initCellHeight) {//只有当前行高大于最小行高的时候，才改变
//                rowsHeight[row] = initCellHeight;
//            }
//        }
//    }

    public String getLabel(){
        return label;
    }

    public List<ISheetColumnModel> getColumnModels(){
        return columnModels;
    }

    public List<ISheetRowModel> getRowModels(){
        return rowModels;
    }

    public boolean isEnableSort() {
        return enableSort;
    }

    public void setEnableSort(boolean enableSort) {
        this.enableSort = enableSort;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getTotalCellCounts() {
        return totalCellCounts;
    }

    //是否平铺
    public boolean isAverageCellRect() {
        return averageCellRect;
    }

    public Activity getActivity() {
        return activity;
    }

    public int[] getColumnsWidth() {
        return columnsWidth;
    }

    public int getIndexWidth(){
        return dip2px(indexWidth);
    }

    public int getIndexHeight(){
        return dip2px(indexHeight);
    }

    public int[] getRowsHeight() {
        return rowsHeight;
    }

    public int getTextHeight(String text, int maxWidth, float textSize) {

        int maxWidthPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                maxWidth, activity.getResources().getDisplayMetrics());
        int textSizePX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textSize, activity.getResources().getDisplayMetrics());

        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        paint.setTextSize(textSizePX);
        paint.setTypeface(Typeface.DEFAULT);

        int lineCount = 0;

        int index = 0;
        int length = text.length();

        while(index < length - 1) {
            index += paint.breakText(text, index, length, true, maxWidthPX, null);
            lineCount++;
        }

        Rect bounds = new Rect();
        //通过两个特殊字符，获取bounds的高度！
        paint.getTextBounds("Py", 0, 2, bounds);

        int height = bounds.height();

        return (int)Math.floor(lineCount * height);
    }

    public DataSortHelper getDataSortHelper() {
        return dataSortHelper;
    }

    public int getSheetStyle() {
        return sheetStyle;
    }

    public static class Builder{

        public Builder(Activity activity){
            this.activity = activity;
        }

        public Activity activity;
        public String label;
        public boolean averageCellRect = false;
        public boolean enableSort = false;
        public List<ISheetColumnModel> columnModel = new ArrayList<>();
        public List<ISheetRowModel> rowModel = new ArrayList<>();
        public int sheetStyle = InnerSheetView.STYLE_FULL;
        public int indexWidth = ISheetCellModel.DEFAULT_CELL_WIDTH;
        public int indexHeight = ISheetCellModel.DEFAULT_CELL_HEIGHT;
        public DataSet build(){
            return new DataSet(activity,this);
        }

        public Builder setLabel(String label){
            this.label = label;
            return this;
        }

        public Builder setAverageCellRect(boolean averageCellRect){
            this.averageCellRect = averageCellRect;
            return this;
        }

        public Builder setColumnData(List<ISheetColumnModel> columnModel){
            this.columnModel.addAll(columnModel);
            return this;
        }

        public Builder setRowData(List<ISheetRowModel> rowModel){
            this.rowModel.addAll(rowModel);
            return this;
        }

        public Builder setSheetStyle(int sheetStyle){
            this.sheetStyle = sheetStyle;
            return this;
        }

        public Builder setEnableSort(boolean enableSort){
            this.enableSort = enableSort;
            return this;
        }

        public Builder setIndexWidth(int indexWidth){
            this.indexWidth = indexWidth;
            return this;
        }

        public Builder setIndexHeight(int indexHeight){
            this.indexHeight = indexHeight;
            return this;
        }

    }


    private int dip2px(float source){
        final float scale = activity.getResources().getDisplayMetrics().density;
        //return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, source, displayMetrics);
        return (int)(source * scale + 0.5f);
    }

}
