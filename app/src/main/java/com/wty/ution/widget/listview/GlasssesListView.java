package com.wty.ution.widget.listview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wty.ution.base.ImpQuery;
import com.wty.ution.data.bmob.BmobGlassesDALEx;
import com.wty.ution.data.dalex.GlassesDALEx;
import com.wty.ution.receiver.GlassesObserver;
import com.wty.ution.task.SimpleDialogTask;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.ui.adapter.GlassesListAdapter;
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
 * 功能描述：眼镜销售信息
 * @author wty
 **/
public class GlasssesListView extends XRecyclerView implements IFilterListView {

	private boolean isInit;
    private boolean isInitLoadFromServie;
    private boolean isLoadFromService = false;
    private ListPage page;

    protected GlassesListAdapter adapter;
    public List<GlassesDALEx> data = new ArrayList<GlassesDALEx>();

    private SimpleTask task;
    private SimpleTask loadMoreTask;
    private BaseActivity activity;

	public GlasssesListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        activity = (BaseActivity)context;
        page = new ListPage(20);
        adapter = new GlassesListAdapter(context,data);
        setAdapter(adapter);
        setLoadingListener(loadingListener);
        adapter.setOnItemLongClickListener(itemLongClick);
	}

    @Override
    public void refreshService(){
        startTask();
    }

    @Override
	public void refreshDefault(){
        //重置一下页面
        page.pageReset();
        isLoadFromService = false;
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
                List<GlassesDALEx> query=(List<GlassesDALEx>) result;
                data.clear();
                data.addAll(query);
                adapter.notifyDataSetChanged();
                if(data.size()==0){
                    //本地没有数据，从服务端拿数据
                    if(!isInitLoadFromServie){
                        refreshService();
                        isInitLoadFromServie = true;
                    }
                }else{
                    page.setPage(query.size());
                }
            }
        };

        task.startTask();
	}

	public void initData(){
		if(!isInit){
			if(emptyListener!=null)emptyListener.onNormal();
			refreshDefault();
			isInit = true;
        }
	}

    /**
     * 功能描述：从本地获取数据,20条
     **/
    protected List<GlassesDALEx> LoadFromLocal(int limit,int offset){
        return GlassesDALEx.get().queryLimitByOffset(limit,offset);
    }

    protected OnDataEmptyListener emptyListener;
    public void setOnDataEmptyListener(OnDataEmptyListener emptyListener) {
        this.emptyListener = emptyListener;
    }

    LoadingListener loadingListener = new LoadingListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            startTask(new ImpQuery<BmobGlassesDALEx>(){
                @Override
                public void onSuccess(List<BmobGlassesDALEx> var1) {

                }

                @Override
                public void onError(int var1, String var2) {

                }
            });

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
    private void startTask(final ImpQuery listener){
        if(task!=null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(false);
        }
        //重置页数
        page.pageReset();

//        activity.loading("正在加载数据,请稍候...");
        BmobQuery<BmobGlassesDALEx> query = new BmobQuery<BmobGlassesDALEx>();
        BmobQuery.clearAllCachedResults(activity);
        query.setLimit(page.getPagePiece());
        query.order("-" + GlassesDALEx.XWDATE);
        query.findObjects(activity, new FindListener<BmobGlassesDALEx>() {
            @Override
            public void onSuccess(List<BmobGlassesDALEx> list) {
//                activity.dismissLoading();
                listener.onSuccess(list);
                refreshComplete();
                page.setPage(list.size());
                if (list.size() == 0) return;
                BmobGlassesDALEx.get().save(list);
                isLoadFromService = true;
                data.clear();
                data.addAll(LoadFromLocal(page.getPagePiece(), 0));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                refreshComplete();
                listener.onError(i,s);
//                activity.dismissLoading();
                activity.onToastErrorMsg(s);
            }
        });

//        task =  new SimpleDialogTask(activity){
//
//            @Override
//            public Object onAsync() {
//                BmobQuery<BmobGlassesDALEx> query = new BmobQuery<BmobGlassesDALEx>();
//                BmobQuery.clearAllCachedResults(activity);
//                query.setLimit(page.getPagePiece());
//                query.order("-" + GlassesDALEx.XWDATE);
//                query.findObjects(activity, new FindListener<BmobGlassesDALEx>() {
//                    @Override
//                    public void onSuccess(List<BmobGlassesDALEx> list) {
//                        refreshComplete();
//                        page.setPage(list.size());
//                        if (list.size() == 0) return;
//                        BmobGlassesDALEx.get().save(list);
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
            BmobQuery<BmobGlassesDALEx> query = new BmobQuery<BmobGlassesDALEx>();
            query.setLimit(page.getPagePiece());
            query.setSkip(page.getnextPageStartIndex());
            query.order("-" + GlassesDALEx.XWDATE);
            query.findObjects(activity, new FindListener<BmobGlassesDALEx>() {
                @Override
                public void onSuccess(final List<BmobGlassesDALEx> list) {
                    loadMoreComplete();
                    page.setPage(list.size());
                    if(list.size()==0)return;
//                            BmobGlassesDALEx.get().save(list);
                    data.addAll(BmobGlassesDALEx.get().saveReturn(list));
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
//                    //从服务端加载
//                    BmobQuery<BmobGlassesDALEx> query = new BmobQuery<BmobGlassesDALEx>();
//                    query.setLimit(page.getPagePiece());
//                    query.setSkip(page.getnextPageStartIndex());
//                    query.order("-" + GlassesDALEx.XWDATE);
//                    query.findObjects(activity, new FindListener<BmobGlassesDALEx>() {
//                        @Override
//                        public void onSuccess(final List<BmobGlassesDALEx> list) {
//                            loadMoreComplete();
//                            page.setPage(list.size());
//                            if(list.size()==0)return;
////                            BmobGlassesDALEx.get().save(list);
//                            data.addAll(BmobGlassesDALEx.get().saveReturn(list));
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//                            loadMoreComplete();
//                            activity.onToastErrorMsg(s);
//                        }
//                    });
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
                    List<GlassesDALEx> query=(List<GlassesDALEx>) result;
                    data.addAll(query);
                    adapter.notifyDataSetChanged();
                    page.setPage(query.size());
                }
            };
            loadMoreTask.startTask();
        }

//        loadMoreTask.startTask();
    }

    GlassesListAdapter.MyItemLongClickListener itemLongClick = new GlassesListAdapter.MyItemLongClickListener() {

        @Override
        public void onItemLongClick(View view, int position) {
            final GlassesDALEx dalex = data.get(position);
            SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("确定要删除此条眼镜销售信息吗？");

            dialog.setConfirmText("确定");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();

                    BmobGlassesDALEx bmob = new BmobGlassesDALEx();
                    bmob.setXwstatus(GlassesDALEx.Status_Delete);
                    bmob.update(activity, dalex.getGlassesid(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            //编辑成功之后刷新一下页面
                            dalex.setXwstatus(GlassesDALEx.Status_Delete);
                            GlassesDALEx.get().save(dalex);
                            GlassesObserver.notifyCount(getContext());
                            GlassesObserver.notifyList(getContext());
                            activity.onToastSuccess("删除成功");
                        }

                        @Override
                        public void onFailure(int i, String msg) {
                            activity.onToastErrorMsg(msg);
                        }
                    });

//                    BmobGlassesDALEx bmob = new BmobGlassesDALEx();
//                    bmob.setObjectId(dalex.getGlassesid());
//                    bmob.delete(activity, new DeleteListener() {
//                        @Override
//                        public void onSuccess() {
//                            dalex.deleteById(dalex.getGlassesid());
//                            GlassesObserver.notifyCount(getContext());
//                            GlassesObserver.notifyList(getContext());
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