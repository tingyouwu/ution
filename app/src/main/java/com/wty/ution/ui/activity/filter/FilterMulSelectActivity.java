package com.wty.ution.ui.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.adapter.FilterMulSelectAdapter;
import com.wty.ution.util.SerializableList;
import com.wty.ution.util.SerializableMap;
import com.wty.ution.widget.filter.base.FilterOptionModel;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author Amberlo 筛选控件选择页面
 */
public class FilterMulSelectActivity extends BaseActivity {

    public final static String Tag_Fieldname =  "Tag_Fieldname";
    public final static String Tag_Title =  "Tag_title";
    public final static String Tag_Options = "Tag_Options";
    public final static String Tag_Selected = "Tag_Selected"; 

    private ListView listView;
    private FilterMulSelectAdapter adapter ;

    private List<FilterOptionModel> options;
    private LinkedHashMap<String,FilterOptionModel> selected;
    private String fieldname;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_filterlabel_select);
        init();
        
    }

    private String title;

	private void init() {
    	title = getIntent().getStringExtra(Tag_Title);
        fieldname = getIntent().getStringExtra(Tag_Fieldname);

    	SerializableList params = (SerializableList) getIntent().getSerializableExtra(Tag_Options);
        options = (List<FilterOptionModel>) params.getList();

        SerializableMap mapParams = (SerializableMap) getIntent().getSerializableExtra(Tag_Selected);
        selected = (LinkedHashMap<String, FilterOptionModel>) mapParams.getMap();


        getDefaultNavigation().setTitle(title);
        getDefaultNavigation().setRightButton("确定",new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				result();
			}
		});
        
        listView = (ListView)findViewById(R.id.flb_select_listview);
        adapter = new FilterMulSelectAdapter(this, options,selected);
        listView.setAdapter(adapter);

    }
    
    private void result(){

		Intent i = new Intent();
        if(!TextUtils.isEmpty(fieldname)){
            i.putExtra(Tag_Fieldname, fieldname);
        }

        LinkedHashMap<String,FilterOptionModel> selected = adapter.getSelected();
		i.putExtra(Tag_Selected, new SerializableMap(selected));
		setResult(RESULT_OK,i);
		finish();
    }

}