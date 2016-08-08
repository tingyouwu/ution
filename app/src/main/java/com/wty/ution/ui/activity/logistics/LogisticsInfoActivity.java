package com.wty.ution.ui.activity.logistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wty.ution.R;
import com.wty.ution.data.dalex.ExpandinfoDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.receiver.BroadcastConstants;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.widget.fieldlabel.ContentDivide;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.navigation.NavigationContainer;
import com.wty.ution.widget.navigation.NavigationText;

import java.util.List;

/**
 * 功能描述：物流详情
 * @author wty
 */
public class LogisticsInfoActivity extends BaseActivity {

    public static String TAG = "logisticsDalex";
    private FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;
    private LogisticsDALEx logis;
    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_logistics_info);

        logis = (LogisticsDALEx) getIntent().getSerializableExtra(TAG);
        if(logis==null)return;

        LogisticsDALEx dbCache = LogisticsDALEx.get().queryById(logis.getLogisticsid());
        if(dbCache!=null){
            logis = dbCache;
        }

        NavigationContainer navigation = new NavigationText(this)
                .setTitle("物流详情")
                .setRightButton("编辑", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(logis == null) {
                            onToast("本物流信息已在数据库删除，无法编辑");
                        }
                        Intent i = new Intent(LogisticsInfoActivity.this,LogisticsEditActivity.class);
                        i.putExtra(LogisticsEditActivity.TAG, logis);
                        startActivity(i);
                    }
                }).build();
        setNavigation(navigation);

        layout_container = (FieldLabelContainer)findViewById(R.id.logistics_info_extendfield);
        layout_container.setMode(FieldLabelContainer.Mode.INFO);

        ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Logistics);
        if(info!=null){
            fieldDescripts = FieldDescriptDALEx.get().queryByEntityregid(info.getKey());
            if(fieldDescripts!=null){
                layout_container.addLabel(fieldDescripts);
                refreshView(logis);
            }
        }

        filter.addAction(BroadcastConstants.REFRESH_LOGISINFO);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }
    
    private void refreshView(final LogisticsDALEx logis){
    		if(logis==null)return;

    		if(layout_container.getChildCount()>=3){
    			View view = layout_container.getChildAt(2);
    			if(view!=null && view instanceof ContentDivide){
    				view.setVisibility(View.GONE);
    			}
    		}
    		layout_container.setFieldValue(logis);

    }

    BroadcastReceiver receiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if(action.equals(BroadcastConstants.REFRESH_LOGISINFO)){
                    logis = LogisticsDALEx.get().queryById(logis.getLogisticsid());
                    if(logis==null){
                        logis = (LogisticsDALEx) intent.getSerializableExtra(LogisticsInfoActivity.TAG);
                    }
                    if(logis!=null){
                        refreshView(logis);
                    }
                }

        }
    };

}