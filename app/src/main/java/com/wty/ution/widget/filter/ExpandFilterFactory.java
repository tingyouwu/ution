package com.wty.ution.widget.filter;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.fieldlabel.ContentDivide;
import com.wty.ution.widget.fieldlabel.LabelEditText;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentDate;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentDateTime;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentIndustry;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentMultiSelect;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentRegion;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentSingleSelect;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTelephone;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentText;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextArea;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextDecimal;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextInteger;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.view.ExpandFilterDateModel;
import com.wty.ution.widget.filter.view.ExpandFilterMulSelectModel;
import com.wty.ution.widget.filter.view.ExpandFilterRangeModel;
import com.wty.ution.widget.filter.view.ExpandFilterRegionModel;
import com.wty.ution.widget.filter.view.ExpandFilterSelectModel;
import com.wty.ution.widget.filter.view.ExpandFilterTextModel;


/**
 * 功能描述：筛选过滤字段工厂
 * @author wty
 */
public class ExpandFilterFactory {

    public static AbstractFilterModel initModel(Context context,FieldDescriptDALEx descript){

        try {

            AbstractFilterModel model = initOther(descript, context) ;
            if(model== null){

                switch (descript.getXwcontroltype()) {
                    case ExpandFilterTextModel.FieldType:
                        //Text = 1 文本
                        model = new ExpandFilterTextModel(context,descript);
                        break;
                    case ExpandFilterSelectModel.FieldType:
                        //Select = 2 单选
                        model = new ExpandFilterSelectModel(context,descript);
                        break;
                    case ExpandContentMultiSelect.FieldType:
                        //MultiSelect=3 多选
                        model = new ExpandFilterMulSelectModel(context,descript);
                        break;
                    case ExpandContentTextArea.FieldType:
                        //TextArea = 4 大文本
                        model = new ExpandFilterTextModel(context,descript,true);
                        break;
                    case ExpandContentTextInteger.FieldType:
                        //Integer = 5 整数
                        model = new ExpandFilterRangeModel(context,descript);
                        break;
                    case ExpandContentTextDecimal.FieldType:
                        //Decimal = 6 实数（保留三位小数）
                        model = new ExpandFilterRangeModel(context,descript, LabelEditText.Type_Decimal);
                        break;
                    case ExpandContentDate.FieldType:
                        //Date = 7 日期
                        model = new ExpandFilterDateModel(context,descript);
                        break;
                    case ExpandContentDateTime.FieldType:
                        //DateTiem =8 日期时间
                        model = new ExpandFilterDateModel(context,descript);
                        break;
                    case ExpandContentTelephone.FieldType:
                        //Mobile = 9 手机号
                        model = new ExpandFilterTextModel(context,descript);
                        break;
                    case ContentDivide.FieldType:
                        //Divider = 10 分组
                        //什么都不干
                        break;
                    case ExpandContentRegion.FieldType:
                        //Region = 11 行政区域
                        model = new ExpandFilterRegionModel(context,descript);
                        break;
//                    case ExpandContentProduct.FieldType:
//                        //Product = 12 产品
//                        model = new ExpandFilterProductModel(context,descript);
//                        break;
//                    case ExpandContentSellStage.FieldType:
//                        //Product = 13 销售阶段
//                        model = new ExpandFilterSellStageModel(context,descript);
//                        break;
//                    case ExpandContentCustScale.FieldType:
//                        //CustScale = 14 客户规模
//                        model = new ExpandFilterScaleModel(context,descript);
//                        break;
//                    case ExpandContentIndustry.FieldType:
//                        //Industry = 15 行业
//                        model = new ExpandFilterIndustryModel(context,descript);
//                        break;
//                    case ExpandContentAddress.FieldType:
//                        //Address = 16 地址
//                        model = new ExpandFilterTextModel(context,descript);
//                        break;
//                    case ExpandContentOther.FieldType:
//                        //Other = 17 其他
//                        model = initOther(descript, context);
//                        break;
//                    case ExpandContentCustLevel.FieldType:
//                        //Other = 18 客户等级
//                        model = new ExpandFilterLevelModel(context,descript);
//                        break;
                    default:
                        //Other = 18以外 其他
                        model = initOther(descript,context);
                        if(model==null)model = new ExpandFilterTextModel(context,descript);
                        break;
                }
            }

            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 特殊处理的控件
     */
    private static AbstractFilterModel initOther(FieldDescriptDALEx descript,Context context){
        AbstractFilterModel model = null;
        String fieldname = descript.getXwfieldname();
        return model;
    }

}
