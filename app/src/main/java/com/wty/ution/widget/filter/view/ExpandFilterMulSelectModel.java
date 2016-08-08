package com.wty.ution.widget.filter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.filter.FilterMulSelectActivity;
import com.wty.ution.util.SerializableList;
import com.wty.ution.util.SerializableMap;
import com.wty.ution.widget.OnActivityResultListener;
import com.wty.ution.widget.filter.base.AbstractFilterModel;
import com.wty.ution.widget.filter.base.FilterOptionModel;
import com.wty.ution.widget.filter.base.IFilterModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by apple on 15/12/21.
 */
public class ExpandFilterMulSelectModel extends AbstractFilterModel {
    public final static int FieldType = 3;
    private int requestCode = 19995;
    private IFilterModel.SelectCallback callback;
    List<FilterOptionModel> models = new ArrayList<FilterOptionModel>();

    LinkedHashMap<String,FilterOptionModel> selected = new LinkedHashMap<String,FilterOptionModel>();
    FieldDescriptDALEx descript;
    public ExpandFilterMulSelectModel(Context context, FieldDescriptDALEx descript) {
        super(context, descript, InputMode.Select);
        this.descript = descript;
        init();
    }

    private void init(){
        BaseActivity activity = (BaseActivity)context;
        activity.registerOnActivityResultListener(new OnActivityResultListener(activity, requestCode) {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(resultCode == Activity.RESULT_OK){
                    String fieldname = data.getStringExtra(FilterMulSelectActivity.Tag_Fieldname);
                    if(!filterid.equals(fieldname))return;

                    //界面返回的选项
                    SerializableMap selectedMap = (SerializableMap) data.getSerializableExtra(FilterMulSelectActivity.Tag_Selected);
                    selected = (LinkedHashMap<String, FilterOptionModel>) selectedMap.getMap();

                    //界面显示用到的...
                    String textValue = "";
                    String value = "";

                    if(selected.size()!=0){

                        List<String> valueArray = new ArrayList<String>();

                        List<FilterOptionModel> selects = new ArrayList<FilterOptionModel>();
                        selects.addAll(selected.values());

                        for(FilterOptionModel model:selects){
                            valueArray.add(model.getValue());
                        }
                        textValue = String.format("%s等%d个选项",selects.get(0).getText(),selects.size());
                        value = TextUtils.join(",",valueArray);
                    }

                    FilterOptionModel itemModel = new FilterOptionModel(textValue,value);
                    if(selected!=null && callback!=null){
                        callback.onResult(itemModel);
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
        initSelectData();

        Intent i = new Intent(context,FilterMulSelectActivity.class);
        //字段名，做标识
        i.putExtra(FilterMulSelectActivity.Tag_Fieldname, filterid);
        //标题
        i.putExtra(FilterMulSelectActivity.Tag_Title, label);
        //选项
        i.putExtra(FilterMulSelectActivity.Tag_Options,new SerializableList(models));
        //已选值
        i.putExtra(FilterMulSelectActivity.Tag_Selected, new SerializableMap(selected));

        ((BaseActivity)context).startActivityForResult(i, requestCode);
    }

    @Override
    public String toSql() {
        return "";
    }

    protected void initSelectData(){

        if(descript!=null  && !TextUtils.isEmpty(descript.getXwoptional())){
            String[] options = descript.getXwoptional().split("\\|");

            for(String option: options){
                models.add(new FilterOptionModel(option, option));
            }
        }


    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    public void setTextValue(String textValue) {
        super.setTextValue(textValue);
    }

    @Override
    public void clearValue() {
        super.clearValue();
        selected.clear();
    }
}
