package com.wty.ution.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.ution.R;
import com.wty.ution.widget.navigation.NavigationText;

/**
 * 报表
 * @author wty
 */
public class ReportFragment extends BaseFragment{

    NavigationText navigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanState) {
        Log.v("性能调试", this.getClass().getSimpleName() + " onCreateView");
        View view = inflater.inflate(R.layout.fragment_report, null);
        return view;
    }

    @Override
    public void initFragmentActionBar() {
        if(navigation == null){
            navigation = new NavigationText(activity).setTitle("报表分析");
            navigation.getLeftButton().hide();
        }
        activity.setNavigation(navigation);
    }

}
