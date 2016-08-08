package com.wty.ution.ui.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;


import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.widget.fieldlabel.LabelEditText;
import com.wty.ution.widget.filter.base.FilterRangeOptionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amberlo 筛选控件范围选择页面
 */
public class FilterRangeActivity extends BaseActivity {

    public final static String Tag_Title =  "Tag_title";
    public final static String Tag_Label =  "Tag_label";
    public final static String Tag_Selected = "Tag_Selected";

    public final static String Tag_InputMode = "Tag_InputMode";

    public final static String Tag_Field = "Tag_Field";

    private LabelEditText let_begin,let_end;

    String label;
    String title;
    String field;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_filterlabel_range);
        init();
        
    }


	private void init() {
        int inputMode = getIntent().getIntExtra(Tag_InputMode, LabelEditText.Type_Int);
        title = getIntent().getStringExtra(Tag_Title);
        label = getIntent().getStringExtra(Tag_Label);
        field = getIntent().getStringExtra(Tag_Field);
        getDefaultNavigation().setTitle(title);
        getDefaultNavigation().setRightButton("确定", new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                submit();
            }
		});
        
        let_begin = (LabelEditText)findViewById(R.id.flb_range_begin);
        let_end = (LabelEditText)findViewById(R.id.flb_range_end);

        if(inputMode == LabelEditText.Type_Decimal){
            let_begin.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            let_end.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }else if(inputMode == LabelEditText.Type_Money){
            let_begin.setViewByType(LabelEditText.Type_Money);
            let_end.setViewByType(LabelEditText.Type_Money);
        }else{
            let_begin.setViewByType(LabelEditText.Type_Int);
            let_end.setViewByType(LabelEditText.Type_Int);
        }

        let_begin.setLabel(label);
        let_begin.setHint("请输入最小"+label);
        let_end.setHint("请输入最大"+label);
        try {
            FilterRangeOptionModel selected = (FilterRangeOptionModel) getIntent().getSerializableExtra(Tag_Selected);
            if(selected!=null){
                let_begin.setValue(selected.minValue);
                let_end.setValue(selected.maxValue);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
    }

	@Override
	protected List<String> validate() {
		//控件标签，控件校验结果
        List<String> result = new ArrayList<String>();
        if(TextUtils.isEmpty(let_begin.getValue())  && TextUtils.isEmpty(let_end.getValue())){
        	result.add("输入"+label);
        }else{ 
        	String begin = let_begin.getValue();
        	String end = let_end.getValue();
        	
        	if( !TextUtils.isEmpty(let_begin.getValue())   
    			&& !TextUtils.isEmpty(let_end.getValue()) 
    			&&  Double.valueOf(end)< Double.valueOf(begin)){
        		
        		result.add("输入"+label+"范围不正确，请重新输入");
        	}
        }
        
        return result;
	}
	
	@Override
	protected boolean submit() {
		if(super.submit()){
            Intent i = new Intent();

			String begin = let_begin.getValue();
			String end = let_end.getValue();

            FilterRangeOptionModel selected = FilterRangeOptionModel.create(begin,end);
            i.putExtra(Tag_Field, field);
			i.putExtra(Tag_Selected, selected);
			setResult(RESULT_OK,i);
			finish();
    	}
    	return true;
	}
	
}