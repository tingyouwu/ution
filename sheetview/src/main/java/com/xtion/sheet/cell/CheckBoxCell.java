package com.xtion.sheet.cell;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;


import com.xtion.sheet.R;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.holder.SheetCellHolder;

import java.util.Random;

/**
 * Created by Administrator on 2015-10-9.
 */
public class CheckBoxCell extends Cell {

    private CheckBox checkBox;

    public CheckBoxCell(Context context, AlphaAdapter adapter) {
        super(context, adapter);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
    }

    @Override
    public void setWH(int width, int height) {

    }

    @Override
    public void setTheme() {

    }

    @Override
    public void setBgColor(int color) {

    }

    @Override
    public void setTextColor(int color) {

    }

    @Override
    public void setData(int postion) {

        //testing
        Random random =new Random();
        //random.nextInt(max)表示生成[0,max]之间的随机数
        int s = random.nextInt(8);

        if (s%2 == 0) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }
    }
}
