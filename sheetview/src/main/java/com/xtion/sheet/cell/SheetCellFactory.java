package com.xtion.sheet.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;


/**
 * 功能描述：表格控件工厂
 */
public class SheetCellFactory {

    public static final int TYPE_SIMPLE_TEXT = 0;
    public static final int TYPE_LINK_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_CHECKBOX = 3;
    public static final int TYPE_EDIT_TEXT = 4;
    public static final int TYPE_EDIT_TEXT_IMAGE = 5;

    private static SheetCellFactory instance;

    public static SheetCellFactory getInstance(){
        if(instance == null)
            instance = new SheetCellFactory();
        return  instance;
    }

    private SheetCellFactory() {}

    public Cell createCell(int viewType, AlphaAdapter adapter, ViewGroup parent){
        Context context = parent.getContext();
        Cell cell;
        switch(viewType) {
//            case TYPE_IMAGE:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_image, parent,
//                        false);
//                viewHolder = new ImageCell(v, adapter);
//                break;

            case TYPE_SIMPLE_TEXT:
                cell = new TextCell(parent.getContext(), adapter);
                break;

//            case TYPE_LINK_TEXT:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent,
//                        false);
//                viewHolder = new LinkedCell(v, adapter);
//                break;
//
//            case TYPE_EDIT_TEXT:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_edittext, parent,
//                        false);
//                viewHolder = new EditTextCell(v, adapter);
//                break;
//
//            case TYPE_CHECKBOX:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_checkbox, parent,
//                        false);
//                viewHolder = new CheckBoxCell(v, adapter);
//                break;
            case TYPE_EDIT_TEXT_IMAGE:
                cell = new TextImageCell(context, adapter);
                break;
            default:
                cell = new TextCell(parent.getContext(), adapter);
                break;
        }
        return cell;
    }

}
