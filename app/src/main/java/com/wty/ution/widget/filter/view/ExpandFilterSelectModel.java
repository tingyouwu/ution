package com.wty.ution.widget.filter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.filter.FilterSelectActivity;
import com.wty.ution.util.SerializableParams;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterOptionModel;
import com.wty.ution.widget.filter.base.IFilterModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by apple on 15/12/21.
 */
public class ExpandFilterSelectModel extends AbstractFilterModel {

    public final static int FieldType = 2;

    private int requestCode = 19994;
    private IFilterModel.SelectCallback callback;
    List<FilterOptionModel> models = new ArrayList<FilterOptionModel>();

    FieldDescriptDALEx descript;
    public ExpandFilterSelectModel(Context context, FieldDescriptDALEx descript) {
        super(context, descript, InputMode.Select);
        this.descript = descript;
        init();
    }

    public ExpandFilterSelectModel(Context context, String filterid, String label){
        super(context, filterid, InputMode.Select,label);
        init();
    }
    
    private void init(){
        BaseActivity activity = (BaseActivity)context;
        activity.registerOnActivityResultListener(new OnActivityResultListener(activity, requestCode) {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String fieldname = data.getStringExtra(FilterSelectActivity.Tag_Fieldname);
                    if (!filterid.equals(fieldname)) return;
                    FilterOptionModel selected = (FilterOptionModel) data.getSerializableExtra(FilterSelectActivity.Tag_Selected);
                    if (selected != null && callback != null) {
                        callback.onResult(selected);
                    }
                }
                callback = null;
            }
        });
    }

    @Override
    public void onSelect(SelectCallback callback) {
        super.onSelect(callback);
        this.callback = callback;
        models.clear();
        models.add(new FilterOptionModel("不限", ""));
        initSelectData();

        SerializableParams paramMap = new SerializableParams(models);
        Intent i = new Intent(context,FilterSelectActivity.class);
        i.putExtra(FilterSelectActivity.Tag_Title, label);
        i.putExtra(FilterSelectActivity.Tag_Options,paramMap);

        i.putExtra(FilterSelectActivity.Tag_Fieldname, filterid);
        if(!TextUtils.isEmpty(value)){
            i.putExtra(FilterSelectActivity.Tag_Selected, new FilterOptionModel(textValue, value));

        }
        if(context instanceof BaseActivity){
            ((BaseActivity)context).startActivityForResult(i, requestCode);
        }

    }

    @Override
    public String toSql() {
        if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(filterid)){
            return "";
        }else{
            return filterid+" = '"+value +"'";
        }
    }

    protected void initSelectData(){


        if(!TextUtils.isEmpty(descript.getXwoptional())){
            String[] options = descript.getXwoptional().split("\\|");

            for(String option: options){
                models.add(new FilterOptionModel(option, option));
            }
        }

    }

}
