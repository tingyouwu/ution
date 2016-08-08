package com.wty.ution.ui.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.adapter.FilterSelectAdapter;
import com.wty.ution.util.SerializableParams;
import com.wty.ution.widget.filter.base.FilterOptionModel;

import java.util.List;


/**
 * @author Amberlo 筛选控件选择页面
 */
public class FilterSelectActivity extends BaseActivity implements Handler.Callback{

    public final static int UI_EVENT_Select = 101010101;

    public final static String Tag_Fieldname =  "Tag_Fieldname";
    public final static String Tag_Title =  "Tag_title";
    public final static String Tag_Options = "Tag_Options";
    public final static String Tag_Selected = "Tag_Selected"; 
    
    private Handler handler;
    private ListView listView;
    private FilterSelectAdapter adapter ;

    private List<FilterOptionModel> options;
    private FilterOptionModel selected;
    private String fieldname;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_filterlabel_select);
        init();
        
    }

    private String title;

    @SuppressWarnings("unchecked")
	private void init() {
    	handler = new Handler(this);
    	title = getIntent().getStringExtra(Tag_Title);
    	
    	SerializableParams params = (SerializableParams) getIntent().getSerializableExtra(Tag_Options);
    	
    	if(params!=null){
    		options = (List<FilterOptionModel>) params.get();
    	}
    	
    	selected = (FilterOptionModel) getIntent().getSerializableExtra(Tag_Selected);
        fieldname = getIntent().getStringExtra(Tag_Fieldname);

        getDefaultNavigation().setTitle(title);
        getDefaultNavigation().setRightButton("确定",new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				result();
			}
		});
        
        listView = (ListView)findViewById(R.id.flb_select_listview);
        if(options!=null){
        	
        	adapter = new FilterSelectAdapter(this, options,selected,handler);
        	listView.setAdapter(adapter);
        }
    }
    
    private void result(){
		FilterOptionModel selected = adapter.getSelected(); 

		Intent i = new Intent();
        if(!TextUtils.isEmpty(fieldname)){
            i.putExtra(Tag_Fieldname, fieldname);
        }
		i.putExtra(Tag_Selected, selected);
		setResult(RESULT_OK,i);
		finish();
    }

	@Override
	public boolean handleMessage(Message msg) {
		result();
		return false;
	}
}