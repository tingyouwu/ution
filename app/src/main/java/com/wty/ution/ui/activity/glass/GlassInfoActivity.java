package com.wty.ution.ui.activity.glass;

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
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.receiver.BroadcastConstants;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.activity.logistics.LogisticsEditActivity;
import com.wty.ution.widget.fieldlabel.FieldLabelContainer;
import com.wty.ution.widget.navigation.NavigationContainer;
import com.wty.ution.widget.navigation.NavigationText;

import java.util.List;

/**
 * 功能描述：眼镜详情
 * @author wty
 */
public class GlassInfoActivity extends BaseActivity {

    public static String TAG = "glassDalex";
    private FieldLabelContainer layout_container;
    private List<FieldDescriptDALEx> fieldDescripts;
    private GlassesDALEx dalex;
    IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_glass_info);

        dalex = (GlassesDALEx) getIntent().getSerializableExtra(TAG);
        if(dalex==null)return;

        GlassesDALEx dbCache = GlassesDALEx.get().queryById(dalex.getGlassesid());
        if(dbCache!=null){
            dalex = dbCache;
        }

        NavigationContainer navigation = new NavigationText(this)
                .setTitle("眼镜订单详情")
                .setRightButton("编辑", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dalex == null) {
                            onToast("眼镜订单信息已在数据库删除，无法编辑");
                        }
                        Intent i = new Intent(GlassInfoActivity.this,GlassEditActivity.class);
                        i.putExtra(GlassEditActivity.TAG, dalex);
                        startActivity(i);
                    }
                }).build();
        setNavigation(navigation);

        layout_container = (FieldLabelContainer)findViewById(R.id.glass_info_extendfield);
        layout_container.setMode(FieldLabelContainer.Mode.INFO);

        ExpandinfoDALEx info = ExpandinfoDALEx.get().queryByName(ExpandinfoDALEx.Name_Glass);
        if(info!=null){
            fieldDescripts = FieldDescriptDALEx.get().queryByEntityregid(info.getKey());
            if(fieldDescripts!=null){
                layout_container.addLabel(fieldDescripts);
                refreshView(dalex);
            }
        }

        filter.addAction(BroadcastConstants.REFRESH_GLASSES_INFO);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        receiver = null;
    }
    
    private void refreshView(final GlassesDALEx dalex){
    		if(dalex==null)return;
    		layout_container.setFieldValue(dalex);
    }

    BroadcastReceiver receiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if(action.equals(BroadcastConstants.REFRESH_GLASSES_INFO)){
                    dalex = GlassesDALEx.get().queryById(dalex.getGlassesid());
                    if(dalex==null){
                        dalex = (GlassesDALEx) intent.getSerializableExtra(GlassInfoActivity.TAG);
                    }
                    if(dalex!=null){
                        refreshView(dalex);
                    }
                }

        }
    };

}