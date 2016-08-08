package com.wty.ution.ui.report;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wty.ution.R;
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.task.SimpleDialogTask;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.filter.BaseFilterEvent;
import com.wty.ution.widget.filter.base.FilterEventBus;
import com.wty.ution.widget.filter.base.FilterLabelConditionView;
import com.wty.ution.widget.filter.base.FilterLabelContainerView;
import com.wty.ution.widget.filter.base.FilterLabelScrollBar;
import com.wty.ution.widget.filter.base.IFilterModel;
import com.wty.ution.widget.filter.report.ReportGlassFilterLabelConditionView;
import com.wty.ution.widget.filter.report.ReportNavigationFilterLabelButton;
import com.wty.ution.widget.filter.view.GlassFilterOrderbyDateModel;
import com.wty.ution.widget.report.ReportPrecentButton;
import com.xtion.sheet.DataSet;
import com.xtion.sheet.model.ISheetColumnModel;
import com.xtion.sheet.model.ISheetRowModel;
import com.xtion.sheet.mvp.InnerSheetView;
import com.xtion.sheet.mvp.SheetPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能描述：眼镜销售信息报表
 * @author wty
 **/
public class ReportGlassSellDetailActivity extends BaseActivity {

    @Bind(R.id.report_glass_scrollbar) FilterLabelScrollBar filter_scrollbar;
    @Bind(R.id.report_glass_container) FilterLabelContainerView filter_container;
    @Bind(R.id.report_glass_count) ReportPrecentButton rpbtn_glasscount;//发货量(数量)
    @Bind(R.id.report_glass_cost) ReportPrecentButton rpbtn_glass_cost;//总成本(金额)
    @Bind(R.id.report_glass_sell) ReportPrecentButton rpbtn_glass_sell;//销售额(数量)
    @Bind(R.id.report_glass_profit) ReportPrecentButton rpbtn_glass_profit;//利润(金额)
    @Bind(R.id.report_glass_sheet) LinearLayout layout_sheet;//报表

    SheetPresenter sheetPresenter;

    private FilterLabelConditionView filter_condition;
    private ReportNavigationFilterLabelButton filter_button;
    private BaseFilterEvent filterEvent;
    private String busid = "report_GlassSell";

    SimpleTask task;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_report_glasssell);
        getDefaultNavigation().setTitle("眼镜销售报表");
        ButterKnife.bind(this);
        filterEvent = new BaseFilterEvent(this){

            @Override
            public IFilterModel getFilterOrderDefault() {
                return new GlassFilterOrderbyDateModel(ReportGlassSellDetailActivity.this);
            }

            @Override
            public void refreshPage() {
                startTask(filterEvent.getFilters(),filterEvent.getFilterOrder());
            }

        };

        filter_button = new ReportNavigationFilterLabelButton(this);
        filter_condition = new ReportGlassFilterLabelConditionView(this);
        filter_container.addContentView(filter_condition);
        filter_button.setContentView(filter_condition);

        FilterEventBus.register(busid, filter_button, filter_scrollbar, filter_container, filterEvent);

        getDefaultNavigation().setRightButton(R.drawable.actionbar_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTask(filterEvent.getFilters(),filterEvent.getFilterOrder());
            }
        });
        getDefaultNavigation().addRightButton(filter_button);


        Map<String,IFilterModel> map = filter_condition.getFilterMap();
        IFilterModel filter_date = map.get("xwdate");
        //过滤出今天
        filter_date.setValue(CommonUtil.dateToYYYYMMdd(CommonUtil.getTime()));
        filter_date.setTextValue(CommonUtil.dateToYYYYMMdd(CommonUtil.getTime()));

        FilterEventBus bus = FilterEventBus.getEventBus(busid);
        for(IFilterModel model:filter_condition.getFilters()){
            if(model.getFilterId().equals("xwdate")) {
                bus.onSubmit(filter_condition.getEventId(), model);
                filter_condition.onSubmit(model);
            }
        }
        filterEvent.refreshPage();
    }

    private void startTask(final List<IFilterModel> baseFilters,final IFilterModel orderModel){
        if(task!=null && task.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }

        task = new SimpleTask(){
            long cost = 0;
            long sell = 0;
            long profit = 0;
            @Override
            protected Object doInBackground(String... params) {
                cost = GlassesDALEx.get().countCostByFilters(baseFilters);
                sell = GlassesDALEx.get().countSellByFilters(baseFilters);
                profit = GlassesDALEx.get().countProfitByFilters(baseFilters);
                return GlassesDALEx.get().queryByFilters(baseFilters,orderModel);
            }

            @Override
            protected void onPostExecute(Object obj) {
                List<GlassesDALEx> result = (List<GlassesDALEx>)obj;
                refreshCount(result.size(),cost,sell,profit);
                refreshSheet(result);
            }

        };

        task.startTask();
//        task = new SimpleDialogTask(this){
//            long cost = 0;
//            long sell = 0;
//            long profit = 0;
//            @Override
//            public Object onAsync() {
//                cost = GlassesDALEx.get().countCostByFilters(baseFilters);
//                sell = GlassesDALEx.get().countSellByFilters(baseFilters);
//                profit = GlassesDALEx.get().countProfitByFilters(baseFilters);
//                return GlassesDALEx.get().queryByFilters(baseFilters,orderModel);
//            }
//
//            @Override
//            public void onResult(Object obj) {
//                List<GlassesDALEx> result = (List<GlassesDALEx>)obj;
//                refreshCount(result.size(),cost,sell,profit);
//                refreshSheet(result);
//            }
//        };
//        ((SimpleDialogTask)task).startTask("正在加载数据，请稍候...");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterEventBus.unregister(busid);
    }

    /**
     * 刷新统计数字
     * @param count 发货量
     * @param cost 成本价
     * @param sell 销售额
     * @param profit 利润
     **/
    private void refreshCount(int count,long cost,long sell,long profit){
        rpbtn_glasscount.setCount(""+count);
        rpbtn_glass_cost.setMoney("" + cost);
        rpbtn_glass_sell.setMoney(""+sell);
        rpbtn_glass_profit.setMoney(""+profit);
    }

    /**
     * 刷新表格
     * */
    private void refreshSheet(List<GlassesDALEx> models){
        //init row column
        List<ISheetColumnModel> columnModels = new ArrayList<>();
        List<ISheetRowModel> rowModels = new ArrayList<>();

        rowModels.addAll(models);
        columnModels.add(new ISheetColumnModel("订单时间", GlassesDALEx.XWDATE, ISheetColumnModel.ColumnType.DATE, true));
        columnModels.add(new ISheetColumnModel("款式", GlassesDALEx.XWTYPE));
        columnModels.add(new ISheetColumnModel("成本(元)", GlassesDALEx.XWCOSTPRICE, ISheetColumnModel.ColumnType.INT,true));
        columnModels.add(new ISheetColumnModel("售价(元)", GlassesDALEx.XWSELLPRICE, ISheetColumnModel.ColumnType.INT,true));
        columnModels.add(new ISheetColumnModel("利润(元)", GlassesDALEx.XWPROFIT,ISheetColumnModel.ColumnType.INT,true));

        DataSet dataSet = new DataSet.Builder(ReportGlassSellDetailActivity.this)
                .setAverageCellRect(true)
                .setColumnData(columnModels)
                .setSheetStyle(InnerSheetView.STYLE_TOP)
                .setEnableSort(true)
                .setRowData(rowModels)
                .build();
        sheetPresenter = new SheetPresenter(ReportGlassSellDetailActivity.this);

        layout_sheet.removeAllViews();
        layout_sheet.addView(sheetPresenter.getView(dataSet));
    }

}
