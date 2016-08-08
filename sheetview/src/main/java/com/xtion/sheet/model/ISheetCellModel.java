package com.xtion.sheet.model;


import com.xtion.sheet.cell.SheetCellFactory;

/**
 * Created by Administrator on 2015-9-29.
 */
public interface ISheetCellModel {
    //100 * 40 dp，我们以这个为最小的宽高来计算
    int DEFAULT_CELL_WIDTH = 100; //dp
    int DEFAULT_CELL_HEIGHT = 45; //dp
    int DEFAULT_CELL_VIEWTYPE = SheetCellFactory.TYPE_SIMPLE_TEXT;

    String getCellText();
}
