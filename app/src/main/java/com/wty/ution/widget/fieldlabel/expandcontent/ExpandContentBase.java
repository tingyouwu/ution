package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.fieldlabel.IContent;
import com.wty.ution.widget.fieldlabel.IContentLimit;
import com.wty.ution.widget.fieldlabel.LabelEditText;


public class ExpandContentBase extends LabelEditText implements IContent {
	
	private boolean clickMode = false;
	protected FieldDescriptDALEx descript;
	
	public ExpandContentBase(Context context) {
		super(context);
	}
	
	
	@Override
	public String fieldValidate(){
		boolean maxLengthValidate = getEditText().getText().toString().length() <= descript.getXwfieldlength();
		String defaultValidate = validate()?"":makeMessagePrefix()+getLabel();
		if(isVcardmode() && !maxLengthValidate ){
			//在VCard模式下，输入超过最大长度
			defaultValidate = getLabel()+"最长"+descript.getXwfieldlength()+"个字符";
		}
		return defaultValidate;
	}

	@Override
	public String getFieldValue(){
		return super.getValue();
	}
	
	@Override
	public void setFieldValue(String value){
		super.setValue(value);
	}

    @Override
    public void clearFieldValue() {
        super.clearValue();
    }

    @Override
	public void setFieldDescript(FieldDescriptDALEx descript){
		this.descript = descript;
		setIsRequired(descript.getXwisallowempty()==0?true:false);
		setLabel(descript.getXwfieldlabel());
		setHint(makeMessagePrefix()+descript.getXwfieldlabel());
//        setFieldReadOnly(descript.getXwisreadonly()==1);
		//扩展字段才限制
		if(!clickMode){
            setFieldMaxLength(descript.getXwfieldlength());
		}
	}
	
	
	/**
	 * validate、hint前缀,输入框时显示“请输入xxx”，选择框时显示“请选择”
	 * @return
	 */
	protected String makeMessagePrefix(){
		if(isVcardmode())
			return "请选择";
		return "请输入";
	}

	
	
	/** 设置限制 */
	
	@Override
	public IContentLimit setFieldReadOnly(boolean isReadOnly) {
		setIsReadOnly(isReadOnly);
		return this;
	}

	@Override
	public IContentLimit setFieldMaxLength(int length) {
		setMaxLength(length);
		return this;
	}


	@Override
	public IContentLimit setFieldAllowEmpty(boolean isAllowEmpty) {
		setIsRequired(!isAllowEmpty);
		return this;
	}


	public boolean isClickMode() {
		return clickMode;
	}


	public void setClickMode(boolean clickMode) {
		this.clickMode = clickMode;
	}
	
	public Object clone() {   
        try {   
            return super.clone();   
        } catch (CloneNotSupportedException e) {   
            return null;   
        }   
    }


	@Override
	public FieldDescriptDALEx getFieldDescript() {
		return descript;
	}
	
	@Override
	public boolean validate() {
		boolean defaultValideta = super.validate();
		if(defaultValideta && isVcardmode()){
			//名片扫描模式,输入框不限制长度，但提交前要校验输入长度
			defaultValideta = getEditText().getText().toString().length() <= descript.getXwfieldlength();
		}
		return defaultValideta;
	}


	@Override
	public void setFieldHint(String hint) {
		setHint(hint);
	}
}
