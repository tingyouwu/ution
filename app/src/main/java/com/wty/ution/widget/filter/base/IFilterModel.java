package com.wty.ution.widget.filter.base;

import java.io.Serializable;


/**
 * @author Amberllo
 *	筛选对象model
 */
public interface IFilterModel extends Serializable{

	/**
	 * 输入模式
	 * @return
	 */
	InputMode getInputMode();
	
	/**
	 * 筛选id
	 * @return
	 */
	String getFilterId();
	
	/**
	 * 标题
	 * @return
	 */
	String getLabel();
	
	/**
	 * 筛选获取值
	 * @return
	 */
	String getValue();
	
	/**
	 * 设置值
	 * @param value
	 */
	void setValue(String value);
	
	/**
	 * 筛选显示值
	 * @return
	 */
	String getTextValue();

	/**
	 * 设置值
	 * @return
	 */
	void setTextValue(String textValue);

	/**
	 * 选中筛选事件
	 * @return
	 */
	void onSelect(SelectCallback callback);
	
	/**
	 * 将值转换成sql语句
	 * @return
	 */
	String toSql();

	void clearValue();

	enum InputMode{
		Text,TextArea,Select,MultiInput
	}
	
	interface SelectCallback{
		void onResult(FilterOptionModel result);
	}
}
