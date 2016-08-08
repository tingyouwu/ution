package com.xtion.sheet.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtion.sheet.DataSet;
import com.xtion.sheet.FixedGridLayoutManager3;
import com.xtion.sheet.InsetDecoration;
import com.xtion.sheet.R;
import com.xtion.sheet.SelfRemovingOnScrollListener;
import com.xtion.sheet.SheetRecyclerView;
import com.xtion.sheet.SimpleAdapter;
import com.xtion.sheet.ViewSet;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.adapter.IndexAdapter;

/**
 * Created by 廖东明 on 2015-9-29.
 *
 * use a builder to build the sheet, 负责把各大块拼起来，避免在这里进行逻辑处理
 *
 * never used it xml
 */
public class InnerSheetView extends LinearLayout{



    //顶部和左边都有冻结的行列
    public static final int STYLE_FULL = 0;
    //只有顶部有冻结的行
    public static final int STYLE_TOP = 1;
    //只有左边有冻结的列
    public static final int STYLE_LEFT = 2;
    //完全没有冻结的行列
    public static final int STYLE_NONE = 3;

    ////////////////////builder parameter////start///////////////////////////
    //保证private SheetView()该构造函数中一定有下面几个参数
    /**
     * builder parameters  ---  required
     */
    private int myStyle;       //style
    private DataSet dataSet;
    private ViewSet viewSet;
    ////////////////////builder parameter////end///////////////////////////
    private RelativeLayout sheetLayout = null;

    private int rowCount = 0;
    private int columnCount;   //列数
    private int totalCellCount;//所有cells数量

    private SheetRecyclerView mainList = null;
    private SheetRecyclerView topList = null;
    private SheetRecyclerView leftList = null;

    FixedGridLayoutManager3 mainManager;
    TextView labelView;
    RelativeLayout labalLayout;
    AlphaAdapter mainAdapter;


    LinearLayoutManager linearLayoutManagerHZ;
    LinearLayoutManager verticalLayoutManager;
    IndexAdapter horizontalAdapter;
    IndexAdapter verticalAdapter;

    //注释
    /**
     * 处理的触控事件有两种：
     * 1.点击
     * 2.滑动
     *
     * 1.当滑动的时候，SelfRemovingOnScrollListener会自己处理scroll的事件，mainList停止scroll的时候
     * 会自动remove OnScrollListener
     * 2.点击事件则需要添加下面的scrollOccur变量来辅助在适当的时候remove OnScrollListener
     * 否则会导致mainList和topList， LeftList scrollList滑动的距离不一致！
     */
    private boolean scrollOccur = false;

    private final RecyclerView.OnScrollListener mTopRightSL = new SelfRemovingOnScrollListener() {

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //once scrolled ,no focus item
            mainAdapter.setCurrentFocusPos(-1);

            if(topList != null) {
                topList.scrollBy(dx, 0);
            }
            if(leftList != null) {
                leftList.scrollBy(0, dy);
            }

            scrollOccur = true;
        }
    };

    private final RecyclerView.OnScrollListener mTopSL = new SelfRemovingOnScrollListener() {

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //once scrolled ,no focus item
            //mainAdapter.setCurrentFocusPos(-1);

            if(mainList != null) {
                mainList.scrollBy(dx, 0);
            }

            scrollOccur = true;
        }
    };

    private final RecyclerView.OnScrollListener mLeftSL = new SelfRemovingOnScrollListener() {

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //once scrolled ,no focus item
            //mainAdapter.setCurrentFocusPos(-1);

            if(mainList != null) {
                mainList.scrollBy(0, dy);
            }

            scrollOccur = true;
        }
    };

    //由于继承了LinearLayout,而LinearLayout要求必须有context
    public InnerSheetView(Context context,DataSet dataSet, ViewSet viewSet) {
        //call the father's constructor
        super(context);

        ////builder parameters
        // required
        myStyle = dataSet.getSheetStyle();
        this.dataSet = dataSet;
        ///其他数据譬如Data，cell的类型,背景颜色等等
        ///optional
        this.viewSet = viewSet;

        columnCount = dataSet.getColumnCount();
        rowCount = dataSet.getRowCount();
        totalCellCount = dataSet.getTotalCellCounts();

        int resource = -1;
        switch (myStyle) {
            case STYLE_FULL:
                resource = R.layout.raw_sheet_full;
                break;
            case STYLE_TOP:
                resource = R.layout.raw_sheet_top;
                break;
            case STYLE_LEFT:
                resource = R.layout.raw_sheet_left;
                break;
            case STYLE_NONE:
                resource = R.layout.raw_sheet_none;
                break;
            default:
                break;
        }
        sheetLayout = (RelativeLayout) LayoutInflater.from(context).inflate(resource, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initRecyclerViews();
        this.addView(sheetLayout,params);
    }

    private void initRecyclerViews() {
        //1.init main list
        mainList = (SheetRecyclerView) sheetLayout.findViewById(R.id.section_list);
        FixedGridLayoutManager3 manager = new FixedGridLayoutManager3();

        //adapter interact with dataset
        mainAdapter = new AlphaAdapter(mainList,dataSet,viewSet);
        mainList.setAdapter(mainAdapter);

        //manager use pixel
        //our custom manager is so special , so we have got to set some data explicitly
        manager.setInitData(columnCount, totalCellCount);
        manager.setWidthColumnArray(mainAdapter.getRowsHeight(), mainAdapter.getColumnsWidth());
        mainList.setLayoutManager(manager);

        //shut off the animations,否则会为了产生动画，每次notifyItemChanged 的时候会有闪屏的情况出现
        //关闭动画
        if(mainList.getItemAnimator() instanceof SimpleItemAnimator){
            ((SimpleItemAnimator)mainList.getItemAnimator()).setSupportsChangeAnimations(false);
        }

        mainList.addItemDecoration(getItemDecoration());

        mainList.getItemAnimator().setAddDuration(1000);
        mainList.getItemAnimator().setChangeDuration(1000);
        mainList.getItemAnimator().setMoveDuration(1000);
        mainList.getItemAnimator().setRemoveDuration(1000);

        //TouchListener
        mainList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull final
            MotionEvent e) {
                final Boolean ret = rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;
                if (!ret) {
                    onTouchEvent(rv, e);
                }
                return Boolean.FALSE;
            }

            @Override
            public void onTouchEvent(@NonNull final RecyclerView rv,
                                     @NonNull final MotionEvent e) {
                //一次触控事件的开始
                scrollOccur = false;

                if (e.getAction() == MotionEvent.ACTION_DOWN) {

                    if (topList != null) {
                        if (topList.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            rv.addOnScrollListener(mTopRightSL);
                        }
                    } else if (leftList != null) {
                        if (leftList.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            rv.addOnScrollListener(mTopRightSL);
                        }
                    }
                }

                if (e.getAction() == MotionEvent.ACTION_UP) {
                    //一次触控事件的结束
                    rv.clearOnScrollListeners();
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {

            }
        });

        if(this.myStyle == STYLE_FULL){

            labelView = (TextView)sheetLayout.findViewById(R.id.section_label);
            labelView.setText(TextUtils.isEmpty(dataSet.getLabel())?"":dataSet.getLabel());

            labelView.setBackgroundColor(getResources().getColor(viewSet.columnBgColor));
            labelView.setTextColor(getResources().getColor(viewSet.columnTextColor));

            labalLayout = (RelativeLayout)sheetLayout.findViewById(R.id.section_label_layout);
            labalLayout.setLayoutParams(new RelativeLayout.LayoutParams(dataSet.getIndexWidth(),dataSet.getIndexHeight()));
        }

        //2.init top list
        if(this.myStyle != STYLE_LEFT && this.myStyle != STYLE_NONE) {

            topList = (SheetRecyclerView) sheetLayout.findViewById(R.id.section_list_horizontal_top);
            linearLayoutManagerHZ = new LinearLayoutManager(this.getContext(),
                    LinearLayoutManager.HORIZONTAL,false);
            topList.setLayoutManager(linearLayoutManagerHZ);
            topList.addItemDecoration(getItemDecoration());

            //关闭动画
            if(topList.getItemAnimator() instanceof SimpleItemAnimator){
                ((SimpleItemAnimator)topList.getItemAnimator()).setSupportsChangeAnimations(false);
            }
            topList.getItemAnimator().setAddDuration(1000);
            topList.getItemAnimator().setChangeDuration(1000);
            topList.getItemAnimator().setMoveDuration(1000);
            topList.getItemAnimator().setRemoveDuration(1000);

            //column
            horizontalAdapter = new IndexAdapter(topList,dataSet,viewSet, IndexAdapter.HORIZONTAL_TYPE);
            //hzAdapter.setOnItemClickListener(this);
            topList.setAdapter(horizontalAdapter);
            topList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                @Override
                public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull final
                MotionEvent e) {
                    final Boolean ret = rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
                    if (ret) {
                        onTouchEvent(rv, e);
                    }
                    return Boolean.FALSE;
                }

                @Override
                public void onTouchEvent(@NonNull final RecyclerView rv,
                                         @NonNull final MotionEvent e) {
                    //一次触控事件的开始
                    scrollOccur = false;

                    if (e.getAction() == MotionEvent.ACTION_DOWN) {

                        if (mainList != null) {
                            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                                mainList.stopScroll();
                                rv.addOnScrollListener(mTopSL);
                            }
                        }
                    }

                    if (e.getAction() == MotionEvent.ACTION_UP) {
                        //一次触控事件的结束
                        rv.clearOnScrollListeners();
                    }
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {

                }
            });

        }

        //3.init left list
        if(this.myStyle != STYLE_TOP && this.myStyle != STYLE_NONE) {
            leftList = (SheetRecyclerView) sheetLayout.findViewById(R.id.section_list_vertical_left);
            verticalLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            leftList.setLayoutManager(verticalLayoutManager);
            leftList.addItemDecoration(getItemDecoration());

            //关闭动画
            if(leftList.getItemAnimator() instanceof SimpleItemAnimator){
                ((SimpleItemAnimator)leftList.getItemAnimator()).setSupportsChangeAnimations(false);
            }
            leftList.getItemAnimator().setAddDuration(1000);
            leftList.getItemAnimator().setChangeDuration(1000);
            leftList.getItemAnimator().setMoveDuration(1000);
            leftList.getItemAnimator().setRemoveDuration(1000);

            verticalAdapter = new IndexAdapter(leftList,dataSet,viewSet, SimpleAdapter.VERTICAL_TYPE);
            leftList.setAdapter(verticalAdapter);

//            RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
//            leftList.addOnItemTouchListener(disabler);
            leftList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                @Override
                public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull final
                MotionEvent e) {
                    final Boolean ret = rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
                    if (ret) {
                        onTouchEvent(rv, e);
                    }
                    return Boolean.FALSE;
                }



                @Override
                public void onTouchEvent(@NonNull final RecyclerView rv,
                                         @NonNull final MotionEvent e) {
                    //一次触控事件的开始
                    scrollOccur = false;

                    if (e.getAction() == MotionEvent.ACTION_DOWN) {

                        if (mainList != null) {

                            if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                                mainList.stopScroll();
                            rv.addOnScrollListener(mLeftSL);
                            }
                        }
                    }

                    if (e.getAction() == MotionEvent.ACTION_UP) {
                        //一次触控事件的结束
                        rv.clearOnScrollListeners();
                    }
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {

                }
            });
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration(){
        return new InsetDecoration(this.getContext());
    }

    public void setMainListItemClickListener(AlphaAdapter.OnCellClickListener listener) {
        if(mainAdapter != null && mainList != null) {
            mainAdapter.setOnItemClickListener(listener);
        }
    }

    public void setLeftItemClickListener(AlphaAdapter.OnRowClickListener listener){
        if(leftList!=null && verticalAdapter!=null){
            verticalAdapter.setOnRowClickListener(listener);
        }
    }

    public void setTopItemClickListener(AlphaAdapter.OnColumnClickListener listener){
        if(topList!=null && horizontalAdapter!=null){
            horizontalAdapter.setOnColumnClickListener(listener);
        }
    }

    public void updateItemData(int position, String text) {

//        //first should update the cell rect
//        dataSet.updateCellRect(position, text);
//
//        mainAdapter.updateItemdata(position, text);
//        verticalAdapter.updateData(position);
    }

    public IndexAdapter getLeftAdapter(){
        return verticalAdapter;
    }

    public IndexAdapter getTopAdapter(){
        return horizontalAdapter;
    }

    public AlphaAdapter getMainAdapter() {
        return mainAdapter;
    }

    public int getItemType(int position) {
        return mainAdapter.getItemViewType(position);
    }

    //disable touch event
    public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

