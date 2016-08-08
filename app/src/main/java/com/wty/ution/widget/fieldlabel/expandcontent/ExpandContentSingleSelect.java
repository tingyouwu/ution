package com.wty.ution.widget.fieldlabel.expandcontent;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;


import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.widget.MenuDialog;
import com.wty.ution.widget.fieldlabel.LabelEditText;
import com.wty.ution.widget.fieldlabel.SingleOptionItem;

import java.util.ArrayList;
import java.util.List;

public class ExpandContentSingleSelect extends ExpandContentBase {
	public final static int FieldType = 2;
	/** 是否忽略配置的选项 */
	private boolean disableParentOptions = false;
	protected List<SingleOptionItem> selectItems = new ArrayList<SingleOptionItem>();
	public ExpandContentSingleSelect(Context context) {
		super(context);
		setViewByType(LabelEditText.Type_SingleChoice);
		setClickMode(true);
		setContentListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showOptionList(selectItems);
			}
		});
	}
	
	@Override
	public void setFieldDescript(FieldDescriptDALEx descript) {
		super.setFieldDescript(descript);
		if(disableParentOptions)return;
		if(!TextUtils.isEmpty(descript.getXwoptional())){
			String[] ops = descript.getXwoptional().split("\\|");
			if(descript.getXwisallowempty()==1)
				selectItems.add(new SingleOptionItem("无","",""));
			for (String o : ops) {
				selectItems.add(new SingleOptionItem(o,o,o));
			}
		}
	}
	
	@Override
	protected String makeMessagePrefix() {
		return "请选择";
	}
	
	@Override
	public void setFieldValue(String id) {
		super.setFieldValue(getOptionTextById(id));
	}
	
	public void setFieldValue(SingleOptionItem option){
		boolean contains = false;
		for(SingleOptionItem item:selectItems){
			if(item.getId().equals(option.getId()))contains = true;
		}
		if(!contains)selectItems.add(option);
		super.setFieldValue(option.getText());
	}
	
	@Override
	public String getFieldValue() {
		return getOptionIdByText(edt_content.getEditableText().toString());
	}
	
	public void setDisableParentOptions(boolean disable){
		this.disableParentOptions = disable;
	}
	
	private String getOptionTextById(String id){
		String value = "";
		for(SingleOptionItem item: selectItems){
			if(item.getId().equals(id)){
				value = item.getText();
			}
		}
		return value;
	}
	
	private String getOptionIdByText(String text){
		String result = "";
		for(SingleOptionItem item: selectItems){
			if(item.getText().equals(text)){
				result = item.getId();
			}
		}
		return result;
	}
	
	/**
     * 弹出选择菜单
     */
    private void showOptionList(final List<SingleOptionItem> items) {
    	if(items==null || items.size() == 0)return;
        final Context dialogContext = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Light);
        
        List<String> optext = new ArrayList<String>(); 
        for(SingleOptionItem option:selectItems){
        	optext.add(option.getOption());
        }
        
        MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
        MenuDialog dialog = builder
//                .setTitle("请选择")
                .setItems(optext.toArray(new String[optext.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final String id = selectItems.get(which).getId();
                        if(onOptionSelectedListener!=null){
                        	if(!onOptionSelectedListener.onSelected(selectItems.get(which).getOption(),selectItems.get(which).getId())){
                        		setFieldValue(id);
                        	}
                        }
                        else{
                        	setFieldValue(id);
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}