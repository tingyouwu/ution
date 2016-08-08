package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.wty.ution.R;
import com.wty.ution.widget.fieldlabel.IContentLimit;


public class ExpandContentTelephone extends ExpandContentBase {
	public final static int FieldType = 9;
	public ExpandContentTelephone(Context context) {
		super(context);
		setViewByType(Type_Text);
	}
	
	@Override
	public void setFieldValue(String value) {
		super.setFieldValue(value);
		setFieldReadOnly(isReadOnly);
	}
	
	@Override
	public IContentLimit setFieldReadOnly(boolean isReadOnly) {
		super.setFieldReadOnly(isReadOnly);
		if(isReadOnly){
			setPhoneButton();
			setMessageButton();
		}
		return this; 
	}
	
	public void setPhoneButton(){
		getMultiOptionLayout().setVisibility(View.VISIBLE);
		final String telStr = getFieldValue().replaceAll("[^0-9]","").trim();// 获取电话号码,过滤掉非数字字符
		if(TextUtils.isEmpty(telStr)){
			getMultiOption().setVisibility(View.GONE);
		}else{
			getMultiOption().setVisibility(View.VISIBLE);
			getMultiOption().setImageResource(R.drawable.img_contact_phone);
			getMultiOption().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if (telStr != null && !telStr.equals("")) {
						Uri uri = Uri.parse("tel:" + telStr); // 拨打电话号码的URI格式
						Intent it = new Intent(); // 实例化Intent
						it.setAction(Intent.ACTION_CALL); // 指定Action
						it.setData(uri); // 设置数据
						getContext().startActivity(it);
					}
				}
			});
		}
		
	}
	
	public void setMessageButton(){
		
	}
}
