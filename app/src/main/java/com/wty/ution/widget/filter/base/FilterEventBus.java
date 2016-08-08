package com.wty.ution.widget.filter.base;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 筛选控件事件分发器，负责把事件分发到所有注册的对象中
 * @author wty
 *
 */
public class FilterEventBus {

    private static Map<String,FilterEventBus> map = new HashMap<String,FilterEventBus>();

	private static List<IFilterEvent> events = new ArrayList<IFilterEvent>();

	/** 当前eventbus对应的总线id,每一组event都有唯一的id */
    private String busid;

    FilterEventBus(String busid){
        this.busid = busid;
    }

    public String getBusid(){
        return busid;
    }

	/**
	 * 功能描述：注册事件,单个event对象,加入观察者对象集合，以便分发事件
	 * @param event
	 **/
	public void registerEvent(IFilterEvent event){
		boolean isContainer = false;
		for(IFilterEvent e:events ){
			if(e.getEventId().equals(event.getEventId())){
				isContainer = true;
			}
		}
		if(!isContainer){
			events.add(event);
		}
	}

	/**
	 * 功能描述：注销事件，单个event对象,从观察者对象集合中移除，不再分发事件
	 * @param event
	 **/
	public void unRegisterEvent(IFilterEvent event){
		Iterator<IFilterEvent> it = events.iterator();
		while(it.hasNext()){
			IFilterEvent e =  it.next();
			if(e.getEventId().equals(event.getEventId())){
				it.remove();
			}
		}
	}

	/**
	 * 功能描述：分发button事件,由FilterLabelButton触发
	 * @param eventId 观察者对象eventId
	 * @param buttonid button对应的id   FilterLabelButton的eventId和buttonid相同
	 * @param selected 是否是选择或者反选状态
	 **/
	public void onButtonEvent(String eventId,String buttonid, boolean selected) {
		Log.v("性能调试", "eventBus filter: onButtonEvent "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			if(checkSender(eventId, event))continue;
			if(!buttonid.equals(event.getEventId())){
				event.onButtonEvent(buttonid,selected);
			}
		}
	}

	/**
	 * 功能描述：提交事件,单个过滤model触发
	 * @param eventId 观察者对象eventId
	 * @param model 提交参数
	 **/
	public void onSubmit(String eventId,IFilterModel model) {
		Log.v("性能调试", "eventBus filter: onSubmitOne "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			if(checkSender(eventId, event))continue;
			event.onSubmit(model);
		}

	}

	/**
	 * 功能描述：提交事件 由"确定"按钮触发
	 * @param eventId 观察者对象eventId
	 * @param filters 提交参数
	 **/
	public void onSubmit(String eventId,List<IFilterModel> filters) {
		Log.v("性能调试", "eventBus filter: onSubmitAll "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			if(checkSender(eventId, event))continue;
			event.onSubmit(filters);
		}
	}

	/**
	 * 功能描述：view触发显示事件
	 * @param eventId 观察者对象eventId
	 * @param viewid  view的id
	 * @param show 布尔值  view是否是显示状态
	 **/
	public void onViewShow(String eventId,String viewid, boolean show) {
		Log.v("性能调试", "eventBus filter: onViewShow "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			event.onViewShow(viewid, show);
		}
	}

	/**
	 * 功能描述：单个过滤条件取消事件
	 * @param eventId  观察者对象eventId
	 * @param model   单个过滤条件
	 **/
	public void onCancel(String eventId,IFilterModel model) {
		Log.v("性能调试", "eventBus filter: onCancelOne "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			if(checkSender(eventId, event))continue;
			event.onCancel(model);
		}
	}

	/**
	 * 功能描述：取消事件
	 * @param eventId  观察者对象eventId
	 **/
	public void onCancel(String eventId) {
		Log.v("性能调试", "eventBus filter: onCancel "+eventId);
		if(!checkParams(eventId))return;
		for(IFilterEvent event:events){
			if(checkSender(eventId, event))continue;
			event.onCancel();
		}
	}

	/**
	 * 功能描述：排序事件
	 * @param senderId  观察者对象eventId
	 * @param model   单个过滤条件
	 **/
	public void onOrderEvent(String senderId,IFilterModel model) {
		Log.v("性能调试", "eventBus filter: onOrderEvent "+senderId);
		if(!checkParams(senderId))return;
		for(IFilterEvent event:events){
			if(checkSender(senderId, event))continue;
			event.onOrderEvent(model);
		}
	}



	/**
	 * 功能描述：判断一下触发者是否是自己
	 * @param eventId 触发者的eventId
	 * @return true 是自己，事件不需要分发
	 **/
	private boolean checkSender(String eventId,IFilterEvent event){
		return eventId.equals(event.getEventId());
	}

	/**
	 * 功能描述：判断一下eventId是否是空的,为空不处理
	 * @param eventId 触发者的eventId
	 **/
	private boolean checkParams(String eventId){
		if(TextUtils.isEmpty(eventId))
			return false;
		else
			return true;
	}

	/**
	 *  功能描述:注册事件
	 *  @param busid 业务id
	 *  @param registers 一系列需要注册的观察者event对象
	 **/
    public static void register(String busid,IFilterEvent... registers ){
        FilterEventBus eventBus = getEventBus(busid);
        eventBus.events.clear();
        for(IFilterEvent event:registers){
            event.onRegister(eventBus);
            eventBus.events.add(event);
        }
    }

	/**
	 *  功能描述:注销事件
	 *  @param busid 业务id
	 **/
    public static void unregister(String busid){
        if(map.containsKey(busid)){
            FilterEventBus bus = map.get(busid);
            bus.events.clear();
            map.remove(busid);
        }
    }


	/**
	 * 功能描述:销毁事件
	 * 退出应用时调用清除缓存
	 **/
    public static void destroy(){
        for(FilterEventBus bus:map.values()){
            bus.events.clear();
        }
        map.clear();
    }

	/**
	 * 功能描述：根据busid获取对应的eventbus，不存在就创建
	 * @param busid 业务id
	 **/
    public static FilterEventBus getEventBus(String busid){
        FilterEventBus eventBus;
        if(!map.containsKey(busid)){
            eventBus = new FilterEventBus(busid);
            map.put(busid, eventBus);
        }else{
            eventBus = map.get(busid);
        }
        return eventBus;
    }

}