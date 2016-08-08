package com.wty.ution.widget.fieldlabel;

import com.wty.ution.data.dalex.FieldDescriptDALEx;

public interface IContentBase {

	/** 获取值 */
	public String getFieldValue();

	/** 设置值 */
	public void setFieldValue(String value);

    /** 清除值 */
    public void clearFieldValue();

	/** 设置hint值*/
	public void setFieldHint(String hint);
	
	/** 字段校验 */
	public String fieldValidate();
	
	/** 绑定描述信息 */
	public void setFieldDescript(FieldDescriptDALEx descript);
	
	/** 绑定描述信息 */
	public FieldDescriptDALEx getFieldDescript();
}
