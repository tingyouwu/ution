package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;

import java.text.DecimalFormat;

public class ExpandContentTextDecimal extends ExpandContentBase {
	public final static int FieldType = 6;

	private static final int MaxEndLength = 4; 	//小数点后最大位数
	private static final int MaxLength = 13; //总长度
	public ExpandContentTextDecimal(Context context) {
		super(context);
		edt_content.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
		edt_content.addTextChangedListener(watcher);
	}
	
	@Override
	public void setFieldValue(String value) {
		if(isReadOnly){
			super.setFieldValue(fixValue(value));
		}else{
			super.setFieldValue(value);
		}
	};
	
	private String fixValue(String value){
		if(TextUtils.isEmpty(value)){
			return "";
		}
		double f = Double.parseDouble(value);
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("#.####");// 将格式应用于格式化器
		return df.format(f);
	}
	
	 TextWatcher watcher = new TextWatcher(){
		
		String beforeContent = "";
		int beforeSelection = 0;
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			this.beforeContent = s.toString();
			this.beforeSelection = edt_content.getSelectionStart()-1;
		}

		@Override
		// 主要是重置文本改变事件,判断当前输入的内容
		public void afterTextChanged(Editable s) {}
		
		@Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
//			String regx = "^[0-9]{0,9}\\.[0-9]{0,4}$|(^[0-9]{1,13}$)";
//			boolean matches = CommonUtil.regxMatches(s.toString(), regx);
//			
//			if(!matches){
//				
//				edt_content.setText(beforeContent);
//            	edt_content.setSelection(beforeSelection);
//			}
			
        	//带小数点
            if (s.toString().contains(".")) {
            	
                if (s.length() - 1 - s.toString().indexOf(".") > MaxEndLength) {
                	//小数点后超出长度
                	edt_content.setText(beforeContent);
                	System.out.println(beforeSelection);
                	edt_content.setSelection(beforeSelection);
                    return;
                }else if(s.length() - 1 >MaxLength){
                	edt_content.setText(beforeContent);
                	edt_content.setSelection(beforeSelection);
                }
                
            }
            else if(s.length() > MaxLength){
            	//不带小数点超过长度
            	edt_content.setText(beforeContent);
            	edt_content.setSelection(beforeSelection);
            }
         
        }
	};
}