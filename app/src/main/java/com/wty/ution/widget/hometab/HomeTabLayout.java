package com.wty.ution.widget.hometab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.fragment.BaseFragment;
import com.wty.ution.ui.fragment.GlassesFragment;
import com.wty.ution.ui.fragment.LogisticsFragment;
import com.wty.ution.ui.fragment.ReportFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用主页面底部tab布局
 * @author wty
 **/
public class HomeTabLayout extends LinearLayout {

    public int index = 0;
    public List<HomeTab> tabs    = new ArrayList<HomeTab>();
    public List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    public Map<String,HomeTab> mapHomeTab = new LinkedHashMap<String,HomeTab>();
    public HomeTab       lastTab = null;
    public ViewPager viewPager;
    public FragmentPagerAdapter fragmenetAdapter;
    private BaseActivity activity;

    public HomeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (BaseActivity)context;
    }

    public void addTab(int resource_normal, int resource_selected, String title,
            BaseFragment fragment,boolean isDefaultSelect) {
        HomeTab homeTab = new HomeTab(index, resource_normal, resource_selected, title, fragment,isDefaultSelect);
        tabs.add(homeTab);
        index++;
        mapHomeTab.put(title, homeTab);
        fragments.add(fragment);
        fragment.setActivity(activity);
    }

    /**
     * 功能描述：构建底部tab
     */
    public void initHomeTab(final ViewPager viewPager) {
        //第一步先清除数据
        this.tabs.clear();
        this.fragments.clear();
        this.mapHomeTab.clear();
        this.viewPager = viewPager;

        addTab(R.drawable.tab_glass,R.drawable.tab_glass_p, "眼镜", new GlassesFragment(), true);
        addTab(R.drawable.tab_office,R.drawable.tab_office_p,"物流",new LogisticsFragment(),false);
        addTab(R.drawable.tab_crm,R.drawable.tab_crm_p,"报表",new ReportFragment(),false);

        this.fragmenetAdapter = new HomeFragmentPagerAdapter(activity.getSupportFragmentManager(), tabs);
        this.viewPager.setOffscreenPageLimit(tabs.size());
        this.viewPager.addOnPageChangeListener(onPagerChangeListener);
        this.viewPager.setAdapter(fragmenetAdapter);

        // //底部tab对象
        for (final HomeTab tab : tabs) {

            View tabView = LayoutInflater.from(activity).inflate(R.layout.item_home_tab, null);
            TextView tabTitle = (TextView) tabView.findViewById(R.id.home_tab_title);
            ImageView tabIcon = (ImageView) tabView.findViewById(R.id.home_tab_icon);

            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
            addView(tabView, params);
            tab.setView(tabView);
            tabIcon.setImageResource(tab.iconResource);
            tabTitle.setText(tab.title);

            // 第一个选中的tab
            if (tab.isSelect) {
                lastTab = tab;
                tab.onSelect();
                tab.fragment.initFragmentActionBar();
                viewPager.setCurrentItem(tab.index);
            }

            tabView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(lastTab!=null){
                        lastTab.onUnSelect();
                    }
                    viewPager.setCurrentItem(tab.index);
                    tab.onSelect();
                    lastTab = tab;
                }
            });

        }
    }

    /**
     * @author wty 对页签对象的封装
     */
    public class HomeTab {
        public BaseFragment fragment;
        public int       index;
        public int       iconResource;
        public int       selectResource;
        public String    title;
        public boolean   isSelect;
        public View      tabView;
        public ImageView tabImg;
        public TextView  tabTitle;
        
        public HomeTab(int index, int iconResource, int selectResource, String title,BaseFragment fragment,boolean isSelect) {
            this.index = index;
            this.selectResource = selectResource;
            this.iconResource = iconResource;
            this.title = title;
            this.isSelect = isSelect;
            this.fragment = fragment;
        }
        
        /**
         * 功能描述：绑定对象也对应的View
         */
        public void setView(View tabView) {
            this.tabView = tabView;
            tabImg = (ImageView) tabView.findViewById(R.id.home_tab_icon);
            tabTitle = (TextView) tabView.findViewById(R.id.home_tab_title);
        }

        /**
         * 功能描述：页签选中状态事件
         */
        public void onSelect() {
            if (tabView != null) {
                tabTitle.setTextColor(getResources().getColor(R.color.blue_crm));
                tabImg.setImageResource(selectResource);

                if (onTabSelectedListener != null) {
                    onTabSelectedListener.onTabChange(index, this);
                }
            }
        }

        /**
         * 功能描述：页签没选中状态事件
         */
        public void onUnSelect() {
            if (tabView != null) {
                tabTitle.setTextColor(getResources().getColor(R.color.gray));
                tabImg.setImageResource(iconResource);
            }
        }
    }

    /**
     * @author wty
     *	ViewPager的Adapter
     */
    class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
        List<HomeTab> tabs;

        public HomeFragmentPagerAdapter(FragmentManager fm,List<HomeTab> tabs) {
            super(fm);

            this.tabs= tabs;
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position).fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    ViewPager.OnPageChangeListener onPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            try {
                for(int i=0;i<fragments.size();i++){
                    BaseFragment f = fragments.get(i);
                    f.setIsBackground(i!=position);
                }
                fragments.get(position).initFragmentActionBar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //页签切换事件
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private OnTabSelectedListener onTabSelectedListener = null;

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public interface OnTabSelectedListener {
        public void onTabChange(int position, HomeTab tab);
    }

    public List<HomeTab> getTabs() {
        return tabs;
    }

    public List<BaseFragment> getFragments() {
        return fragments;
    }

    public Map<String, HomeTab> getMapHomeTab() {
        return mapHomeTab;
    }

    public HomeTab getLastTab() {
        return lastTab;
    }
}
