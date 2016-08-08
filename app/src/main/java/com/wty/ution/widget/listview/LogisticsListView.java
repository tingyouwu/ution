package com.wty.ution.widget.listview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wty.ution.data.bmob.BmobLogisticsDALEx;
import com.wty.ution.data.dalex.LogisticsDALEx;
import com.wty.ution.receiver.LogisticsObserver;
import com.wty.ution.task.SimpleDialogTask;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.adapter.LogisticsListAdapter;
import com.wty.ution.widget.filter.IFilterListView;
import com.wty.ution.widget.sweet.OnDismissCallbackListener;

import java.util.ArrayList;
import java.util.List;

import SweetAlert.SweetAlertDialog;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 功能描述：物流信息
 * @author wty
 **/
public class LogisticsListView extends XRecyclerView implements IFilterListView {

	private boolean isInit;
    private boolean isInitLoadFromServie;
    private boolean isLoadFromService;

    protected LogisticsListAdapter adapter;
    public List<LogisticsDALEx> data = new ArrayList<LogisticsDALEx>();

    private ListViewEmptyLayout emptyView;
    private SimpleTask task;
    private SimpleTask loadMoreTask;
    private ListPage page;
    private BaseActivity activity;

	public LogisticsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        activity = (BaseActivity)context;
        page = new ListPage(20);
        adapter = new LogisticsListAdapter(context,data);
        setAdapter(adapter);
        setLoadingListener(loadingListener);
        adapter.setOnItemLongClickListener(itemLongClick);
    }

    @Override
    public void refreshService(){
        //重置一下页面
        startTask();
    }

    @Override
	public void refreshDefault(){
        //重置一下页面
        page.pageReset();
        isInitLoadFromServie = false;
        if(task!=null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(false);
        }

        task = new SimpleTask(){

            @Override
            protected Object doInBackground(String... params) {
                return LoadFromLocal(page.getPagePiece(),page.getnextPageStartIndex());
            }

            @Override
            protected void onPostExecute(Object result) {
                List<LogisticsDALEx> query=(List<LogisticsDALEx>) result;
                data.clear();
                data.addAll(query);
                adapter.notifyDataSetChanged();
                if(data.size()==0){
                    //本地没有数据，从服务端拿数据
                    if(!isInitLoadFromServie){
                        refreshService();
                        isInitLoadFromServie = true;
                    }
                }else {
                    page.setPage(query.size());
                }
            }
        };

        task.startTask();
	}

    public void setEmptyLayout(ListViewEmptyLayout emptyView) {
        this.emptyView = emptyView;
    }

	public void initData(){
		if(!isInit){
			if(emptyListener!=null)emptyListener.onNormal();
			refreshDefault();
			isInit = true;
        }
	}


    protected OnDataEmptyListener emptyListener;
    public void setOnDataEmptyListener(OnDataEmptyListener emptyListener) {
        this.emptyListener = emptyListener;
    }

    /**
     * 功能描述：从本地获取数据,20条
     **/
    protected List<LogisticsDALEx> LoadFromLocal(int limit,int offset){
        return LogisticsDALEx.get().queryLimitByOffset(limit,offset);
    }

    LoadingListener loadingListener = new LoadingListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            startTask();
        }

        @Override
        public void onLoadMore() {
            //上拉加载更多
            startLoadMoreTask();
        }
    };

    /**
     * 功能描述：下拉刷新
     **/
    private void startTask(){
//        if(task!=null && task.getStatus() == AsyncTask.Status.RUNNING){
//            task.cancel(false);
//        }
        //重置页数
        page.pageReset();

//        activity.loading("正在加载数据,请稍候...");
        BmobQuery<BmobLogisticsDALEx> query = new BmobQuery<BmobLogisticsDALEx>();
        query.setLimit(page.getPagePiece());
        query.order("-" + LogisticsDALEx.XWDATE);
        query.findObjects(activity, new FindListener<BmobLogisticsDALEx>() {
            @Override
            public void onSuccess(List<BmobLogisticsDALEx> list) {
                refreshComplete();
//                activity.dismissLoading();
                page.setPage(list.size());
                if (list.size() == 0) return;
                BmobLogisticsDALEx.get().save(list);
                isLoadFromService = true;
                data.clear();
                data.addAll(LoadFromLocal(page.getPagePiece(), 0));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                refreshComplete();
//                activity.dismissLoading();
                activity.onToastErrorMsg(s);
            }
        });

//        task =  new SimpleDialogTask(activity){
//
//            @Override
//            public Object onAsync() {
//                BmobQuery<BmobLogisticsDALEx> query = new BmobQuery<BmobLogisticsDALEx>();
//                query.setLimit(page.getPagePiece());
//                query.order("-" + LogisticsDALEx.XWDATE);
//                query.findObjects(activity, new FindListener<BmobLogisticsDALEx>() {
//                    @Override
//                    public void onSuccess(List<BmobLogisticsDALEx> list) {
//                        refreshComplete();
//                        page.setPage(list.size());
//                        if (list.size() == 0) return;
//                        BmobLogisticsDALEx.get().save(list);
//                        isLoadFromService = true;
//                        data.clear();
//                        data.addAll(LoadFromLocal(page.getPagePiece(),0));
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
//                        refreshComplete();
//                        activity.onToastErrorMsg(s);
//                    }
//                });
//                return null;
//            }
//
//            @Override
//            public void onResult(Object o) {
//            }
//        };
//        ((SimpleDialogTask)task).startTask("正在加载数据,请稍候...");
    }

    /**
     * 功能描述：上拉加载更多
     **/
    private void startLoadMoreTask(){
        if(loadMoreTask!=null && loadMoreTask.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }

        //已经是最后一页
        if(page.isLastPage()) {
            loadMoreComplete();
            return ;
        }

        if(isLoadFromService){
            //从服务端加载
             BmobQuery<BmobLogisticsDALEx> query = new BmobQuery<BmobLogisticsDALEx>();
             query.setLimit(page.getPagePiece());
             query.setSkip(page.getnextPageStartIndex());
             query.order("-" + LogisticsDALEx.XWDATE);
             query.findObjects(activity, new FindListener<BmobLogisticsDALEx>() {
                   @Override
                   public void onSuccess(final List<BmobLogisticsDALEx> list) {
                         loadMoreComplete();
                         page.setPage(list.size());
                         if(list.size()==0)return;
                         BmobLogisticsDALEx.get().save(list);
                         data.addAll(BmobLogisticsDALEx.get().saveReturn(list));
                         adapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onError(int i, String s) {
                         loadMoreComplete();
                         activity.onToastErrorMsg(s);
                   }
             });
//            loadMoreTask =  new SimpleTask(){
//                @Override
//                protected Object doInBackground(final String... params) {
//                        //从服务端加载
//                        BmobQuery<BmobLogisticsDALEx> query = new BmobQuery<BmobLogisticsDALEx>();
//                        query.setLimit(page.getPagePiece());
//                        query.setSkip(page.getnextPageStartIndex());
//                        query.order("-" + LogisticsDALEx.XWDATE);
//                        query.findObjects(activity, new FindListener<BmobLogisticsDALEx>() {
//                            @Override
//                            public void onSuccess(final List<BmobLogisticsDALEx> list) {
//                                loadMoreComplete();
//                                page.setPage(list.size());
//                                if(list.size()==0)return;
//                                BmobLogisticsDALEx.get().save(list);
//                                data.addAll(BmobLogisticsDALEx.get().saveReturn(list));
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onError(int i, String s) {
//                                loadMoreComplete();
//                                activity.onToastErrorMsg(s);
//                            }
//                        });
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Object o) {
//                }
//            };
        }else{
            loadMoreTask =  new SimpleTask(){
                @Override
                protected Object doInBackground(String... params) {
                    return LoadFromLocal(page.getPagePiece(),page.getnextPageStartIndex());
                }

                @Override
                protected void onPostExecute(Object result) {
                    loadMoreComplete();
                    List<LogisticsDALEx> query=(List<LogisticsDALEx>) result;
                    data.addAll(query);
                    adapter.notifyDataSetChanged();
                    page.setPage(query.size());
                }
            };
            loadMoreTask.startTask();
        }

//        loadMoreTask.startTask();
    }

    LogisticsListAdapter.MyItemLongClickListener itemLongClick = new LogisticsListAdapter.MyItemLongClickListener() {

        @Override
        public void onItemLongClick(View view, int position) {
            final LogisticsDALEx logis = data.get(position);
            SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("确定要删除物流信息吗？");

            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();

                    BmobLogisticsDALEx bmob = new BmobLogisticsDALEx();
                    bmob.setXwstatus(LogisticsDALEx.Status_Delete);
                    bmob.update(activity, logis.getLogisticsid(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            //删除成功之后刷新一下页面
                            logis.setXwstatus(LogisticsDALEx.Status_Delete);
                            LogisticsDALEx.get().save(logis);
                            LogisticsObserver.notifyCount(getContext());
                            LogisticsObserver.notifyList(getContext());
                            activity.onToastSuccess("删除成功");
                        }

                        @Override
                        public void onFailure(int i, String msg) {
                            activity.onToastErrorMsg(msg);
                        }
                    });

//                    BmobLogisticsDALEx bmob = new BmobLogisticsDALEx();
//                    bmob.setObjectId(logis.getLogisticsid());
//                    bmob.delete(activity, new DeleteListener() {
//                        @Override
//                        public void onSuccess() {
//                            logis.deleteById(logis.getLogisticsid());
//                            LogisticsObserver.notifyCount(getContext());
//                            LogisticsObserver.notifyList(getContext());
//                            activity.onToastSuccess("删除成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            activity.onToastErrorMsg(s);
//                        }
//                    });

                }
            });
            dialog.setCancelText("取消");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                }
            });
            dialog.show();
        }

    };
}