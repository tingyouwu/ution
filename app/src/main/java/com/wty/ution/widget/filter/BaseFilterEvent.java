package com.wty.ution.widget.filter;

import android.content.Context;

import com.wty.ution.widget.filter.base.FilterEventBus;
import com.wty.ution.widget.filter.base.IFilterEvent;
import com.wty.ution.widget.filter.base.IFilterModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 功能描述：把listview或者xxx的过滤实现抽取出来
 * 可以获取到过滤的model  排序的model
 **/
public abstract class BaseFilterEvent implements IFilterEvent {

    private String eventId;
    private FilterEventBus eventBus;
    private List<IFilterModel> filters;
    private IFilterModel filterOrder;
    private IFilterModel filterOrderDefault;
    Context context;
    public BaseFilterEvent(Context context){
        this.context = context;
        eventId = UUID.randomUUID().toString();
        filterOrderDefault = getFilterOrderDefault();
        filterOrder = filterOrderDefault;
        filters = new ArrayList<IFilterModel>();
    }

    @Override
    public void onSubmit(final IFilterModel filter) {

        boolean contains = false;
        for (IFilterModel f : filters) {
            if (f.getFilterId().equals(filter.getFilterId())) {
                contains = true;
                f.setTextValue(filter.getTextValue());
                f.setValue(filter.getValue());
            }
        }
        if (!contains) {
            filters.add(filter);
        }

    }

    @Override
    public void onCancel(IFilterModel filter) {
        Iterator<IFilterModel> it = filters.iterator();
        while (it.hasNext()) {
            IFilterModel f = it.next();
            if(disableModel.containsKey(f.getFilterId()))continue;
            if(f.getFilterId().equals(filter.getFilterId())){
                it.remove();
            }
        }
    }

    @Override
    public void onViewShow(String viewid, boolean show) {
        if(!show){
            //关闭页面，刷新查询
            refreshPage();
        }
    }

    @Override
    public void onButtonEvent(String buttonid, boolean selected) {
    }

    @Override
    public void onSubmit(final List<IFilterModel> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
    }

    @Override
    public void onCancel() {
        this.filterOrder = filterOrderDefault;
        if(disableModel.size()==0){
            this.filters.clear();
        }else{
            Iterator<IFilterModel> it = filters.iterator();
            while (it.hasNext()) {
                IFilterModel f = it.next();
                if(disableModel.containsKey(f.getFilterId()))continue;
                it.remove();

            }
        }
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public void onOrderEvent(IFilterModel model) {
        this.filterOrder = model;
        refreshPage();
    }

    @Override
    public void onRegister(FilterEventBus bus) {
        this.eventBus = bus;
        setFilterOrder(getFilterOrderDefault());
    }

    public void clearFilter(){
        filterOrder = filterOrderDefault;
        filters.clear();
        if(eventBus!=null){
            eventBus.onCancel(getEventId());
            eventBus.onButtonEvent(getEventId(), "", false);
        }
    }


    public void setFilterOrder(IFilterModel filterOrder) {
        this.filterOrder = filterOrder;
    }

    public IFilterModel getFilterOrder() {
        return filterOrder;
    }
    public List<IFilterModel> getFilters(){
        return filters;
    }

    public  IFilterModel getFilterOrderDefault(){
        //return new CustomerFilterOrderbyOnliveModel(context);
        return null;
    }
    public abstract void refreshPage();


    private Map<String,Boolean> disableModel = new HashMap<String,Boolean>();//人工禁止操作
    public void setDisableModel(String filterId){
        disableModel.put(filterId,true);
    }

}