package com.wty.ution.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.data.Person;
import com.wty.ution.task.BasicDataInfoTask;
import com.wty.ution.task.RegionInfoTask;
import com.wty.ution.ui.fragment.BaseFragment;
import com.wty.ution.widget.HomeTabViewPager;
import com.wty.ution.widget.hometab.HomeTabLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author wty
 * 应用主页面 
 */
public class HomeActivity extends BaseActivity{

    @Bind(R.id.home_tab_layout) HomeTabLayout tabBar;
    @Bind(R.id.home_viewpager) HomeTabViewPager viewPager;

    BroadcastReceiver receiver = null;

    @Override
    public void onCreate(Bundle paramBundle) {
        try {
            super.onCreate(paramBundle);
            setContentView(R.layout.activity_home);// set the current View
            ButterKnife.bind(this);
            //设置ationBar样式
            if (android.os.Build.VERSION.SDK_INT > 11) {
                setTheme(R.style.Base_Theme_AppCompat_Light);
            } else {
                setTheme(R.style.UtionTheme);
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 弹出键盘不重新布局以避免底部上移
            //初始化控件
            initWidget();
        } catch (Exception e) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
            AppContext.getInstance().exitApp();
        }
    }

    /**
     * 初始化组件
     */
    private void initWidget() {
        Log.v("性能调试", this.getClass().getSimpleName() + " initHomeTab");
        tabBar.initHomeTab(viewPager);
        registerReceiver();
        Log.v("性能调试", this.getClass().getSimpleName() + " initHomeTab finish");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitBy2Click();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return false;
    }


    /**
     * 注册服务
     */
    public void registerReceiver() {

        if(receiver == null){
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //把广播消息分发到fragment上
                    for(BaseFragment fragment:tabBar.getFragments()){
                        if(!fragment.isInit())continue;
                        fragment.onBroadCast(intent);
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            //添加fragment需要监听的action
            for(BaseFragment fragment:tabBar.getFragments()){
                for(String action :fragment.getBroadCastList()){
                    filter.addAction(action);
                }
            }
            registerReceiver(receiver, filter);
        }
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            boolean tab = true;
            for(BaseFragment fragment:tabBar.getFragments()){
                tab = tab && fragment.onBackClick();
            }
            if(!tab){
                return;
            }

            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
        }
    }
}