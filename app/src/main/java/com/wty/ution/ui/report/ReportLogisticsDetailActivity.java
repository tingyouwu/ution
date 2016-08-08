package com.wty.ution.ui.report;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wty.ution.R;
import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.task.SimpleDialogTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.filter.BaseFilterEvent;
import com.wty.ution.widget.filter.base.FilterEventBus;
import com.wty.ution.widget.filter.base.FilterLabelConditionView;
import com.wty.ution.widget.filter.base.FilterLabelContainerView;
import com.wty.ution.widget.filter.base.FilterLabelScrollBar;
import com.wty.ution.widget.filter.base.IFilterModel;
import com.wty.ution.widget.filter.report.ReportFilterLabelConditionView;
import com.wty.ution.widget.filter.report.ReportNavigationFilterLabelButton;
import com.wty.ution.widget.filter.view.LogisticsFilterOrderbyDateModel;
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
 * 功能描述：物流信息报表
 * @author wty
 **/
public class ReportLogisticsDetailActivity extends BaseActivity {

    @Bind(R.id.report_logistics_scrollbar) FilterLabelScrollBar filter_scrollbar;
    @Bind(R.id.report_logistics_container) FilterLabelContainerView filter_container;
    @Bind(R.id.report_logistics_count) ReportPrecentButton rpbtn_logisticscount;//发货量(数量)
    @Bind(R.id.report_logistics_amount) ReportPrecentButton rpbtn_logisticsamount;//发货运费(金额)
    @Bind(R.id.report_logistics_sheet) LinearLayout layout_sheet;//报表

    SheetPresenter sheetPresenter;

    private FilterLabelConditionView filter_condition;
    private ReportNavigationFilterLabelButton filter_button;
    private BaseFilterEvent filterEvent;
    private String busid = "report_LogisticsDetail";

    SimpleDialogTask task;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_report_logisticsdetail);
        getDefaultNavigation().setTitle("物流信息报表");
        ButterKnife.bind(this);
        filterEvent = new BaseFilterEvent(this){

            @Override
            public IFilterModel getFilterOrderDefault() {
                return new LogisticsFilterOrderbyDateModel(ReportLogisticsDetailActivity.this);
            }

            @Override
            public void refreshPage() {
                startTask(filterEvent.getFilters(),filterEvent.getFilterOrder());
            }

        };

        filter_button = new ReportNavigationFilterLabelButton(this);
        filter_condition = new ReportFilterLabelConditionView(this);
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

        task = new SimpleDialogTask(this){
            long money = 0;
            @Override
            public Object onAsync() {
                money = LogisticsDALEx.get().countMoneyByFilters(baseFilters,orderModel);
                return LogisticsDALEx.get().queryByFilters(baseFilters,orderModel);
            }

            @Override
            public void onResult(Object obj) {
                List<LogisticsDALEx> result = (List<LogisticsDALEx>)obj;
                refreshCount(result.size(),money);
                refreshSheet(result);
            }
        };
        task.startTask("正在加载数据，请稍候...");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterEventBus.unregister(busid);
    }

    /**
     * 刷新统计数字
     **/
    private void refreshCount(int count,long amount){
        rpbtn_logisticscount.setCount(""+count);
        rpbtn_logisticsamount.setMoney(""+amount);
    }

    /**
     * 刷新表格
     * */
    private void refreshSheet(List<LogisticsDALEx> models){
        //init row column
        List<ISheetColumnModel> columnModels = new ArrayList<>();
        List<ISheetRowModel> rowModels = new ArrayList<>();

        rowModels.addAll(models);
        columnModels.add(new ISheetColumnModel("出车时间", "xwdate", ISheetColumnModel.ColumnType.DATE,true));
        columnModels.add(new ISheetColumnModel("运费(元)", "xwprice", ISheetColumnModel.ColumnType.INT,true));
        columnModels.add(new ISheetColumnModel("出发地", "xwsource"));
        columnModels.add(new ISheetColumnModel("目的地", "xwdestination"));


        DataSet dataSet = new DataSet.Builder(ReportLogisticsDetailActivity.this)
                .setAverageCellRect(true)
                .setColumnData(columnModels)
                .setSheetStyle(InnerSheetView.STYLE_TOP)
                .setEnableSort(true)
                .setRowData(rowModels)
                .build();
        sheetPresenter = new SheetPresenter(ReportLogisticsDetailActivity.this);

        layout_sheet.removeAllViews();
        layout_sheet.addView(sheetPresenter.getView(dataSet));
    }

}
