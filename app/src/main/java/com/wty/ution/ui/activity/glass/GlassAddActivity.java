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
import com.wty.ution.ui.activity.MultiMediaActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.fieldlabel.expandcontent.ExpandContentTextInteger;
import com.wty.ution.widget.navigation.NavigationContainer;
import com.wty.ution.widget.navigation.NavigationText;
import com.wty.ution.widget.sweet.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty 添加眼镜销售信息页面
 */
public class GlassAddActivity extends MultiMediaActivity {

    protected FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;

    GlassesDALEx dalex;
    BmobGlassesDALEx bmobDalex;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_glass_add);
        init();
    }
    
    protected void init() {

        NavigationContainer navigation = new NavigationText(this)
        .setTitle("新增眼镜订单")
                .setRightButton("提交", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                }).build();
        setNavigation(navigation);

        layout_container = (FieldLabelContainer)findViewById(R.id.glass_add_container);
        layout_container.setMode(FieldLabelContainer.Mode.ADD);
        refreshFieldLayout();
    }
    
    private void refreshFieldLayout(){
        ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Glass);
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
	private void refreshView(GlassesDALEx dalex) {
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
                dalex = new GlassesDALEx();
                dalex.setGlassesid(UUID.randomUUID().toString());
            }

            if(bmobDalex==null){
                bmobDalex = new BmobGlassesDALEx();
            }

            setProperty();

            bmobDalex.save(GlassAddActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    onToast(new OnDismissCallbackListener("新增成功") {
                        @Override
                        public void onCallback() {
                            finish();
                        }
                    });
                    dalex.setGlassesid(bmobDalex.getObjectId());
                    //新增成功之后刷新一下页面
                    GlassesDALEx.get().save(dalex);
                    GlassesObserver.notifyCount(GlassAddActivity.this);
                    GlassesObserver.notifyList(GlassAddActivity.this);
                }

                @Override
                public void onFailure(int i, String msg) {
                    onToast("新增失败:"+msg);
                }
            });
    	}
    	return true;
    }


    private void setProperty(){
        ExpandContentTextInteger cost = (ExpandContentTextInteger)layout_container.getLabel(GlassesDALEx.XWCOSTPRICE);
        ExpandContentTextInteger sell = (ExpandContentTextInteger)layout_container.getLabel(GlassesDALEx.XWSELLPRICE);
        dalex.setXwupdatetime(CommonUtil.getTime());
        dalex.setXwcreatetime(CommonUtil.getTime());
        dalex.setAnnotationField(layout_container.getFixFieldValue());
        dalex.setXwprofit(Integer.parseInt(sell.getValue())-Integer.parseInt(cost.getValue()));
        dalex.setXwstatus(GlassesDALEx.Status_Normal);
        //Bmob数据模型
        bmobDalex.setAnnotationField(dalex);
    }
	
	protected void initLabelSuccess(){
	    	
	}
}