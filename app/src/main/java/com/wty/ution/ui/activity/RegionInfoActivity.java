package com.wty.ution.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wty.ution.R;
import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.adapter.RegionListAdapter;

import java.util.ArrayList;
import java.util.List;


public class RegionInfoActivity extends BaseActivity{

	private ListView listview;
	private RegionListAdapter adapter;
	private List<RegionInfoDALEx> data = new ArrayList<RegionInfoDALEx>();
	private int regionid;
	private RegionInfoDALEx region;		
	private String fieldname;
	private String title;
	private boolean limit = true;
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.setContentView(R.layout.activity_regionselect);
		
		fieldname = getIntent().getStringExtra("fieldname");
		regionid = getIntent().getIntExtra("regionid", RegionInfoDALEx.defaultId);
		title = getIntent().getStringExtra("title");
		limit = getIntent().getBooleanExtra("limit", true);
		
		UtionObjectCache.getInstance().putRegionCache(this, fieldname);

		getDefaultNavigation().setTitle("选择区域");
		
		if(limit){
            getDefaultNavigation().getRightButton().hide();
		}else{
            getDefaultNavigation().getRightButton().show();
		}

		listview = (ListView)findViewById(R.id.regionselect_listview);
		listview.setOnItemClickListener(itemClickListener);

		new SimpleTask(){
			@Override
			protected Object doInBackground(String... params) {
				region = RegionInfoDALEx.get().queryById(regionid);
				if(region!=null){
					data = RegionInfoDALEx.get().queryPregions(region.getRegionid());
				}
				return super.doInBackground(params);
			}

			@Override
			protected void onPostExecute(Object o) {
				if(region!=null){
					if (region.getPregionid() != 0) {
						TextView textView = new TextView(RegionInfoActivity.this);
						textView.setPadding(20, 20, 0, 5);
						textView.setText(region.getRegionname());
						textView.setFocusable(false);
						textView.setClickable(false);
						listview.addHeaderView(textView);
					}
					adapter = new RegionListAdapter(RegionInfoActivity.this, data);
					listview.setAdapter(adapter);

				}
			}
		}.startTask();
		
	}
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			RegionInfoDALEx dalex = (RegionInfoDALEx) listview.getItemAtPosition(position);
			if(RegionInfoDALEx.get().queryPregions(dalex.getRegionid()).size()==0){
				long t = System.currentTimeMillis();
            	UtionObjectCache.getInstance().setSelectedRegionid(""+dalex.getRegionid());
            	
            	Intent i = new Intent();
            	i.putExtra("fieldname", fieldname);
            	setResult(Activity.RESULT_OK);
            	
            	UtionObjectCache.getInstance().cleanRegionPage();
            	System.out.println("-------------------- select region clear Cache ------"+(System.currentTimeMillis()-t));
            	
            }else{
                Intent i = new Intent(RegionInfoActivity.this,RegionInfoActivity.class);
                i.putExtra("regionid", dalex.getRegionid());
                i.putExtra("title", title);
                i.putExtra("fieldname", fieldname);
                startActivityForResult(i, AppContext.Constant.ActivityResult_FieldLabel_RegionSelect);
            }
		        
			
		}
	};
	
}