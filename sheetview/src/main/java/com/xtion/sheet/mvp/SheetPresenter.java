package com.xtion.sheet.mvp;

import android.app.Activity;

import com.xtion.sheet.DataSet;
import com.xtion.sheet.ViewSet;
import com.xtion.sheet.adapter.AbstractSheetAdapter;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.adapter.IndexAdapter;
import com.xtion.sheet.model.ISheetColumnModel;

/**
 * Created by Administrator on 2015-9-29.
 *
 * mvp的全称为Model-View-Presenter，Model提供数据，View负责显示，
 * Controller/Presenter负责逻辑的处理。MVP与MVC有着一个重大的区别：
 * 在MVP中View并不直接使用Model，它们之间的通信是通过Presenter
 * (MVC中的Controller)来进行的，所有的交互都发生在Presenter内部，
 * 而在MVC中View会直接从Model中读取数据而不是通过 Controller
 *
 *
 * 表格控件：
 *
 * Model--->存储的是从协议xml获取的属性
 *
 * presenter负责从Model获取属性转换为相对应的数据结构给RecyclerView
 *
 * RecyclerView需要提交的只有提交的数据，该数据数据结构跟model的无关，最多model
 * 能获取数据源
 *
 */
public class SheetPresenter implements OuterBarView.onEditTextChangedListener{
    private static final String TAG = "SheetPresenter";
    private Activity context;
    private OuterBarView outerBarView;
    InnerSheetView innerSheetView;

    private AlphaAdapter.OnCellClickListener mainClickListener;
    private AlphaAdapter.OnRowClickListener leftClickListener;
    private AlphaAdapter.OnColumnClickListener topClickListener;

    public SheetPresenter(Activity context) {
        this.context = context;
    }

    public OuterBarView getView(final DataSet dataSet) {
        ViewSet viewSet = new ViewSet.Builder().build();
        return getView(dataSet,viewSet);
    }

    public OuterBarView getView(final DataSet dataSet,ViewSet viewSet) {

        //warning all the parameters should come from dataset

        // TODO: 2015-10-12
        //1.DataSet的流程： 服务器---->xml--->解析----->SheeetAttributes--->SheetPresenter
        // 根据attribute构建dataset

        //2.通过dataset来构建InnerSheetView,adapter,

        //3.通过dataset来构建OuterBarView

        //参数太多，利用builder来构造一个sheet
        if(innerSheetView==null){
            innerSheetView = new InnerSheetView(context, dataSet,viewSet);

            //set onClickListener

            if(topClickListener==null && dataSet.isEnableSort()){
                topClickListener = new AbstractSheetAdapter.OnColumnClickListener() {
                    @Override
                    public void onClick(ISheetColumnModel columnModel) {
                        if(!columnModel.isEnabelSort())return;
                        dataSet.getDataSortHelper().sortData(columnModel);
                        IndexAdapter adapter = innerSheetView.getLeftAdapter();
                        if(adapter!=null)adapter.notifyDataSetChanged();

                        AlphaAdapter mainAdapter = innerSheetView.getMainAdapter();
                        mainAdapter.notifyDataSetChanged();
                    }
                };
                innerSheetView.setTopItemClickListener(topClickListener);
            }

            innerSheetView.setMainListItemClickListener(mainClickListener);
            innerSheetView.setLeftItemClickListener(leftClickListener);

        }

        if(outerBarView==null){
            outerBarView = new OuterBarView(context, this);
        }
        outerBarView.addContentView(innerSheetView);

        return outerBarView;
    }




//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        Toast.makeText(this.context, "Clicked: " + position + ", index " ,
////                Toast.LENGTH_SHORT).show();
//
//        //get the item type
//        //we could get the data from the dataset
//
//        int type = innerSheetView.getItemType(position);
//        if(type == AlphaAdapter.TYPE_EDIT_TEXT) {
//            //get data from adapter
//            AlphaAdapter adapter = innerSheetView.getMainAdapter();
//            String text = adapter.getData(position);
//            //set the current focus item
//            adapter.setCurrentFocusPos(position);
//
//            outerBarView.setTextInput(text);
//        }
//
//    }

    @Override
    public void onEditTextChanged(String text) {

        AlphaAdapter adapter = innerSheetView.getMainAdapter();
        int pos = adapter.getCurrentFocusPos();

        if(pos != -1) {
            innerSheetView.updateItemData(pos, text);
        }
    }

    public void setMainItemClickListener(AlphaAdapter.OnCellClickListener clickListener){
        this.mainClickListener = clickListener;
    }

    public void setLeftItemClickListener(AlphaAdapter.OnRowClickListener clickListener){
        this.leftClickListener = clickListener;

    }

    public void setTopItemClickListener(AlphaAdapter.OnColumnClickListener clickListener){
        this.topClickListener = clickListener;
    }

}
