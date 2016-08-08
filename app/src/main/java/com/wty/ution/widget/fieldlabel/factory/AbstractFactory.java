package com.wty.ution.widget.fieldlabel.factory;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.fieldlabel.IContent;

/**
 * @author wty
 * 功能描述：抽象工程  对应抽象icontent
 */
public abstract class AbstractFactory {
    public abstract IContent initByType(Context context,FieldDescriptDALEx descript);
}
