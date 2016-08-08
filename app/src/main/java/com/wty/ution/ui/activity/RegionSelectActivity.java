package com.wty.ution.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wty.ution.R;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.task.SimpleTask;
import com.wty.ution.ui.adapter.RegionListAdapter;
import com.wty.ution.widget.navigation.NavigateAble;
import com.wty.ution.widget.navigation.NavigationBar;

import java.util.ArrayList;
import java.util.List;

public class RegionSelectActivity extends BaseActivity implements NavigationBar.OnNavigationListener {

	private ListView listview;
	private RegionListAdapter adapter;
	private List<RegionInfoDALEx> data = new ArrayList<RegionInfoDALEx>();
	private int regionid;
	private RegionInfoDALEx currentregion;
	private String fieldname;
	private NavigationBar navBar;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_regionselect);
		getDefaultNavigation().setTitle("选择区域");

		fieldname = getIntent().getStringExtra("fieldname");
		regionid = getIntent().getIntExtra("regionid", RegionInfoDALEx.defaultId);
		getDefaultNavigation().setRightButton("确定", submit);
		listview = (ListView)findViewById(R.id.regionselect_listview);
		listview.setOnItemClickListener(itemClickListener);
		adapter = new RegionListAdapter(RegionSelectActivity.this, data);
		listview.setAdapter(adapter);

		navBar = (NavigationBar)findViewById(R.id.region_select_navigationbar);
		navBar.setOnNavigationListener(this);

		RegionInfoDALEx navRoot = RegionInfoDALEx.get().queryById(regionid);
		navBar.addNavigationItem(navRoot);

		initData();
	}

	private void initData(){

		new SimpleTask(){
			@Override
			protected Object doInBackground(String... params) {
				List<RegionInfoDALEx> result = new ArrayList<RegionInfoDALEx>();
				currentregion = RegionInfoDALEx.get().queryById(regionid);
				if(currentregion != null){
					result = RegionInfoDALEx.get().queryPregions(currentregion.getRegionid());
				}
				return result;
			}

			@Override
			protected void onPostExecute(Object result) {
				List<RegionInfoDALEx> regionlist = (List<RegionInfoDALEx>)result;
				if(regionlist.size()!=0){
					data.clear();
					data.addAll(regionlist);
					adapter.notifyDataSetChanged();
				}
			}
		}.startTask();
	}

	View.OnClickListener submit = new View.OnClickListener(){
		@Override
		public void onClick(View v) {

			if(currentregion == null || currentregion.getRegionid()==RegionInfoDALEx.defaultId){
				onToast("请选择省市区区域");
				return;
			}

			result(currentregion);
		}
	};

	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			RegionInfoDALEx dalex = (RegionInfoDALEx) listview.getItemAtPosition(position);
			if(RegionInfoDALEx.get().queryPregions(dalex.getRegionid()).size()==0){
				result(dalex);
			}else{
				//进入下一层
				currentregion = dalex;
				navBar.addNavigationItem(dalex);
				List<RegionInfoDALEx> querys = RegionInfoDALEx.get().queryPregions(dalex.getRegionid());
				data.clear();
				data.addAll(querys);
				adapter.notifyDataSetChanged();
			}

		}
	};

	@Override
	public void onNavigationItemSelect(NavigateAble item) {
		RegionInfoDALEx series = (RegionInfoDALEx) item;
		currentregion = series;
		List<RegionInfoDALEx> querys = RegionInfoDALEx.get().queryPregions(series.getRegionid());
		data.clear();
		data.addAll(querys);
		adapter.notifyDataSetChanged();
	}

	public void result(RegionInfoDALEx dalex){
		//已经是最后一层
		UtionObjectCache.getInstance().setSelectedRegionid("" + dalex.getRegionid());
		UtionObjectCache.getInstance().setRegionFieldName(fieldname);
		Intent i = new Intent();
		i.putExtra("fieldname", fieldname);
		i.putExtra("regionid",dalex.getRegionid());
		setResult(Activity.RESULT_OK);
		finish();
	}
}