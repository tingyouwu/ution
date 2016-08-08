package com.xtion.sheet.model;

import java.util.Map;

/**
 * Created by Administrator on 2015-9-29.
 */
public interface ISheetRowModel extends ISheetCellModel {
    public Map<String,String> getCellValue();

}
