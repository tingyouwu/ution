package com.wty.ution.widget.fieldlabel.expandcontent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;


import com.wty.ution.base.AppContext;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.FieldLabelMultiChoiceActivity;
import com.wty.ution.widget.OnActivityResultListener;

import java.util.List;

@SuppressLint("DefaultLocale")
public class ExpandContentMultiSelect extends ExpandContentBase {
	public final static int FieldType = 3;
	private String selected;
	OnActivityResultListener onActivityResultListener;
	public ExpandContentMultiSelect(Context context) {
		super(context);
		setClickMode(true);
		setViewByType(Type_MutipleChoice);
		int requestCode = AppContext.Constant.ActivityResult_FieldLabel_MultiSelect;
		final BaseActivity activity = (BaseActivity) context;
		onActivityResultListener = new OnActivityResultListener(activity,requestCode) {
			
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {
				if(resultCode == Activity.RESULT_OK){
					String fieldname = data.getStringExtra(FieldLabelMultiChoiceActivity.Fieldname);
					if(TextUtils.isEmpty(fieldname))return;
					if(resultCode == Activity.RESULT_OK && descript.getXwfieldname().equals(fieldname)){
						String selected = data.getStringExtra(FieldLabelMultiChoiceActivity.Selected);
						setFieldValue(selected);
					}
				}
			}
		};
		activity.registerOnActivityResultListener(onActivityResultListener);
		
		setContentListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getContext(),FieldLabelMultiChoiceActivity.class);
				intent.putExtra(FieldLabelMultiChoiceActivity.Title ,getLabel());
				intent.putExtra(FieldLabelMultiChoiceActivity.Options,descript.getXwoptional());
				intent.putExtra(FieldLabelMultiChoiceActivity.Selected,getFieldValue());
				intent.putExtra(FieldLabelMultiChoiceActivity.Fieldname, descript.getXwfieldname());
				onActivityResultListener.startActivity(intent);
			}
		});
	}
	
	@Override
	protected String makeMessagePrefix() {
		return "请选择"; 
	}
	
	@Override
	public String getFieldValue() {
		if(this.selected == null)return "";
		return this.selected; 
	}
	
	@Override
	public void setFieldValue(String value) {
		this.selected = value;
		if(TextUtils.isEmpty(value)){
			edt_content.setText("");
			tv_content.setText("");
			return;
		}
		//value的形式为"选项1|选项2|选项3"
		String[] array = cropValue(value);
		String text ;
		if(array.length>1){
			 text = String.format("%s等%d个选项", new Object[]{array[0],array.length});
		}else{
			 text = value;
		}
		edt_content.setText(text);
		tv_content.setText(TextUtils.join(",", array));
	}
	
	public static String[] cropValue(String value){
		if(TextUtils.isEmpty(value)){
			return new String[]{};
		}
		String[] array = value.split("\\|");
		return array;
	}
	
	public static String wrapValue(List<String> selected){
		if(selected!=null && selected.size()!=0){
			return TextUtils.join("|", selected);
		}else{
			return "";
		}
	}
	
	@Override
	public void setFieldDescript(FieldDescriptDALEx descript) {
		super.setFieldDescript(descript);
		this.setMaxLength(99);
	}

    @Override
    public void clearFieldValue() {
        super.clearFieldValue();
        selected = null;
    }
}
