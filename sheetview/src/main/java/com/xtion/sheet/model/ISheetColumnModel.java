package com.xtion.sheet.model;



/**
 * Created by Administrator on 2015-9-29.
 */
public class ISheetColumnModel implements ISheetCellModel {

    private String text;//显示的中文
    private String columnid;//对应的关键id
    private int viewType = DEFAULT_CELL_VIEWTYPE;//显示的viewholder
    private int columnWidth = DEFAULT_CELL_WIDTH;
    ColumnType columnType;//排序用到
    private boolean isEnabelSort = false;


    public ISheetColumnModel(String text, String rowid){
        this.text = text;
        this.columnid = rowid;
    }

    public ISheetColumnModel(String text, String rowid,ColumnType columnType,boolean isEnabelSort){
        this.text = text;
        this.columnid = rowid;
        this.columnType = columnType;
        this.isEnabelSort = isEnabelSort;
    }

    public ISheetColumnModel(String text, String rowid,int columnWidth){
        this.text = text;
        this.columnid = rowid;
        this.columnWidth = columnWidth;
    }

    public ISheetColumnModel(String text, String rowid,int columnWidth,int viewType){
        this.text = text;
        this.columnid = rowid;
        this.columnWidth = columnWidth;
        this.viewType = viewType;
    }

    @Override
    public String getCellText() {
        return text;
    }

    public String getColumnId(){
        return columnid;
    }

    public int getColumnWidth(){
        return columnWidth;
    }

    public int getViewType(){
        return viewType;
    }

    public boolean isEnabelSort() {
        return isEnabelSort;
    }

    public ColumnType getColumnType(){
        return columnType;
    }

    public enum ColumnType{
        INT,DATE,STRING
    }
}
