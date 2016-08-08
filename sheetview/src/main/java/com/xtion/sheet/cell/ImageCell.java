package com.xtion.sheet.cell;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;

import java.util.Random;

/**
 * Created by Administrator on 2015-10-9.
 */
public class ImageCell extends Cell {
    private ImageView imageView;

    public ImageCell(Context context, AlphaAdapter adapter) {
        super(context, adapter);
//        imageView = (ImageView)itemView.findViewById(R.id.imageView);
    }

    @Override
    public void setBgColor(int color) {

    }

    @Override
    public void setTextColor(int color) {

    }

    @Override
    public void setWH(int width, int height) {

    }

    @Override
    public void setTheme() {
//        setBgColor(Color.CYAN);
//        setTextColor(Color.BLACK);
    }

    @Override
    public void setData(int postion) {
        //// TODO: 2015-10-9
        //get data from dataSet which is referenced in the adapter, then set data
        //cell have no absolute relation with the position, so setData should be
        //invoiked in onBindViewHolder() in the adapter


//        DataSet dataSet = getAdapter().getDataSet();


        //testing
        ramdomSetImage();
        //imageView.setImageResource(R.drawable.a);
    }

    //warning : image should not be too big ,or will lead to performance issues
    private void ramdomSetImage() {
        Random random =new Random();
        //random.nextInt(max)表示生成[0,max]之间的随机数
        int s = random.nextInt(4);
        int res = R.drawable.icon;
//        switch (s) {
//            case 0:
//                res = R.drawable.a;
//                break;
//            case 1:
//                res = R.drawable.b;
//                break;
//            case 2:
//                res = R.drawable.c;
//                break;
//            case 3:
//                res = R.drawable.d;
//                break;
//            case 4:
//                res = R.drawable.e;
//                break;
//        }
        imageView.setImageResource(res);
    }
}
