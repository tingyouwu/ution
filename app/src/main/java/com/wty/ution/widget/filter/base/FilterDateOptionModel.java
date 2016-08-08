package com.wty.ution.widget.filter.base;


import com.wty.ution.util.CommonUtil;

/**
 * Created by apple on 16/2/22.
 */
public class FilterDateOptionModel extends FilterOptionModel{
    private String begin;
    private String end;
    private FilterDateOptionModel(String text, String value) {
        super(text, value);
    }

    public static FilterDateOptionModel createDateSelectOptionModel(String begin,String end){

        begin = CommonUtil.dateToFormat(CommonUtil.DataFormat_list_year, begin);
        end = CommonUtil.dateToFormat(CommonUtil.DataFormat_list_year,end);

        String text = begin+" 至 "+end;
        String value = begin+","+end;
        if(begin.equals(end)){
            text = begin;
        }
        FilterDateOptionModel model = new FilterDateOptionModel(text,value);
        model.setBegin(begin);
        model.setEnd(end);
        return model;
    }

    public static FilterDateOptionModel createMonthSelectOptionModel(String begin,String end){

        String beginValue = CommonUtil.getFirstDay(begin);
        String endValue = CommonUtil.getLastDay(end);

        String beginText = CommonUtil.dateToFormat("yyyy-MM", begin);
        String endText = CommonUtil.dateToFormat("yyyy-MM", end);

        String value = beginValue+","+endValue;
        String text = beginText+" 至 "+endText;

        if(CommonUtil.getMonth(begin).equals(CommonUtil.getMonth(end))){
            text = beginText;
        }

        FilterDateOptionModel model = new FilterDateOptionModel(text,value);
        model.setBegin(beginValue);
        model.setEnd(endValue);
        return model;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }
}
