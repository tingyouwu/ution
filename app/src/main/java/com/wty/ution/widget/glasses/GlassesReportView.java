package com.wty.ution.widget.glasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.ReportButton;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class GlassesReportView extends LinearLayout{

    @Bind(R.id.crm_tv_time)
    TextView tv_time;

    @Bind(R.id.crm_viewpager)
    ViewPager viewpager;

    ReportButton btn_glasses_month_amount;
    ReportButton btn_glasses_month_money;
    ReportButton btn_glasses_today_amount;
    ReportButton btn_glasses_today_money;
    SimpleTask simpletask;

    View[] pages;

//    private ImageView[] circleImages;

//    private void initCircls(Context context) {
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.crm_circle_linear);
//        LinearLayout.LayoutParams layoutParamsImageMain = new LinearLayout.LayoutParams(15, 15);
//        layoutParamsImageMain.setMargins(15, 0, 0, 0);
//        circleImages = new ImageView[pages.length];
//
//        for (int i = 0; i < pages.length; i++) {
//            circleImages[i] = new ImageView(context);
//            circleImages[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            circleImages[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//            if (i == 0) {
//                circleImages[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_white));
//            } else {
//                circleImages[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_gray));
//            }
//
//            linearLayout.addView(circleImages[i], layoutParamsImageMain);
//        }
//
//    }

	public GlassesReportView(Context context, AttributeSet attrs) {
		super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_glasses_report, this);
        ButterKnife.bind(this);

        View page_1 = LayoutInflater.from(getContext()).inflate(R.layout.view_glasses_report_1,null);
//        View page_2 = LayoutInflater.from(getContext()).inflate(R.layout.view_crm_report_2,null);

        btn_glasses_month_amount = (ReportButton)page_1.findViewById(R.id.glasses_month_amount);
        btn_glasses_month_money = (ReportButton)page_1.findViewById(R.id.glasses_month_money);
        btn_glasses_today_amount = (ReportButton)page_1.findViewById(R.id.glasses_today_amount);
        btn_glasses_today_money = (ReportButton)page_1.findViewById(R.id.glasses_today_money);
        pages = new View[]{ page_1};
        viewpager.setAdapter(new CrmPageAdapter());
//        initCircls(context);
//        viewpager.setOnPageChangeListener(new MyPageChangeListener());
	}

    public void refresh() {
        tv_time.setText(CommonUtil.getTime(CommonUtil.DataFormat_list_year));
        if(simpletask !=null && simpletask.getStatus() == AsyncTask.Status.RUNNING){
            simpletask.cancel(false);
        }
        simpletask = new SimpleTask() {
            int monthAmount = 0;
            int todayAmount = 0;
            int monthMoney = 0;
            int todayMoney = 0;
            @Override
            protected Object doInBackground(String... params) {
                monthAmount = GlassesDALEx.get().countCurrentMonthAmount();
                todayAmount = GlassesDALEx.get().countTodayAmount();
                monthMoney = GlassesDALEx.get().countCurrentMonthMoney();
                todayMoney = GlassesDALEx.get().countTodayMoney();
                return "";
            }

            @Override
            protected void onPostExecute(Object o) {
                btn_glasses_month_amount.setCount(monthAmount);
                btn_glasses_today_amount.setCount(todayAmount);
                btn_glasses_month_money.setCount(monthMoney);
                btn_glasses_today_money.setCount(todayMoney);
            }
        };
        simpletask.startTask();
    }


    class CrmPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //页面view
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = pages[position];
            container.addView(view);
            return view;
        }

    }

//    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageSelected(int page) {
//
//            for (int i = 0; i < pages.length; i++) {
//                if (i == page) {
//                    circleImages[i].setImageDrawable(getResources()
//                            .getDrawable(R.drawable.circle_white));
//                } else {
//                    circleImages[i].setImageDrawable(getResources()
//                            .getDrawable(R.drawable.circle_gray));
//                }
//            }
//
//        }
//
//    }

}
