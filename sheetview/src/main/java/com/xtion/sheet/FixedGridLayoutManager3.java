package com.xtion.sheet;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;


/**
 * Created by Thomas Liao on 2015-9-16.
 */

//warning : we are not supporting animations yet , so not to worry preLayout() issues


public class FixedGridLayoutManager3 extends SheetRecyclerView.LayoutManager{

    private static boolean debug = true;

    /* Fill Direction Constants */
    private static final int DIRECTION_NONE = -1;
    private static final int DIRECTION_START = 0;/////scroll right
    private static final int DIRECTION_END = 1;///////scroll left
    private static final int DIRECTION_UP = 2;////////scroll down
    private static final int DIRECTION_DOWN = 3;//////scroll up

    /*
    这三个值需要我们实时维护
    在函数updateWindowSizing()中
     */
    //在可见范围内的第一个view在adapter中的位置
    private int mFirstVisiblePosition = 0;   //start from zero
    private int mVisibleColumnCount = 0;
    private int mVisibleRowCount = 0;

    //
//    private int mMaxVisibleColumnCount = 0;
//    private int mMaxVisibleRowCount = 0;

    private static final String TAG = "FixedGridLayoutManager3";

    //data set used to calculate the visible row and column counts for layout usage
    //we should not depends on this to decide the position of the cell，表示cell的可见宽高(不一定等于cell的宽高)
    //the cells' width and height should be set in the adapter ,
    // this layoutmanager only layout the cells according these arrays
    private int[] columnsWidth;
    private int[] rowsHeight;

    int totalCellCounts = -1;
    int rowCount = -1;
    int columnCount = -1;

    boolean vertical = true;
    boolean horizontal = true;

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount);

        Log.d("Thomas", "items changed start position: " + positionStart + "  count: " + itemCount);

        //我们在这里处理动态调整高度的问题！



    }


    /**
     * Set the number of columns the layout manager will use. This will
     * trigger a layout update.
     *special case special solution
     */
    public void setInitData(int count, int totalCount) {

        //测试中，这些数据应该放在adapter中去处理，现在是为了测试
        totalCellCounts = totalCount;

        columnCount = count;
        rowCount  = totalCellCounts/columnCount;

//        //data set of all the cell's height and width in the sheet
//        columnsWidth = new int[columnCount];
//        rowsHeight = new int[rowCount];
//
//        //init value
//        for(int i = 0;i < columnCount; i++) {
//            columnsWidth[i] = cellW;
//        }
//
//        for(int j = 0;j < rowCount; j++) {
//            rowsHeight[j] = cellH;
//        }
    }

    public void setWidthColumnArray(int[] rows, int[] columns) {
        rowsHeight = rows;
        columnsWidth = columns;
    }

    // TODO: 2015-10-12  delete this function
    public void setTotalColumnCount(int count, int totalCount, int cellW, int cellH) {

        //测试中，这些数据应该放在adapter中去处理，现在是为了测试
        totalCellCounts = totalCount;

        columnCount = count;
        rowCount  = totalCellCounts/columnCount;

        //data set of all the cell's height and width in the sheet
        columnsWidth = new int[columnCount];
        rowsHeight = new int[rowCount];

        //init value
        for(int i = 0;i < columnCount; i++) {
            columnsWidth[i] = cellW;
        }

        for(int j = 0;j < rowCount; j++) {
            rowsHeight[j] = cellH;
        }
    }

    public void testingSizingPlus() {
        //rowsHeight[8] += 20;
        //columnsWidth[8] += 20;
    }

    public void testingSizingMinus() {
        //rowsHeight[8] -= 20;
        //columnsWidth[8] -= 20;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(!horizontal) {
            return 0;
        }

        if (getChildCount() == 0) {
            return 0;
        }

        //Take leftmost measurements from the top-left child
        final View topView = getChildAt(0);
        //Take rightmost measurements from the top-right child
        final View bottomView = getChildAt(mVisibleColumnCount - 1);

        //Optimize the case where the entire data set is too small to scroll
        int viewSpan = getDecoratedRight(bottomView) - getDecoratedLeft(topView);
        if (viewSpan < getHorizontalSpace()) {
            //We cannot scroll in either direction
            return 0;
        }

        int delta;
        boolean leftBoundReached = getFirstVisibleColumn() == 0;
        boolean rightBoundReached = getLastVisibleColumn() >= getTotalColumnCount();
        if (dx > 0) { // Contents are scrolling left
            //Check right bound
            if (rightBoundReached) {
                //If we've reached the last column, enforce limits
                int rightOffset = getHorizontalSpace() - getDecoratedRight(bottomView) + getPaddingRight();
                delta = Math.max(-dx, rightOffset);
            } else {
                //No limits while the last column isn't visible
                delta = -dx;
            }
        } else { // Contents are scrolling right
            //Check left bound
            if (leftBoundReached) {
                int leftOffset = -getDecoratedLeft(topView) + getPaddingLeft();
                delta = Math.min(-dx, leftOffset);
            } else {
                delta = -dx;
            }
        }

        offsetChildrenHorizontal(delta);

        ////  ,这些是为了获取第一个在visible区域显示的view在adapter中的index
        if (dx > 0) {
            if (getDecoratedRight(topView) < 0 && !rightBoundReached) {
                scrollingFill(DIRECTION_END, recycler, state);
            } else if (!rightBoundReached) {
                scrollingFill(DIRECTION_NONE, recycler, state);
            }
        } else {
            if (getDecoratedLeft(topView) > 0 && !leftBoundReached) {
                scrollingFill(DIRECTION_START, recycler, state);
            } else if (!leftBoundReached) {
                scrollingFill(DIRECTION_NONE, recycler, state);
            }
        }

        /*
         * Return value determines if a boundary has been reached
         * (for edge effects and flings). If returned value does not
         * match original delta (passed in), RecyclerView will draw
         * an edge effect.
         */
        //return -delta;


        return dx;
    }


    /**
     * Scroll vertically by dy pixels in screen coordinates and return the distance traveled.
     * The default implementation does nothing and returns 0.
     *
     * @param dy            distance to scroll in pixels. Y increases as scroll position
     *                      approaches the bottom.
     * @param recycler      Recycler to use for fetching potentially cached views for a
     *                      position
     * @param state         Transient state of RecyclerView
     * @return The actual distance scrolled. The return value will be negative if dy was
     * negative and scrolling proceeeded in that direction.
     * <code>Math.abs(result)</code> may be less than dy if a boundary was reached.
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(!vertical) {
            return 0;
        }

        boolean isSmoothScrolling = isSmoothScrolling();
        if(debug) {
            Log.d(TAG, " onScrollStateChanged dy : " + dy);
        }
        //if no child views was attached to RecyclerView
        if (getChildCount() == 0) {
            return 0;
        }

        //Take top measurements from the top-left child
        final View topView = getChildAt(0);
        //Take bottom measurements from the bottom-right child.
        final View bottomView = getChildAt(getChildCount()-1);
        int childCount = getChildCount();

        //Optimize the case where the entire data set is too small to scroll
        int viewSpan = getDecoratedBottom(bottomView) - getDecoratedTop(topView);
        if(debug) {
            Log.d(TAG, "current vertical space span is : " + viewSpan +
                    " childCount: " + childCount + " firsPos: " + mFirstVisiblePosition);
        }

        if((viewSpan - childCount/4*200) != 0) {
            //Log.wtf(TAG, " span result error!!! Plz,must check!");
        }

        int space = getVerticalSpace();
        if (viewSpan < getVerticalSpace()) {
            //We cannot scroll in either direction
            return 0;
        }

        int delta;
        //返回整个表格的行数
        int maxRowCount = getTotalRowCount();

        boolean topBoundReached = (getFirstVisibleRow() == 0);
        boolean bottomBoundReached = (getLastVisibleRow() >= maxRowCount);

        if (dy > 0) {//contents are scrolling up
            if(bottomBoundReached) {
                //If we've reached the last row, enforce limits
                int bottomOffset = 0;
                if (rowOfIndex(getChildCount() - 1) >= (maxRowCount - 1)) {
                    //We are truly at the bottom, determine how far
                    bottomOffset = getVerticalSpace() - getDecoratedBottom(bottomView)
                            + getPaddingBottom();
                } else {
                    Log.wtf(TAG, "shall we do something?");
                }

                delta = Math.max(-dy, bottomOffset);
            } else {
                //No limits while the last row isn't visible
                delta = -dy;
            }

        } else {
            //Check against top bound
            if (topBoundReached) {
                int topOffset = -getDecoratedTop(topView) + getPaddingTop();

                delta = Math.min(-dy, topOffset);
            } else {
                delta = -dy;
            }
        }


        //找到第一个位置
        final View view = getChildAt(0);
        //计算出左边和顶部的左边位置，也就知道了被遮盖住了多少
        int startLeftOffset = getDecoratedLeft(view);
        int startTopOffset = getDecoratedTop(view);

        if(debug) {
            Log.d(TAG, "offsetChildrenVertical ***************************************************");
            Log.d(TAG, "before offsetChildrenVertical [left,top]: [" + startLeftOffset + "," +
                    startTopOffset +
                    "]   " + view.toString());
        }

        /*
        Offset all child views attached to the parent RecyclerView by dy pixels along
        the vertical axis.
         */
        offsetChildrenVertical(delta);

        if(debug) {
            Log.d(TAG, "offsetChildrenVertical [expected dy,actual delta] : [ " + dy + " , " + delta +
                    " ]");
        }

        final View view2 = getChildAt(0);
        //计算出左边和顶部的左边位置，也就知道了被遮盖住了多少
        startLeftOffset = getDecoratedLeft(view2);
        startTopOffset = getDecoratedTop(view2);

        if(debug) {
            Log.d(TAG, "after offsetChildrenVertical [left,top]: [" + startLeftOffset + "," +
                    startTopOffset +
                    "]   " + view2.toString());
        }

        //如果 1.到了顶部继续向下滑 2.到了底部继续向上滑， 那么不会执行以下程序块
        //DIRECTION_DOWN DIRECTION_UP ,这些参数是为了获取第一个在visible区域显示的view在adapter中的index
        if (dy > 0) {//contents are scrolling up
            if (getDecoratedBottom(topView) < 0 && !bottomBoundReached) {
                //Log.d(TAG, "Thomas scrolling down direction!");
                scrollingFill(DIRECTION_DOWN, recycler, state);
            } else if (!bottomBoundReached) {
                scrollingFill(DIRECTION_NONE, recycler, state);
            }
        } else {
            if (getDecoratedTop(topView) > 0 && !topBoundReached) {
                scrollingFill(DIRECTION_UP, recycler, state);
            } else if (!topBoundReached) {
                scrollingFill(DIRECTION_NONE, recycler, state);
            }
        }
         /*
         * Return value determines if a boundary has been reached
         * (for edge effects and flings). If returned value does not
         * match original delta (passed in), RecyclerView will draw
         * an edge effect.
         */
        //return -delta;

        return dy;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // TODO: 2015-9-16
        if(state.isPreLayout()) {
            //we are not going to handling this case,cause,we are not using animation
        }

        ///

        int childCounts = getChildCount();
        if(childCounts == 0) {
            //first layout all the children in the recyclerView
            //1.第一次初始化的时候会调用onLayoutChildren
            mFirstVisiblePosition = 0;
            firstFill(recycler, state, 0, 0);

        } else {
            //2.item变化的时候也调用该函数,adapter.notifyItemChanged

            int leftOffset = 0;
            int topOffset = 0;
            if(getChildCount() != 0) {
                //计算出左边和顶部的左边位置，也就知道了被遮盖住了多少,
                final View topChild = getChildAt(0);
                leftOffset = getDecoratedLeft(topChild);//被遮住的左边的宽度，为负数
                topOffset = getDecoratedTop(topChild);//被遮住的高度，为负数
            }




            //remove all views,so that we would reload all the views
            /**
             * Remove all views from the currently attached RecyclerView. This will not recycle
             * any of the affected views; the LayoutManager is responsible for doing so if desired.
             *
             * item 的长或者宽有变化，我们需要先删除attached的views ，再重新计算view再attach 上去
             */

            // removeAllViews();

            removeAndRecycleAllViews(recycler);//remove all views

            recycler.clear();//clear views in the cache



            firstFill(recycler, state, leftOffset, topOffset);
        }
    }


    /**
     * cause our items would change sometimes, so we have to update the data contiuously
     * this function should be call after fistFill() finishing filling child views
     */
    //这个函数是用来维护可视区域行和列的，只要保证  行*列  所占的面积大于可是区域的面积
    //即可，其他一概不管！
    //// TODO: 2015-9-25
    //为了简化算法，我们限制固定为10*5, 以后我们要计算出最小的行和列
    private void updateWindowSizing() {

        if(debug) {
            Log.d(TAG, "Thomas -------------------divider----------------------");
        }
        int leftOffset,topOffset;
        int pos;

        if(getChildCount() != 0) {
            //计算出左边和顶部的左边位置，也就知道了被遮盖住了多少,
            final View topChild = getChildAt(0);
            leftOffset = getDecoratedLeft(topChild);//被遮住的左边的宽度，为负数
            topOffset = getDecoratedTop(topChild);//被遮住的高度，为负数
            pos = mFirstVisiblePosition;
        } else {
            leftOffset = 0;
            topOffset = 0;
            pos = 0;
        }

        //无论offset是多少，我们都用recyclerView的宽和高来计算当前能显示的行列
        //we do some initial work
        int verticalSpace = getVerticalSpace();
        int horizontalSpace = getHorizontalSpace();

        //表格的列数
        int columnCount = getTotalColumnCount();

        //直接数组访问，这样速度很快
        int i = 0;
        int row = pos / columnCount; ///start from zero

        if(debug) {
            Log.d(TAG, "updateWindowSizing start from row: " + row + " mFirstVisiblePosition is: "
                    + mFirstVisiblePosition + " topView topoffset: " + topOffset);
        }

        //滑动的幅度小，由这个来处理！
        int absOffset = Math.abs(topOffset);
        if( absOffset <  rowsHeight[row]) {
            if(topOffset > 0) {
                verticalSpace += topOffset;
            } else {
                verticalSpace -= topOffset;
            }
        }



        while(verticalSpace > 0 && row + i < rowCount ) {
            //
            verticalSpace -= rowsHeight[row + i];
            i++;
        }

        if(row + i >= getTotalRowCount()) {//如果滑到了底部就不能增加1
            mVisibleRowCount = i;
        } else {
            // 变大一点可以减少在fling情况下的白屏面积
            //但是也会导致太多view了，滑动过程中反而也会产生更多白屏面积
            //要找个平衡点
            //有无可避免的情况出现，之所以增加到3是因为某些单独的cell高度
            //可能太高，需要预留一点空白出来,需要注意，以后动态变化的单元格一定不能超过这个值！
            mVisibleRowCount = i + 1; //
        }


        //直接数组访问，这样速度很快
        int j = 0;
        int column = pos % columnCount; //start from zero
        while(horizontalSpace > 0 && column + j < columnCount) {
            horizontalSpace -= columnsWidth[column + j];
            j++;
        }

        if(column + j >= getTotalColumnCount()) {//到了最右边不能增加1
            mVisibleColumnCount = j;
        } else {
            // 变大一点可以减少在fling情况下的白屏面积
            //但是也会导致太多view了，滑动过程中反而也会产生更多白屏面积
            //要找个平衡点
            mVisibleColumnCount = j + 1;//
        }
        mVisibleColumnCount = 5;
        mVisibleRowCount = 10;

        if(debug) {
            Log.d(TAG, "updateWindowSizing visible row , col is: " + mVisibleRowCount + " ," +
                    mVisibleColumnCount);
        }
    }

    private void initWindowSize() {
        //we do some initial work
        int verticalSpace = getVerticalSpace();
        int horizontalSpace = getHorizontalSpace();

        int[] copyHeights = Arrays.copyOf(rowsHeight, rowsHeight.length);
        int[] copyWidthes = Arrays.copyOf(columnsWidth, columnsWidth.length);

        Arrays.sort(copyHeights);
        Arrays.sort(copyWidthes);

        int i = 0;
        while (verticalSpace > 0 && i < rowCount) {
            verticalSpace -= copyHeights[i];
            i++;
        }

        if (i >= getTotalRowCount()) {//如果滑到了底部就不能增加1
            mVisibleRowCount = i;
        } else {
            // 变大一点可以减少在fling情况下的白屏面积
            //但是也会导致太多view了，滑动过程中反而也会产生更多白屏面积
            //要找个平衡点
            //有无可避免的情况出现，之所以增加到3是因为某些单独的cell高度
            //可能太高，需要预留一点空白出来,需要注意，以后动态变化的单元格一定不能超过这个值！
            mVisibleRowCount = i + 1; //
        }

        int j = 0;
        while (horizontalSpace > 0 && j < columnCount) {
            horizontalSpace -= copyWidthes[j];
            j++;
        }

        if (j >= getTotalColumnCount()) {//到了最右边不能增加1
            mVisibleColumnCount = j;
        } else {
            // 变大一点可以减少在fling情况下的白屏面积
            //但是也会导致太多view了，滑动过程中反而也会产生更多白屏面积
            //要找个平衡点
            mVisibleColumnCount = j + 1;//
        }

    }

    /**
     * init the layout, fill the visible are with child views
     *
     * @param recycler
     * @param state
     */
    private void firstFill(RecyclerView.Recycler recycler, RecyclerView.State state,
                           int addLeftOffset, int addTopOffset) {

        initWindowSize();


        /*
         * First, we will detach all existing views from the layout.
         * detachView() is a lightweight operation that we can use to
         * quickly reorder views without a full add/remove.
         */
        int startLeftOffset = getPaddingLeft() + addLeftOffset;//recyclerView的paddingleft
        int startTopOffset = getPaddingTop() + addTopOffset;//recyclerView 的paddingTop

        //updateWindowSizing();
        //我们可以知道在可见区域的第一个view在adapter中的pos
        //View view = getChildAt(0);
        int visibleChildCount = getVisibleChildCount();

        if(debug) {
            Log.d(TAG, " visible child count is: " + visibleChildCount);
        }

        int leftOffset = startLeftOffset;
        int topOffset = startTopOffset;

        int mDecoratedChildWidth = 0;
        int mDecoratedChildHeight = 0;

        for(int i = 0; i < visibleChildCount; i++) {
            //找到对应i的那个view在adapter中对应的pos
            int nextPosition = positionOfIndex(i);

            //把view 加到recyclerView中去
            View view = recycler.getViewForPosition(nextPosition);
            addView(view);

             /*
                 * Update the new view's metadata, but only when this is a real
                 * layout pass. 初始化一些数据
                 */
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.row = getGlobalRowOfPosition(nextPosition);
            lp.column = getGlobalColumnOfPosition(nextPosition);

            /*
             * It is prudent明智的 to measure/layout each new view we
             * receive from the Recycler. We don't have to do
             * this for views we are just re-arranging.
             */
            measureChildWithMargins(view, 0, 0);
            int left,top,right,bottom;

            mDecoratedChildWidth = getDecoratedMeasuredWidth(view);
            mDecoratedChildHeight = getDecoratedMeasuredHeight(view);

            //public void layoutDecorated(View child, int left, int top, int right, int bottom)
//            mDecoratedChildWidth = columnsWidth[nextPosition % columnCount];
//            mDecoratedChildHeight = rowsHeight[nextPosition / columnCount];

            //第一次从adapter中获取view，同时我们用columnsWidth等两个数组记录下来宽高
            //这个记录的是cell view真正的宽高！
            //remember the rect for later calculating
            columnsWidth[nextPosition % columnCount] = mDecoratedChildWidth;
            rowsHeight[nextPosition / columnCount] = mDecoratedChildHeight;

            left = leftOffset;
            top = topOffset;
            right = leftOffset + mDecoratedChildWidth;
            bottom = topOffset + mDecoratedChildHeight;

            if(debug) {
                Log.d(TAG, "layoutDecorated" + nextPosition + "view [left,top,right,bottom]: " + "[" + left + "," +
                        top + "," +
                        right + "," + bottom + "]");
            }

            layoutDecorated(view, left, top, right, bottom);//Lay out the given child view within the RecyclerView using coordinates

            if (i % mVisibleColumnCount == (mVisibleColumnCount - 1)) {
                //当时列的最后一个的时候记录下一行第一个cell的[left,top]
                leftOffset = startLeftOffset;
                topOffset += mDecoratedChildHeight;
            } else {
                //否则就直接增加left的位置
                leftOffset += mDecoratedChildWidth;
            }
        }
    }

    //这个函数只处理缓慢滑动
    private void scrollingFill(int direction,
                               RecyclerView.Recycler recycler,
                               RecyclerView.State state) {

        //Log.d(TAG, "Thomas Visible Position is : " + mFirstVisiblePosition);

        //必须要动态更新可现实的行和列
        //first update the visible row and column for later use in this function
        //这里更新的是滑动后原来显示的views 和 新增加offset需要填补的views的 总数量
        // updateWindowSizing();

        if (mFirstVisiblePosition < 0) {
            mFirstVisiblePosition = 0;
        }

        int itemCount = getItemCount();
        if (mFirstVisiblePosition >= getItemCount()){
            mFirstVisiblePosition = (getItemCount() - 1);
        }

        if(debug) {
            Log.d(TAG, "First Visible Position is : " + mFirstVisiblePosition);
        }

        /*
         * First, we will detach all existing views from the layout.
         * detachView() is a lightweight operation that we can use to
         * quickly reorder views without a full add/remove.
         */
        SparseArray<View> viewCache = new SparseArray<View>(getChildCount());

        int startLeftOffset = getPaddingLeft();//recyclerView的paddingleft
        int startTopOffset = getPaddingTop();//recyclerView 的paddingTop

        if (getChildCount() != 0) {//已经attached到recyclerView的子view. of course ,it always true

            //找到第一个位置
            final View topView = getChildAt(0);
            //计算出左边和顶部的左边位置，也就知道了被遮盖住了多少
            startLeftOffset = getDecoratedLeft(topView);
            startTopOffset = getDecoratedTop(topView);

            LayoutParams lp = (LayoutParams) topView.getLayoutParams();
            int globalRow = lp.row;
            int globalColumn = lp.column;

            switch (direction) {
                case DIRECTION_START:
                    if(globalColumn > 0) {
                        globalColumn--;
                    }
                    startLeftOffset -= columnsWidth[globalColumn];
                    break;
                case DIRECTION_END:
                    startLeftOffset += columnsWidth[globalColumn];
                    break;
                case DIRECTION_UP://向下滑，垂直方向有一行新的view进入visible area
                    if(globalRow > 0) {
                        globalRow--;
                    }
                    startTopOffset -= rowsHeight[globalRow];
                    break;
                case DIRECTION_DOWN://向上滑，垂直方向有一行view从visible area中消失
                    startTopOffset += rowsHeight[globalRow];
                    break;
            }

            if(debug) {
                Log.d(TAG, "attached " +
                        "--------------------------------------------------------------");
            }
            //Cache all views by their existing position, before updating counts
            for (int i = 0; i < getChildCount(); i++) {
                int position = positionOfIndex(i);
                final View child = getChildAt(i);
                if(debug) {
                    Log.d(TAG, "put the " + i + "view in the adapter to viewCache");
                }
                viewCache.put(position, child);
            }

            //Temporarily detach all views.
            // Views we still need will be added back at the proper index.
            for (int i = 0; i < viewCache.size(); i++) {
                detachView(viewCache.valueAt(i));
            }
        }

        ///以上是准备坐标[left,top],以及把所有view放进去cache，并且从recyclerView分离child views
        /*
         * Next, we advance the visible position based on the fill direction.
         * DIRECTION_NONE doesn't advance the position in any direction.
         */
        switch (direction) {
            case DIRECTION_START:
                mFirstVisiblePosition--;
                break;
            case DIRECTION_END:
                mFirstVisiblePosition++;
                break;
            case DIRECTION_UP:
                mFirstVisiblePosition -= getTotalColumnCount();
                break;
            case DIRECTION_DOWN:
                mFirstVisiblePosition += getTotalColumnCount();
                break;
        }

        /*
         * Next, we supply the grid of items that are deemed visible.
         * If these items were previously there, they will simply be
         * re-attached. New views that must be created are obtained
         * from the Recycler and added.
         */
        int leftOffset = startLeftOffset;
        int topOffset = startTopOffset;
        int mDecoratedChildWidth = 0;
        int mDecoratedChildHeight = 0;

        /////动态更新mFirstVisiblePosition , 然后读取visible area 的行和列，再
        //更新layout中cell
        //mVisibleColumnCount * mVisibleRowCount;
        int visibleCount = getVisibleChildCount();

        if(debug) {
            Log.d(TAG, "visible child count is: " + visibleCount);
            Log.d(TAG, "layoutDecorated " +
                    "--------------------------------------------------------------");
        }

        for(int i = 0; i < getVisibleChildCount(); i++) {

            //找到对应i的那个view在整个中的pos
            int nextPosition = positionOfIndex(i);
            // Log.d(TAG, "nextPos is : " + nextPosition + " columnCount is : " + columnCount);


            if (nextPosition < 0 || nextPosition >= state.getItemCount()) {
                //Item space beyond the data set, don't attempt to add a view
                Log.wtf(TAG, "amazing !");
                continue;
            }

            //找到那个view
            View view = viewCache.get(nextPosition);

            if(view == null) {
                 /*
                 * The Recycler will give us either a newly constructed view,
                 * or a recycled view it has on-hand. In either case, the
                 * view will already be fully bound to the data by the
                 * adapter for us.
                 */
                view = recycler.getViewForPosition(nextPosition);
                addView(view);///recyclerView定义的方法

               /*
                 * Update the new view's metadata, but only when this is a real
                 * layout pass. 初始化一些数据
                 *
                 * 改view在整个表格的行和列
                 */
                LayoutParams viewLP = (LayoutParams) view.getLayoutParams();
                viewLP.row = getGlobalRowOfPosition(nextPosition);
                viewLP.column = getGlobalColumnOfPosition(nextPosition);

                if(debug) {
                    Log.d(TAG, "设置 i view [row,column]:  " + "[" + viewLP.row + "," + viewLP.column + "]");
                }

                /*
                 * It is prudent明智的 to measure/layout each new view we
                 * receive from the Recycler. We don't have to do
                 * this for views we are just re-arranging.
                 */
                measureChildWithMargins(view, 0, 0);
                int left,top,right,bottom;

                mDecoratedChildWidth = getDecoratedMeasuredWidth(view);
                mDecoratedChildHeight = getDecoratedMeasuredHeight(view);

                columnsWidth[nextPosition % columnCount] =  mDecoratedChildWidth;
                rowsHeight[nextPosition / columnCount] = mDecoratedChildHeight;

                //public void layoutDecorated(View child, int left, int top, int right, int bottom)
                left = leftOffset;
                top = topOffset;
                right = leftOffset + mDecoratedChildWidth;
                bottom = topOffset + mDecoratedChildHeight;

                if(debug) {
                    Log.d(TAG, "layoutDecorated " + nextPosition + " view [left,top],[right,bottom]: "
                            + "[" + left + "," + top + "], [" +
                            right + "," + bottom + "]");
                }

                layoutDecorated(view, left, top, right, bottom);//Lay out the given child view within the RecyclerView using coordinates
//
//                layoutDecorated(view, leftOffset, topOffset,
//                        leftOffset + mDecoratedChildWidth,
//                        topOffset + mDecoratedChildHeight);

            } else {//从cache中获取到了view

                measureChildWithMargins(view, 0, 0);
                int left,top,right,bottom;

                mDecoratedChildWidth = columnsWidth[nextPosition % columnCount];
                mDecoratedChildHeight = rowsHeight[nextPosition / columnCount];

                //public void layoutDecorated(View child, int left, int top, int right, int bottom)
                left = leftOffset;
                top = topOffset;
                right = leftOffset + mDecoratedChildWidth;
                bottom = topOffset + mDecoratedChildHeight;

                if(debug) {
                    Log.d(TAG, "layoutDecorated " + nextPosition + " view [left,top],[right,bottom]: "
                            + "[" + left + "," + top + "], [" +
                            right + "," + bottom + "]");
                }

                layoutDecorated(view, left, top, right, bottom);//Lay out the given child view within the RecyclerView using coordinates

                layoutDecorated(view, leftOffset, topOffset,
                        leftOffset + mDecoratedChildWidth,
                        topOffset + mDecoratedChildHeight);

                //Re-attach the cached view at its new index
                attachView(view);

                if(debug) {
                    Log.d(TAG, "layoutDecorated attach " + nextPosition + " view : " + view.toString());
                }
                viewCache.remove(nextPosition);
            }

            if (i % mVisibleColumnCount == (mVisibleColumnCount - 1)) {
                //当时列的最后一个的时候记录下一行第一个cell的[left,top]
                leftOffset = startLeftOffset;
                topOffset += mDecoratedChildHeight;
            } else {
                //否则就直接增加left的位置
                leftOffset += mDecoratedChildWidth;
            }
        }

        /*
         * Finally, we ask the Recycler to scrap and store any views
         * that we did not re-attach. These are views that are not currently
         * necessary because they are no longer visible.
         *
         *  回收没有用完的view
         */
        for (int i=0; i < viewCache.size(); i++) {
            final View removingView = viewCache.valueAt(i);
            recycler.recycleView(removingView);
        }

        if(debug) {
            Log.d(TAG, "finish one fill " +
                    "************************************************************************");
        }
    }

    /*
     * Mapping between child view indices and adapter data
     * positions helps fill the proper views during scrolling.
     */
    private int positionOfIndex(int childIndex) {
        int row = childIndex / mVisibleColumnCount;  //当前可见view中的第chidIndex个在可见view中的行
        int column = childIndex % mVisibleColumnCount;//当前可见view中的第chidIndex个在可见view中的列
        //返回值是当前可见view中的第chidIndex个 ，在整个adapter中的pos
        return mFirstVisiblePosition + (row * columnCount) + column;
    }

    /*
 * Even without extending LayoutParams, we must override this method
 * to provide the default layout parameters that each child view
 * will receive when added.
 */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //我们的view都是adapter从xml中inflate进来的，所以最终会调用到这个函数
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }
    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {

        //数据row 和 column我们都会保存在LayoutParams中
        //Current row in the grid
        public int row;
        //Current column in the grid
        public int column;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }
    }

    /* Return the overall column index of this position in the global layout */
    private int getGlobalColumnOfPosition(int position) {
        return position % columnCount;
    }
    /* Return the overall row index of this position in the global layout */
    private int getGlobalRowOfPosition(int position) {
        return position / columnCount;
    }

    //get the child views that would be in the visible area
    private int getVisibleChildCount() {
        return mVisibleColumnCount * mVisibleRowCount;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private int getTotalRowCount() {
        return rowCount;
    }

    private int getTotalColumnCount() {
        return columnCount;
    }

    private int rowOfIndex(int childIndex) {
        int position = positionOfIndex(childIndex);

        return position / getTotalColumnCount();
    }

    private int getFirstVisibleRow() {
        return (mFirstVisiblePosition / getTotalColumnCount());
    }

    private int getLastVisibleRow() {
        return getFirstVisibleRow() + mVisibleRowCount;
    }

    private int getFirstVisibleColumn() {
        return (mFirstVisiblePosition % getTotalColumnCount());
    }

    private int getLastVisibleColumn() {
        return getFirstVisibleColumn() + mVisibleColumnCount;
    }

}
