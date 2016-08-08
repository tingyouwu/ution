package com.wty.ution.widget.fieldlabel;


public interface IContentLimit {
	
	/** 是否只读 */
	public IContentLimit setFieldReadOnly(boolean isReadOnly);
	
	/** 设置最大长度 */
	public IContentLimit setFieldMaxLength(int length);
	
	/** 设置最大长度 */
	public IContentLimit setFieldAllowEmpty(boolean isAllowEmpty);
}
