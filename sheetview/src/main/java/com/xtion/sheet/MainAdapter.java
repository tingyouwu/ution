package com.xtion.sheet;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
public class MainAdapter extends RecyclerView.Adapter {
    private int count;
    private int columnCount;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_CHECKBOX = 2;

    //存储cells的width 和 height,怕影响效率，我们用原生的数组
    //this is the data set that determine the cells' width and height
    //adapter and layoutmanager should never touch each other directly
    private int[] columnsWidth;
    private int[] rowsHeight;

    public MainAdapter(int totalCount, int columnCount, int cellWPixel, int cellHPixel) {
        this.count = totalCount;
        this.columnCount = columnCount;
        // /data set of all the cell's height and width in the sheet
        columnsWidth = new int[columnCount];
        int rowCount = totalCount / columnCount;
        rowsHeight = new int[rowCount];
        //init the data
        for(int i = 0;i < columnCount; i++) {
            columnsWidth[i] = cellWPixel;
        }

        for(int j = 0;j < rowCount; j++) {
            rowsHeight[j] = cellHPixel;
        }
    }

    //function to handle click events
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void testChangePlus(int position) {
        int row = position / columnCount;
        int column = position % columnCount;

        //rowsHeight[row] += 20;
        columnsWidth[column] += 20;
        notifyDataSetChanged();
    }

    public void testChangeMinus(int position) {
        int row = position / columnCount;
        int column = position % columnCount;

        //rowsHeight[row] -= 20;
        columnsWidth[column] -= 20;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        //根据位置来确定类型！
        int type = TYPE_TEXT;
        switch (position%5) {
            case 0:
                type = TYPE_TEXT;
                break;
            case 1:
                type = TYPE_IMAGE;
                break;
            case 2:
                type = TYPE_TEXT;
                break;
            default:
                type = TYPE_CHECKBOX;
                break;
        }

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

            case TYPE_CHECKBOX:
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
                //根据位置position来决定显示的data，同样我们也要根据position来决定其他data
                ViewHolder1 test = (ViewHolder1) holder;
                TextView tv = test.mTextView;
                tv.setText(position + " ");

                int row = position / columnCount;
                int column = position % columnCount;

                test.setWH(columnsWidth[column],rowsHeight[row]);

                break;

            case TYPE_IMAGE:
                break;

            case TYPE_CHECKBOX:
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
        private MainAdapter mAdapter;

        //itemView is the outer LinearLayout
        public ViewHolder1(View itemView, MainAdapter adapter) {
            super(itemView);
            root = (LinearLayout)itemView;

            ///
            root.setOnClickListener(this);


            mAdapter = adapter;

            mTextView = (TextView)itemView.findViewById(R.id.textView);
        }

        public void setBgColor() {
            mTextView.setBackgroundColor(Color.GREEN);
        }

        public void setWH(int width, int height) {
            float widthPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,
                    root.getContext().getResources().getDisplayMetrics());

            float heightPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,
                    root.getContext().getResources().getDisplayMetrics());

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) root.getLayoutParams();

            params.width = (int) widthPX;
            params.height = (int) heightPX;
            root.setLayoutParams(params);
        }

        public void setWidth(int width) {
            float widthPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,
                    root.getContext().getResources().getDisplayMetrics());

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) root.getLayoutParams();
            params.width = (int) widthPX;
            root.setLayoutParams(params);
        }

        public void setHeight(int height) {
            float heightPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,
                    root.getContext().getResources().getDisplayMetrics());

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) root.getLayoutParams();

            params.height = (int) heightPX;
            root.setLayoutParams(params);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mImageView;
        private MainAdapter mAdapter;

        public ViewHolder2(View itemView, MainAdapter adapter) {
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
        public CheckBox mCheckBox;
        private MainAdapter mAdapter;

        public ViewHolder3(View itemView, MainAdapter adapter) {
            super(itemView);
            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBox);
            mAdapter = adapter;
            //mImageView.setBackground(R.drawable.ico_file_audio);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}

