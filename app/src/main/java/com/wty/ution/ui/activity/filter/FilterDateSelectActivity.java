package com.wty.ution.ui.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.fieldlabel.LabelEditText;
import com.wty.ution.widget.filter.base.FilterDateOptionModel;
import com.wty.ution.widget.filter.base.FilterOptionModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Amberlo 筛选控件选择日期页面
 */
public class FilterDateSelectActivity extends BaseActivity {

    public final static int UI_EVENT_Select = 101010101;
    public final static String Tag_Fieldname = "Tag_Fieldname";
    public final static String Tag_Title =  "Tag_title";
    public final static String Tag_Begin = "Tag_Begin";
    public final static String Tag_End= "Tag_End";
    public final static String Tag_Selected = "Tag_Selected";

    private String fieldname;
    private LabelEditText let_begin,let_end;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_filterlabel_dateselect);
        init();
        keyboardControl(false,let_begin);

    }

    private String title;

	private void init() {

    	title = getIntent().getStringExtra(Tag_Title);
        fieldname = getIntent().getStringExtra(Tag_Fieldname);
        getDefaultNavigation().setTitle(title);
        getDefaultNavigation().setRightButton("确定",new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
        
        let_begin = (LabelEditText)findViewById(R.id.flb_dateselect_begin);
        let_end = (LabelEditText)findViewById(R.id.flb_dateselect_end);

        FilterOptionModel selected = (FilterOptionModel) getIntent().getSerializableExtra(Tag_Selected);
        if(selected!=null){

            	String[] value = selected.getValue().split(",");
                if(value.length == 1){
                    let_begin.setValue(value[0]);
                    let_end.setDefaultDate(new Date());
                    let_begin.setDate(value[0], CommonUtil.DataFormat_list_year);
                    let_end.setValue(CommonUtil.getTime());
                }else{
                    let_begin.setValue(value[0]);
                    let_end.setValue(value[1]);

                    let_begin.setDate(value[0], CommonUtil.DataFormat_list_year);
                    let_end.setDate(value[1], CommonUtil.DataFormat_list_year);
                }


        }else{
            	let_begin.setDefaultDate(new Date());
                let_end.setDefaultDate(new Date());
                
            	String now = CommonUtil.getTime();
            	let_begin.setValue(now);
            	let_end.setValue(now);
        }
    }

	@Override
	protected List<String> validate() {
		//控件标签，控件校验结果
        List<String> result = new ArrayList<String>();
        if(!let_begin.validate())result.add("请输入开始时间");
        if(!let_end.validate())result.add("请输入结束时间");
        
		int compare = CommonUtil.compare_date(let_end.getValue(), let_begin.getValue(),"yyyy-MM-dd");
		if(compare == -1){
			result.add("开始时间不能大于结束时间");
		}
        
        return result;
	}
	
	@Override
	protected boolean submit() {
		if(super.submit()){


            String begin = let_begin.getValue();
            String end = let_end.getValue();
            FilterOptionModel selected = FilterDateOptionModel.createDateSelectOptionModel(begin, end);

            Intent i = new Intent();
			i.putExtra(Tag_Selected, selected);
            if(!TextUtils.isEmpty(fieldname)){
                i.putExtra(Tag_Fieldname, fieldname);
            }
			setResult(RESULT_OK,i);
			finish();
    	}
    	return true;
	}


	
}