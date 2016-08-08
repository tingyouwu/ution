package com.xtion.sheet.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xtion.sheet.adapter.AbstractSheetAdapter;
import com.xtion.sheet.adapter.AlphaAdapter;
import com.xtion.sheet.cell.Cell;

/**
 * Created by Administrator on 2015-10-8.
 */
public class SheetCellHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    protected AbstractSheetAdapter mAdapter;

    private Cell cell;

    public SheetCellHolder(Cell cell, AbstractSheetAdapter adapter) {
        super(cell);
        mAdapter = adapter;
        this.cell = cell;
        this.cell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAdapter.onItemHolderClick(this);
    }

    public Cell getCell(){
        return cell;
    }


}
