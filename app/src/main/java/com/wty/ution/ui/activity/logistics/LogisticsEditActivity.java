package com.wty.ution.ui.activity.logistics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.wty.ution.R;
import com.wty.ution.data.bmob.BmobLogisticsDALEx;
import com.wty.ution.data.dalex.ExpandinfoDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.receiver.LogisticsObserver;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentRegion;
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
public class LogisticsEditActivity extends BaseActivity{

    public static String TAG = "logistics";
    private FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;

    private LogisticsDALEx logis;
    private BmobLogisticsDALEx bmob;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        logis = (LogisticsDALEx) getIntent().getSerializableExtra(TAG);

        setContentView(R.layout.activity_logistics_edit);

        NavigationContainer navigation = new NavigationText(this)
                .setTitle("编辑物流")
                .setRightButton("提交", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                }).build();
        setNavigation(navigation);

        if(logis==null)return;

        LogisticsDALEx dbCache = LogisticsDALEx.get().queryById(logis.getLogisticsid());
        if(dbCache!=null){
            logis = dbCache;
        }
        
        
        layout_container = (FieldLabelContainer)findViewById(R.id.logistics_edit_extendfield);
        layout_container.setMode(FieldLabelContainer.Mode.EDIT);
        refreshFieldLayout();
    }

	private void refreshFieldLayout(){
    	
    	new SimpleTask(){
    		
			@Override
			protected String doInBackground(String... params) {
				ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Logistics);
		        if(info!=null){
		        	fieldDescripts = FieldDescriptDALEx.get().queryByEntityregid(info.getKey()); 
		        }
				return null;
			}
			protected void onPostExecute(Object result) {
				if(fieldDescripts!=null){
					layout_container.addLabel(fieldDescripts);
					refreshView(logis);
				}
			}
        	
        }.startTask();
    }
    
    private void refreshView(LogisticsDALEx logis){
  		if(logis!=null){
  			layout_container.setFieldValue(logis);
  		}
        
    }
    
    private void setProperty(){
        if(logis== null)
            return;
        ExpandContentRegion source = (ExpandContentRegion)layout_container.getLabel(LogisticsDALEx.XWSOURCE);
        ExpandContentRegion des = (ExpandContentRegion)layout_container.getLabel(LogisticsDALEx.XWDESRTINATION);
        logis.setXwupdatetime(CommonUtil.getTime());
        logis.setAnnotationField(layout_container.getFixFieldValue());
        logis.setXwdestinationidpath(RegionInfoDALEx.get().queryById(Integer.parseInt(des.getValue())).getIdpath());
        logis.setXwsourceidpath(RegionInfoDALEx.get().queryById(Integer.parseInt(source.getValue())).getIdpath());

        //Bmob数据模型
        bmob.setAnnotationField(logis);
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
            if(logis == null)
                return false;

            if(bmob==null){
                bmob = new BmobLogisticsDALEx();
            }

            setProperty();

            bmob.update(this, logis.getLogisticsid(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    //编辑成功之后刷新一下页面
                    LogisticsDALEx.get().save(logis);
                    LogisticsObserver.notifyList(LogisticsEditActivity.this);
                    LogisticsObserver.notifyCount(LogisticsEditActivity.this);
                    LogisticsObserver.notifyInfo(LogisticsEditActivity.this,logis);
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