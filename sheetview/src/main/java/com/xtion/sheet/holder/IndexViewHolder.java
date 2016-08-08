package com.xtion.sheet.holder;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.xtion.sheet.adapter.IndexAdapter;
import com.xtion.sheet.cell.TextCell;

public class IndexViewHolder extends SheetCellHolder implements View.OnClickListener{
        public IndexViewHolder(Context context, IndexAdapter adapter) {
            super(new TextCell(context,adapter),adapter);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setValue(String text){
            ((TextCell)getCell()).setText(text);
        }

        public void setColor(int bgColor,int textColor){
            TextCell cell = (TextCell) getCell();
            Resources resource = cell.getResources();

            cell.setBgColor(resource.getColor(bgColor));
            cell.setTextColor(resource.getColor(textColor));
        }

        public void setWH(int width,int height){
            getCell().setWH(width,height);
        }

    }