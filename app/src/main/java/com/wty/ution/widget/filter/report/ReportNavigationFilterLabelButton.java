package com.wty.ution.widget.filter.report;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.wty.ution.R;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.filter.base.FilterEventBus;
import com.wty.ution.widget.filter.base.FilterLabelContainerView;
import com.wty.ution.widget.filter.base.IFilterEvent;
import com.wty.ution.widget.filter.base.IFilterModel;
import com.wty.ution.widget.navigation.NavigationButton;

import java.util.List;
import java.util.UUID;

/**
 * 功能描述：具有筛选过滤功能的actionbar button
 * @author wty
 */
public class ReportNavigationFilterLabelButton extends NavigationButton implements IFilterEvent {

    private String buttonid;
    private boolean selected = false;
    private FilterLabelContainerView.IContainerView contentView;

    private FilterEventBus eventBus;

    public ReportNavigationFilterLabelButton(Context context) {
        super(context);
        init();
    }

    public ReportNavigationFilterLabelButton(Context context, AttributeSet attr) {
        super(context,attr);
        init();
    }

    private void init(){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(CommonUtil.dip2px(getContext(), 60),ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        buttonid = UUID.randomUUID().toString();
        setSelected(selected);
        setButton(R.drawable.actionbar_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean select = !isSelected();
                System.out.println("button click " + buttonid + " select = " + select);

                setSelected(select);
                if (eventBus == null) return;
                if (contentView != null) {
                    eventBus.onViewShow(getEventId(), contentView.getViewId(), select);
                }
                //tab设置反选
            }
        });
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setContentView(FilterLabelContainerView.IContainerView contentView){
        this.contentView = contentView;
    }

    public String getButtonId(){
        return buttonid;
    }


    public boolean isSelected() {
        return selected;
    }

    @Override
    public void onButtonEvent(String buttonid, boolean selected) {
        setSelected(false);
        System.out.println("button " + this.buttonid + " onButtonEvent :" + buttonid + " is " + selected);
    }

    @Override
    public void onViewShow(String viewid, boolean show) {
        if(!show){
            setSelected(show);
        }
    }

    @Override
    public void onSubmit(IFilterModel filter) {

    }

    @Override
    public void onSubmit(List<IFilterModel> filters) {

    }

    @Override
    public void onCancel(IFilterModel filter) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOrderEvent(IFilterModel model) {

    }

    @Override
    public String getEventId() {
        return buttonid;
    }

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;
        if(contentView!=null){
            contentView.onRegister(bus);
        }
    }
}
