package com.wty.ution.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.ution.R;
import com.wty.ution.receiver.BroadcastConstants;
import com.wty.ution.ui.activity.logistics.LogisticsAddActivity;
import com.wty.ution.widget.listview.LogisticsListView;
import com.wty.ution.widget.logistics.LogisticsReportView;
import com.wty.ution.widget.navigation.NavigationText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 物流信息
 * @author wty
 */
public class LogisticsFragment extends BaseFragment{

    NavigationText navigation;
    boolean init = false;

    @Bind(R.id.logistics_listview)
    LogisticsListView mLogisticsListView;
    @Bind(R.id.logistics_headlayout)
    LogisticsReportView mLogisticsReportView;

    public LogisticsFragment(){
        super();
        registerBroadCast(new String[]{
                BroadcastConstants.REFRESH_LOGISCOUNT,
                BroadcastConstants.REFRESH_LOGISLIST});
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanState) {
        Log.v("性能调试", this.getClass().getSimpleName() + " onCreateView");

        View view = inflater.inflate(R.layout.fragment_logistics, null);
        ButterKnife.bind(this, view);
        //刷新列表
        mLogisticsListView.initData();
        //刷新统计数字
        mLogisticsReportView.refresh();
        init = true;
        return view;
    }

    @Override
    public void initFragmentActionBar() {
        if(navigation == null){
            navigation = new NavigationText(activity).setTitle("物流价格");
            navigation.getLeftButton().hide();
            navigation.setRightButton(R.drawable.actionbar_add, new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, LogisticsAddActivity.class);
                    activity.startActivity(i);
                }
            });
        }
        activity.setNavigation(navigation);
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
        if(action.equals(BroadcastConstants.REFRESH_LOGISCOUNT)) {
            mLogisticsReportView.refresh();
        }else if(action.equals(BroadcastConstants.REFRESH_LOGISLIST)){
            mLogisticsListView.refreshDefault();
        }
    }
}
