package com.wty.ution.widget.filter.view;

import android.content.Context;
import android.text.TextUtils;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.filter.base.AbstractFilterModel;

/**
 * Created by apple on 15/12/21.
 */
public class ExpandFilterTextModel extends AbstractFilterModel {

    public final static int FieldType = 1;

    public ExpandFilterTextModel(Context mContext, FieldDescriptDALEx descript){
        super(mContext, descript,InputMode.Text);
    }

    public ExpandFilterTextModel(Context mContext, FieldDescriptDALEx descript, boolean isMulti){
        super(mContext, descript,isMulti?InputMode.TextArea:InputMode.Text);
    }

    @Override
    public String toSql() {
        if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(filterid)){
            return "";
        }else{
            return filterid +" like '%"+ value +"%' ";
        }
    }
}
