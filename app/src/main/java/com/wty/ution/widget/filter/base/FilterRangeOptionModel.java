package com.wty.ution.widget.filter.base;

import android.text.TextUtils;

/**
 * Created by apple on 16/2/22.
 */
public class FilterRangeOptionModel extends FilterOptionModel{

    public String minValue;
    public String maxValue;

    private FilterRangeOptionModel(String text, String value) {
        super(text, value);
    }

    public static FilterRangeOptionModel create(String minValue,String maxValue){
        String text;
        String value;

        if(TextUtils.isEmpty(minValue) || TextUtils.isEmpty(maxValue)){
            //输入其中一个框为空
            if(!TextUtils.isEmpty(minValue)){
                text = minValue;
                value = minValue;
            }else{
                text = maxValue;
                value = maxValue;
            }

        }else{
            text = minValue+" 至 "+maxValue;
            value = minValue+","+maxValue;
            if(minValue.equals(maxValue)){
                text = maxValue;
            }
        }
        FilterRangeOptionModel model = new FilterRangeOptionModel(text,value);
        model.minValue = minValue;
        model.maxValue = maxValue;
        return model;
    }

}
