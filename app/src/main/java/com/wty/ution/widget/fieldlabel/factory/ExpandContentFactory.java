package com.wty.ution.widget.fieldlabel.factory;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.fieldlabel.ContentDivide;
import com.wty.ution.widget.fieldlabel.IContent;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentDate;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentDateTime;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentIndustry;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentMobilePhone;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentMultiSelect;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentRegion;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentSingleSelect;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTelephone;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentText;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextArea;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextDecimal;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextInteger;


/**
 * @author wty
 * 具体工厂
 */
public class ExpandContentFactory extends AbstractFactory{

    @Override
    public IContent initByType(Context context,FieldDescriptDALEx descript){
        try {
            IContent let = initCotentOther(descript, context) ;
            if(let== null){

                switch (descript.getXwcontroltype()) {
                    case ExpandContentText.FieldType:
                        //Text = 1 文本
                        let = new ExpandContentText(context);
                        break;
                    case ExpandContentSingleSelect.FieldType:
                        //Select = 2 单选
                        let = new ExpandContentSingleSelect(context);
                        break;
                    case ExpandContentMultiSelect.FieldType:
                        //MultiSelect=3 多选
                        let = new ExpandContentMultiSelect(context);
                        break;
                    case ExpandContentTextArea.FieldType:
                        //TextArea = 4 大文本
                        let = new ExpandContentTextArea(context);
                        break;
                    case ExpandContentTextInteger.FieldType:
                        //Integer = 5 整数
                        let = new ExpandContentTextInteger(context);
                        break;
                    case ExpandContentTextDecimal.FieldType:
                        //Decimal = 6 实数（保留三位小数）
                        let = new ExpandContentTextDecimal(context);
                        break;
                    case ExpandContentDate.FieldType:
                        //Date = 7 日期
                        let = new ExpandContentDate(context);
                        break;
                    case ExpandContentDateTime.FieldType:
                        //DateTiem =8 日期时间
                        let = new ExpandContentDateTime(context);
                        break;
                    case ExpandContentTelephone.FieldType:
                        //Mobile = 9 手机号
                        let = new ExpandContentMobilePhone(context);
                        break;
                    case ContentDivide.FieldType:
                        //Divider = 10 分组
                        //什么都不干
                        break;
                    case ExpandContentRegion.FieldType:
                        //Region = 11 行政区域
                        let = new ExpandContentRegion(context);
                        break;
                    case ExpandContentIndustry.FieldType:
                        //Industry = 15 行业
                        let = new ExpandContentIndustry(context);
                        break;
//                    case ExpandContentAddress.FieldType:
//                        //Address = 16 地址
//                        let = new ExpandContentAddress(context);
//                        break;
//                    case ExpandContentOther.FieldType:
//                        //Other = 17 其他
//                        let = initCotentOther(descript, context);
//                        break;
//                    case ExpandContentCustLevel.FieldType:
//                        //Other = 18 客户等级
//                        let = new ExpandContentCustLevel(context);
//                        break;
                    default:
                        //Other = 18以外 其他
                        let = new ExpandContentText(context);
                        break;
                }
            }

            let.setFieldDescript(descript);
            return let;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 特殊处理的控件
     */
    private static IContent initCotentOther(FieldDescriptDALEx descript,Context context){
        String fieldname = descript.getXwfieldname();
        IContent let = null;
        return let;
    }

}
