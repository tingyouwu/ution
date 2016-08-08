package com.wty.ution.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.wty.ution.R;
import com.wty.ution.ui.adapter.FieldLabelMultiChoiceAdapter;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentMultiSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wty
 * FieldLabel多选页面
 */
public class FieldLabelMultiChoiceActivity extends BaseActivity{

    public final static String Title =  "title";
    public final static String Options = "options";
    public final static String Selected = "selected";
    public final static String Fieldname = "fieldname";
    
    private ListView listView;
    private List<String> data = new ArrayList<String>();
    private FieldLabelMultiChoiceAdapter adapter ;
    private String title;
    private String options;
    private String selected;
    private String fieldname;
    
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_fieldlabel_multichoice);
        init();
        
    }

    private void init() {

    	title = getIntent().getStringExtra(Title);
    	options = getIntent().getStringExtra(Options);
    	selected = getIntent().getStringExtra(Selected);
    	fieldname = getIntent().getStringExtra(Fieldname);

        getDefaultNavigation().setTitle("请选择" + title);
        getDefaultNavigation().setRightButton("提交",new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String selected = adapter.getSelected();

				Intent i = new Intent();
				i.putExtra(Selected, selected);
				i.putExtra(Fieldname, fieldname);
				setResult(RESULT_OK,i);
				finish();

			}
		});
        
        listView = (ListView)findViewById(R.id.business_transefselect_listview);
        
        String[] opArray = ExpandContentMultiSelect.cropValue(options);
        for(String s:opArray){
        	data.add(s);
        }
        adapter = new FieldLabelMultiChoiceAdapter(this, data,selected);
        listView.setAdapter(adapter);
    }
    
}