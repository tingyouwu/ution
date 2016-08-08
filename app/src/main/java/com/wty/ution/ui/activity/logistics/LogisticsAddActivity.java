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
import com.wty.ution.ui.activity.MultiMediaActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentRegion;
import com.wty.ution.widget.navigation.NavigationContainer;
import com.wty.ution.widget.navigation.NavigationText;
import com.wty.ution.widget.sweet.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty 添加物流信息页面
 */
public class LogisticsAddActivity extends MultiMediaActivity {

    protected FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;

    LogisticsDALEx dalex;
    BmobLogisticsDALEx bmobDalex;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_logistics_add);
        init();
    }
    
    protected void init() {

        NavigationContainer navigation = new NavigationText(this)
        .setTitle("新增物流")
                .setRightButton("提交", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                }).build();
        setNavigation(navigation);

        layout_container = (FieldLabelContainer)findViewById(R.id.customer_add_container);
        layout_container.setMode(FieldLabelContainer.Mode.ADD);
        refreshFieldLayout();
    }
    
    private void refreshFieldLayout(){
        ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Logistics);
        if(info!=null){
            fieldDescripts = FieldDescriptDALEx.get().queryByEntityregid(info.getKey());
            if(fieldDescripts!=null){
                layout_container.addLabel(fieldDescripts);
                refreshView(dalex);
                initLabelSuccess();
            }
        }
    }
    
    
	/**
	 * 页面被系统回收内存以后，恢复页面用
	 */
	private void refreshView(LogisticsDALEx dalex) {
		if(dalex==null)return;
		layout_container.setFieldValue(dalex);
	}
	
    @Override
    protected List<String> validate() {
    	super.validate();
        //控件标签，控件校验结果
        List<String> result = new ArrayList<String>();
        String validate = layout_container.validate();
        if(!TextUtils.isEmpty(validate))result.add(validate);
        return result;
    }

    @Override
    protected boolean submit() {
    	if(super.submit()){

            if(dalex==null){
                dalex = new LogisticsDALEx();
                dalex.setLogisticsid(UUID.randomUUID().toString());
            }

            if(bmobDalex==null){
                bmobDalex = new BmobLogisticsDALEx();
            }

            setProperty(dalex);

            bmobDalex.save(LogisticsAddActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    onToast(new OnDismissCallbackListener("新增成功") {
                        @Override
                        public void onCallback() {
                            finish();
                        }
                    });
                    dalex.setLogisticsid(bmobDalex.getObjectId());
                    //新增成功之后刷新一下页面
                    LogisticsDALEx.get().save(dalex);
                    LogisticsObserver.notifyCount(LogisticsAddActivity.this);
                    LogisticsObserver.notifyList(LogisticsAddActivity.this);
                }

                @Override
                public void onFailure(int i, String msg) {
                    onToast("新增失败:" + msg);
                }
            });

    	}
    	return true;
    }


    private void setProperty(LogisticsDALEx dalex){
        ExpandContentRegion source = (ExpandContentRegion)layout_container.getLabel(LogisticsDALEx.XWSOURCE);
        ExpandContentRegion des = (ExpandContentRegion)layout_container.getLabel(LogisticsDALEx.XWDESRTINATION);
        dalex.setXwupdatetime(CommonUtil.getTime());
        dalex.setXwcreatetime(CommonUtil.getTime());
        dalex.setAnnotationField(layout_container.getFixFieldValue());
        dalex.setXwdestinationidpath(RegionInfoDALEx.get().queryById(Integer.parseInt(des.getValue())).getIdpath());
        dalex.setXwsourceidpath(RegionInfoDALEx.get().queryById(Integer.parseInt(source.getValue())).getIdpath());
        dalex.setXwstatus(LogisticsDALEx.Status_Normal);

        //Bmob数据模型
        bmobDalex.setAnnotationField(dalex);
    }
	
	protected void initLabelSuccess(){
	    	
	}
}