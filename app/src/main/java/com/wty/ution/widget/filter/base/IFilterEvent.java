package com.wty.ution.widget.filter.base;


import java.util.List;

public interface IFilterEvent {

	/**
	 * 页面显示
	 * @param viewid
	 * @param show
	 */
	public void onViewShow(String viewid, boolean show);

	/**
	 * 按钮点击事件
	 * @param buttonid
	 * @param selected
	 */
	public void onButtonEvent(String buttonid, boolean selected);

	/**
	 * 提交单个任务
	 * @param filter
	 */
	public void onSubmit(IFilterModel filter);
	
	/**
	 * 提交所有筛选条件
	 * @param filters
	 */
	public void onSubmit(List<IFilterModel> filters);

	/**
	 * 取消筛选条件
	 * @param filter
	 */
	public void onCancel(IFilterModel filter);
	
	/**
	 * 取消所有的筛选条件
	 */
	public void onCancel();
	
	/**
	 * 排序任务
	 * @param model
	 */
	public void onOrderEvent(IFilterModel model);
	
	/**
	 * 获取事件id
	 * @return
	 */
	public String getEventId();

	/**
	 * 注册eventbus
	 * @param eventBus
	 **/
    public void onRegister(FilterEventBus eventBus);

}
