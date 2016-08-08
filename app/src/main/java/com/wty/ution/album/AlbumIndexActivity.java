package com.wty.ution.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.ui.activity.BaseActivity;
import com.wty.ution.util.PhotoUtils;
import com.wty.ution.widget.FrameButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能描述：相册索引界面
 * @author wty
 **/
public class AlbumIndexActivity extends BaseActivity implements View.OnClickListener{
	
	public final static String Tag_isMutiChoice ="isMutiChoice";
	public final static String Tag_maxlimit = "maxlimit";
	public final static String Tag_uris = "uris";
	public final static String Tag_ids = "ids";
	public final static String Tag_labelId = "labelId";

    @Bind(R.id.album_index_gridview) GridView gridview;
    @Bind(R.id.album_index_submit) FrameButton btn_submit;
    @Bind(R.id.album_index_maxlimit) TextView tv_maxlimit;


    private boolean            isMutiChoice;
    private AlbumIndexAdapter adapter;
    private List<AlbumIndexItem>  albumList;
    

    private int maxlimit;
    private List<String> uris = new ArrayList<String>();
    private List<String> ids = new ArrayList<String>();
    private String labelId;
    
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        AppContext.setContext(this);
        setContentView(R.layout.activity_albumindex);
        ButterKnife.bind(this);
        getDefaultNavigation().setTitle("相册目录");

        labelId = getIntent().getStringExtra(Tag_labelId);
        isMutiChoice = getIntent().getBooleanExtra(Tag_isMutiChoice, false);
        maxlimit = getIntent().getIntExtra(Tag_maxlimit, 3);
        String[] selectedUris = getIntent().getStringArrayExtra(Tag_uris);
        
        albumList = PhotoUtils.getPhotoAlbum(this, Arrays.asList(selectedUris));
        adapter = new AlbumIndexAdapter(albumList,this);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(ClickListener);
        
        tv_maxlimit.setText(String.format("最多能选择%d张图片", maxlimit));
        btn_submit.setOnClickListener(this);
        
        for(AlbumIndexItem item :albumList){
            List<String> us = item.getSelectedItemUris();
            if(us.size()==0){
                item.setSelected(false);
            }else{
                item.setSelected(true);
                uris.addAll(us);
            }
        }
        if(uris.size()==0){
            btn_submit.setText(String.format("完成",uris.size()));
        }else{
            btn_submit.setText(String.format("完成(%d)",uris.size()));
        }
        
    }

    /**
     * 相册目录点击事件
     */
    OnItemClickListener ClickListener =  new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            Intent intent = new Intent(AlbumIndexActivity.this,AlbumChoiceActivity.class);
            intent.putExtra("album", albumList.get(position));
            intent.putExtra("isMutiChoice", isMutiChoice);
            intent.putExtra("maxlimit", maxlimit);
            intent.putExtra("selectedTotal", uris.size());
            startActivityForResult(intent,AppContext.Constant.ActivityResult_AlbumChoice);
        }
    };
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.album_index_submit:
        	returnUri();
            break;
        default:
            break;
        }
    }
    
    protected void returnUri() {
        Intent data = new Intent();
        data.putExtra(Tag_ids, ids.toArray(new String[ids.size()]));
        data.putExtra(Tag_uris, uris.toArray(new String[uris.size()]));
        data.putExtra(Tag_labelId, labelId);
        setResult(RESULT_OK,data);
        finish();
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case AppContext.Constant.ActivityResult_AlbumChoice:
            if(resultCode==RESULT_OK){
                AlbumIndexItem album = (AlbumIndexItem) data.getSerializableExtra("album");
                uris.clear();
                ids.clear();
                for(AlbumIndexItem item :albumList){
                    if(item.getDir_id().equals(album.getDir_id())){
                        item.getBitList().clear();
                        item.getBitList().addAll(album.getBitList());
                    }
                    List<String> us = item.getSelectedItemUris();
                    if(us.size()==0){
                        item.setSelected(false);
                    }else{
                        item.setSelected(true);
                        uris.addAll(us);
                    }

                    List<String> names = item.getSelectedItemIds();
                    if(names.size()!=0){
                        ids.addAll(names);
                    }
                }
                    
                if(data.getBooleanExtra("submit", false)){
                    returnUri();
                }

                if(uris.size()==0){
                    btn_submit.setText(String.format("完成",uris.size()));
                }else{
                    btn_submit.setText(String.format("完成(%d)",uris.size()));
                }
                adapter.notifyDataSetChanged();
            }
            break;
        default:
            break;
        }
    }

}
