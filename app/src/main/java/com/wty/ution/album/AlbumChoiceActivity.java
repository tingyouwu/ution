package com.wty.ution.album;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.widget.FrameButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能描述：相册选择页面
 * @author wty
 **/
public class AlbumChoiceActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.album_choice_gridview) GridView  gridview;
    @Bind(R.id.album_choice_selectedTotal) TextView  tv_selectedTotal;
    @Bind(R.id.album_choice_submit) FrameButton btn_submit;

    private boolean            isMutiChoice;
    private int                maxlimit;
    private int                selectedTotal;

    private AlbumChoiceAdapter adapter;
    private AlbumIndexItem     album;

    
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        AppContext.setContext(this);
        setContentView(R.layout.activity_albumchoice);
        ButterKnife.bind(this);
        getDefaultNavigation().setTitle("选择照片");
        
        album = (AlbumIndexItem)getIntent().getExtras().get("album");
        isMutiChoice = getIntent().getBooleanExtra("isMutiChoice", false);
        maxlimit = getIntent().getIntExtra("maxlimit", 3);
        selectedTotal = getIntent().getIntExtra("selectedTotal", 0);

        adapter = new AlbumChoiceAdapter(this, album, isMutiChoice,gridview);
        gridview.setAdapter(adapter);
        tv_selectedTotal.setText(String.format("已选择 %d/%d", new Object[]{selectedTotal,maxlimit}));
        btn_submit.setOnClickListener(this);
        
        adapter.setOnCheckBoxSelectedListener(new AlbumChoiceAdapter.OnCheckBoxSelectedListener() {
            
            @Override
            public void onSelected(int position, boolean isCheck) {
                if(isCheck){
                    selectedTotal = selectedTotal + 1;
                }else{
                    selectedTotal = selectedTotal - 1;
                }
                tv_selectedTotal.setText(String.format("已选择 %d/%d", new Object[]{selectedTotal,maxlimit}));
            }

            @Override
            public boolean validate(boolean isCheck) {
                if(isCheck){
                    //正选需要校验
                    if(selectedTotal<maxlimit){
                        return true;
                    }else{
                        onToast(String.format("最多只能选择%d个", maxlimit));
                        return false;
                    }
                }else{
                    //反选不需要校验
                    return true;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.album_choice_submit:
            back(true);
            break;
        default:
            break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            back(false);
            break;
        default:
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
    
    private void back(boolean submit){
        Intent i = new Intent();
        i.putExtra("album", album);
        i.putExtra("submit",submit);
        setResult(RESULT_OK,i);
        finish();
    }
}
