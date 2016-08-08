package com.wty.ution.ui.activity.glass;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.wty.ution.R;
import com.wty.ution.data.bmob.BmobGlassesDALEx;
import com.wty.ution.data.dalex.ExpandinfoDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.receiver.GlassesObserver;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextInteger;
import com.wty.ution.widget.navigation.NavigationContainer;
import com.wty.ution.widget.navigation.NavigationText;
import com.wty.ution.widget.sweet.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 功能描述：物流信息修改页面
 * @author wty
 */
public class GlassEditActivity extends BaseActivity{

    public static String TAG = "glass";
    private FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;

    private GlassesDALEx dalex;
    private BmobGlassesDALEx bmob;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        dalex = (GlassesDALEx) getIntent().getSerializableExtra(TAG);

        setContentView(R.layout.activity_glass_edit);

        NavigationContainer navigation = new NavigationText(this)
                .setTitle("编辑订单")
                .setRightButton("提交", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                }).build();
        setNavigation(navigation);

        if(dalex==null)return;

        GlassesDALEx dbCache = GlassesDALEx.get().queryById(dalex.getGlassesid());
        if(dbCache!=null){
            dalex = dbCache;
        }
        
        
        layout_container = (FieldLabelContainer)findViewById(R.id.glass_edit_extendfield);
        layout_container.setMode(FieldLabelContainer.Mode.EDIT);
        refreshFieldLayout();
    }

	private void refreshFieldLayout(){
    	
    	new SimpleTask(){
    		
			@Override
			protected String doInBackground(String... params) {
				ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Glass);
		        if(info!=null){
		        	fieldDescripts = FieldDescriptDALEx.get().queryByEntityregid(info.getKey()); 
		        }
				return null;
			}
			protected void onPostExecute(Object result) {
				if(fieldDescripts!=null){
					layout_container.addLabel(fieldDescripts);
					refreshView(dalex);
				}
			}
        	
        }.startTask();
    }
    
    private void refreshView(GlassesDALEx dalex){
  		if(dalex!=null){
  			layout_container.setFieldValue(dalex);
  		}
        
    }
    
    private void setProperty(){
        if(dalex== null)
            return;

        ExpandContentTextInteger cost = (ExpandContentTextInteger)layout_container.getLabel(GlassesDALEx.XWCOSTPRICE);
        ExpandContentTextInteger sell = (ExpandContentTextInteger)layout_container.getLabel(GlassesDALEx.XWSELLPRICE);
        dalex.setXwupdatetime(CommonUtil.getTime());
        dalex.setXwcreatetime(CommonUtil.getTime());
        dalex.setAnnotationField(layout_container.getFixFieldValue());
        dalex.setXwprofit(Integer.parseInt(sell.getValue()) - Integer.parseInt(cost.getValue()));

        //Bmob数据模型
        bmob.setAnnotationField(dalex);
    }

    
    protected List<String> validate() {
        //控件标签，控件校验结果
        List<String> result = new ArrayList<String>();
        String validate = layout_container.validate();
        if(!TextUtils.isEmpty(validate)){
        	result.add(validate);
        }
        
        return result;
    }
    
    @Override
    protected boolean submit() {
    	if(super.submit()){

            if(dalex == null)
                return false;

            if(bmob==null){
                bmob = new BmobGlassesDALEx();
            }

            setProperty();

            bmob.update(this, dalex.getGlassesid(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    //编辑成功之后刷新一下页面
                    GlassesDALEx.get().save(dalex);
                    GlassesObserver.notifyList(GlassEditActivity.this);
                    GlassesObserver.notifyCount(GlassEditActivity.this);
                    GlassesObserver.notifyInfo(GlassEditActivity.this, dalex);
                    onToast(new OnDismissCallbackListener("修改成功") {
                        @Override
                        public void onCallback() {
                            finish();
                        }
                    });
                }

                @Override
                public void onFailure(int i, String msg) {
                    onToast("修改失败:" + msg);
                }
            });

    	}

    	return true;
    }

}