package com.wty.ution.widget.filter.base;

import java.io.Serializable;

/**
 * 功能描述：过滤选项抽象模型
 **/
public class FilterOptionModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
    /**
     * 显示出来的值
     **/
    protected String text;
    /**
     * 过滤时使用的参数
     **/
	protected String value;
	
	public FilterOptionModel(String text, String value) {
		this.text = text;
		this.value = value;
	}


    public String getText(){
        return text;
    }

    public String getValue(){
        return value;
    }

}