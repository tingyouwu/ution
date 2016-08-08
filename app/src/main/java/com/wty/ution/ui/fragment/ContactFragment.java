package com.wty.ution.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.wty.ution.R;
import com.wty.ution.widget.navigation.NavigationText;

/**
 * 司机列表
 * @author wty
 */
public class ContactFragment extends BaseFragment{

    ScrollView mScrollView;
    LinearLayout office_tab_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanState) {
        Log.v("性能调试", this.getClass().getSimpleName() + " onCreateView");

        View view = inflater.inflate(R.layout.fragment_office, null);
        mScrollView = (ScrollView)view.findViewById(R.id.office_scrollview);
        return view;
    }

    NavigationText navigation;
    @Override
    public void initFragmentActionBar() {
        if(navigation == null){
            navigation = new NavigationText(activity).setTitle("司机");
            navigation.getLeftButton().hide();
        }
        activity.setNavigation(navigation);
    }

}
