package com.xtion.sheet;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-9-28.
 */
public class SimpleAdapter extends RecyclerView.Adapter {
    private int adapterType = VERTICAL_TYPE;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public static final int VERTICAL_TYPE = 0;
    public static final int HORIZONTAL_TYPE = 1;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_EDITTEXT = 2;

    private int count;
    //存储cells的width 和 height
    //this is the data set that determine the cells' width and height
    //adapter and layoutmanager should never touch each other directly
    private int[] lines;

    private int mainListColumnCount = 0;

    public SimpleAdapter(int count, int[] lines) {
        this.count = count;
        this.lines =  lines;
    }

    public SimpleAdapter(DataSet dataSet, int type) {
        this.adapterType = type;
        this.mainListColumnCount = dataSet.getColumnCount();

        if(adapterType == VERTICAL_TYPE) {
            this.count = dataSet.getRowCount();
            this.lines = dataSet.getRowsHeight();
        } else {
            this.count = dataSet.getColumnCount();
            this.lines = dataSet.getColumnsWidth();
        }
    }

    public SimpleAdapter(int count, int value) {
        this.count = count;

        lines =  new int[count];
        for(int j = 0;j < count; j++) {
            lines[j] = value;
        }
    }

    public void updateData(int pos) {
        int position = 0;
        if(this.adapterType == VERTICAL_TYPE) {
            position = pos / mainListColumnCount;
        } else {
            position = pos % mainListColumnCount;
        }

        notifyItemChanged(position);
    }

    public void testChangePlus(int pos) {
        lines[pos] += 20;
        notifyItemChanged(pos);
    }

    public void testChangeMinus(int pos) {
        lines[pos] -= 20;
        notifyItemChanged(pos);
    }

    //function to handle click events
    public void setmOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        //根据位置来确定类型！
        int type = TYPE_TEXT;
//        switch (position%3) {
//            case 0:
//                type = TYPE_TEXT;
//                break;
//            case 1:
//                type = TYPE_IMAGE;
//                break;
//            case 2:
//                type = TYPE_TEXT;
//                break;
//            default:
//                type = TYPE_EDITTEXT;
//                break;
//        }

        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //set the view's margins, paddings and layout parameters

        RecyclerView.ViewHolder viewHolder = null;
        View v = null;

        switch(viewType) {
            case TYPE_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_image, parent,
                        false);
                viewHolder = new ViewHolder2(v, this);
                Log.d("MyAdapter", "ViewHolder2 image view holder was created!");
                break;

            case TYPE_TEXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent,
                        false);
                viewHolder = new ViewHolder1(v, this);
                //TextView textView = (TextView)v.findViewById(R.id.textView);
                break;

            case TYPE_EDITTEXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_checkbox, parent,
                        false);
                viewHolder = new ViewHolder3(v, this);
                break;

            default:
                break;
        }

        return viewHolder;
    }


    /*
        When attempting to supply the LayoutManager with a new view,

         1.a Recycler will first check the scrap heap for a matching position/id; if one exists, it will
         be returned without re-binding to the adapter data.

         If no matching view is found,
         2.the Recycler will instead pull a suitable view from the recycle
         pool and bind the necessary data to it from the adapter:

        RecyclerView.Adapter.bindViewHolder()


         In cases where no valid views exist in the recycle pool, a new view will be created instead
         (i.e. RecyclerView.Adapter.createViewHolder() is invoked) before being bound, and returned:

        RecyclerView.Adapter.createViewHolder()

        we reset the data on onBindViewHolder
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ///we get item type here
        int type = holder.getItemViewType();



        switch (type) {
            case TYPE_TEXT:

                Log.d("Thomas", "onBindViewHolder is invoked");
                //根据位置position来决定显示的data，同样我们也要根据position来决定其他data
                ViewHolder1 test = (ViewHolder1) holder;
                TextView tv = test.mTextView;
                tv.setText(position + " ");

                if(this.adapterType == VERTICAL_TYPE) {
                    //int row = position / count;
                    test.setHeight(lines[position]);
                } else {
                    //int column = position % count;
                    test.setWidth(lines[position]);
                }

                test.setBgColor();
                break;

            case TYPE_IMAGE:
                break;

            case TYPE_EDITTEXT:
                break;

            default:
                break;
        }
        //tv.setBackgroundColor(Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        LinearLayout root ;
        private SimpleAdapter mAdapter;

        //itemView is the outer LinearLayout
        public ViewHolder1(View itemView, SimpleAdapter adapter) {
            super(itemView);
            root = (LinearLayout)itemView;
            mAdapter = adapter;

            mTextView = (TextView)itemView.findViewById(R.id.textView);
        }

        public void setBgColor() {
            mTextView.setBackgroundColor(Color.GREEN);
        }

        public void setWidth(int width) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) root.getLayoutParams();
            params.width = width;
            root.setLayoutParams(params);
        }

        public void setHeight(int height) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) root.getLayoutParams();
            params.height = height;
            root.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mImageView;
        private SimpleAdapter mAdapter;

        public ViewHolder2(View itemView, SimpleAdapter adapter) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.imageView);
            mAdapter = adapter;
            //mImageView.setBackground(R.drawable.ico_file_audio);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckBox mcheckBox;
        private SimpleAdapter mAdapter;

        public ViewHolder3(View itemView, SimpleAdapter adapter) {
            super(itemView);
            mcheckBox = (CheckBox)itemView.findViewById(R.id.checkBox);
            mAdapter = adapter;
            //mImageView.setBackground(R.drawable.ico_file_audio);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
