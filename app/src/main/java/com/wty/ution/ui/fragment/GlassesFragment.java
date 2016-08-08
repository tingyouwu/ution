package com.wty.ution.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.ution.R;
import com.wty.ution.receiver.BroadcastConstants;
import com.wty.ution.ui.activity.glass.GlassAddActivity;
import com.wty.ution.ui.activity.logistics.LogisticsAddActivity;
import com.wty.ution.widget.glasses.GlassesReportView;
import com.wty.ution.widget.listview.GlasssesListView;
import com.wty.ution.widget.navigation.NavigationText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 眼镜销售信息
 * @author wty
 */
public class GlassesFragment extends BaseFragment{

    NavigationText navigation;
    boolean init = false;

    @Bind(R.id.glasses_listview)
    GlasssesListView mGlassesListView;
    @Bind(R.id.glasses_headlayout)
    GlassesReportView mGlassesReportView;

    public GlassesFragment(){
        super();
        registerBroadCast(new String[]{
                BroadcastConstants.REFRESH_GLASSES_COUNT,
                BroadcastConstants.REFRESH_GLASSES_LIST});
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanState) {
        Log.v("性能调试", this.getClass().getSimpleName() + " onCreateView");

        View view = inflater.inflate(R.layout.fragment_glasses, null);
        ButterKnife.bind(this, view);
        //刷新列表
        mGlassesListView.initData();
        //刷新统计数字
        mGlassesReportView.refresh();
        init = true;
        return view;
    }

    @Override
    public void initFragmentActionBar() {
        if(navigation == null){
            navigation = new NavigationText(activity).setTitle("眼镜交易");
            navigation.getLeftButton().hide();
            navigation.setRightButton(R.drawable.actionbar_add, new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, GlassAddActivity.class);
                    activity.startActivity(i);
                }
            });
        }
        activity.setNavigation(navigation);
    }

    public void refreshView() {
        //UI没有初始化成功，不能执行任务
        if(!init)return;
        mGlassesReportView.refresh();
        mGlassesListView.refreshDefault();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);//解绑
    }

    @Override
    public void onBroadCast(Intent intent) {
        String action = intent.getAction();
        if(action.equals(BroadcastConstants.REFRESH_GLASSES_COUNT)) {
            mGlassesReportView.refresh();
        }else if(action.equals(BroadcastConstants.REFRESH_GLASSES_LIST)){
            mGlassesListView.refreshDefault();
        }
    }
}
