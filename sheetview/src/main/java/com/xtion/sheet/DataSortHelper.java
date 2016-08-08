package com.xtion.sheet;

import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetColumnModel.ColumnType;
import com.xtion.sheet.model.ISheetRowModel;
import com.xtion.sheet.util.CommonUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataSortHelper {
    private DataSet dataSet;
    private Map<ColumnType,Comparator> comparatorMap = new LinkedHashMap<ColumnType,Comparator>();;

    DataSortHelper(DataSet dataSet){
        this.dataSet = dataSet;
    }

    private Sort sortMode = Sort.None;

    public void sortData(ISheetColumnModel columModel){
        if(sortMode == Sort.None){
            sortMode = Sort.Asc;
        }else if(sortMode == Sort.Desc){
            sortMode = Sort.Asc;
        }else if(sortMode == Sort.Asc){
            sortMode = Sort.Desc;
        }
        sortData(sortMode,columModel);
    }

    /**
     * 根据列值排序行
     * */
    private void sortData(Sort sortMode,ISheetColumnModel columModel){
        List<ISheetRowModel> rowModels = dataSet.getRowModels();
        if(rowModels.size()==0 || rowModels.size()==1)return;

        Comparator comparator = comparatorMap.get(columModel.getColumnType());

        if(comparator == null){
            if(columModel.getColumnType()==ColumnType.DATE){
                comparator = new RowDateComparator(columModel.getColumnId(),sortMode);
            }else if(columModel.getColumnType()==ColumnType.STRING){
                comparator = new RowStringComparator(columModel.getColumnId(),sortMode);
            }else {
                comparator = new RowIntComparator(columModel.getColumnId(),sortMode);
            }
            comparatorMap.put(columModel.getColumnType(),comparator);
        }
        if(sortMode == Sort.Asc) {
            Collections.sort(rowModels, comparator);
        }else{
            Collections.reverse(rowModels);
        }
    }

    enum Sort{
        Asc,Desc,None
    }

    public class RowIntComparator implements Comparator<ISheetRowModel> {
        String columnid;
        Sort sortMode;

        RowIntComparator(String columnid,Sort sortMode){
            this.columnid = columnid;
            this.sortMode = sortMode;
        }

        @Override
        public int compare(ISheetRowModel lhs, ISheetRowModel rhs) {
            String value1 = lhs.getCellValue().get(columnid);
            String value2 = rhs.getCellValue().get(columnid);
            if(value1==null || value2 == null)return 0;

            if(value1.endsWith("%"))value1 = value1.replace("%","");
            if(value2.endsWith("%"))value2 = value2.replace("%","");
            try {

                int result = Double.valueOf(value1).compareTo(Double.valueOf(value2));
                if(sortMode==Sort.Asc)
                    return result;
                else
                    return result>=0?-1:1;
            }catch (Exception e){
                return 0;
            }
        }
    }

    public class RowDateComparator implements Comparator<ISheetRowModel> {
        String columnid;
        Sort sortMode;
        RowDateComparator(String columnid,Sort sortMode){
            this.columnid = columnid;
            this.sortMode = sortMode;

        }

        @Override
        public int compare(ISheetRowModel lhs, ISheetRowModel rhs) {
            String value1 = lhs.getCellValue().get(columnid);
            String value2 = rhs.getCellValue().get(columnid);
            if(value1==null || value2 == null)return 0;

            try {
                int result = CommonUtil.compare_date(value1,value2,CommonUtil.DataFormat_list_year);
                if(sortMode==Sort.Asc)
                    return result;
                else
                    return result>=0?-1:1;
            }catch (Exception e){
                return 0;
            }
        }
    }

    public class RowStringComparator implements Comparator<ISheetRowModel> {
        String columnid;
        Sort sortMode;


        RowStringComparator(String columnid,Sort sortMode){
            this.columnid = columnid;
            this.sortMode = sortMode;

        }

        @Override
        public int compare(ISheetRowModel lhs, ISheetRowModel rhs) {
            String value1 = lhs.getCellValue().get(columnid);
            String value2 = rhs.getCellValue().get(columnid);
            if(value1==null || value2 == null)return 0;

            if(value1.endsWith("%"))value1 = value1.replace("%","");
            if(value2.endsWith("%"))value2 = value2.replace("%","");
            try {
                int result = Double.valueOf(value1).compareTo(Double.valueOf(value2));
                if(sortMode==Sort.Asc)
                    return result;
                else
                    return result>=0?-1:1;
            }catch (Exception e){
                return 0;
            }
        }
    }
}