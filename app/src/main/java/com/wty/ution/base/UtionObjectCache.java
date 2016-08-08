package com.wty.ution.base;

import android.app.Activity;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.SqliteAnnotationCache;
import com.wty.ution.data.annotation.SqliteDao;
import com.wty.ution.data.annotation.bmob.BmobAnnotationCache;
import com.wty.ution.data.annotation.bmob.BmobObjectDao;
import com.wty.ution.ui.activity.RegionInfoActivity;
import com.wty.ution.widget.filter.base.FilterEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class UtionObjectCache {
	
	/** Cache单例对象 */
	private static UtionObjectCache cache;
	
	/** Activity堆栈树 */
	private LinkedHashMap<String,Activity> activityTree = new LinkedHashMap<String,Activity>();
	
	/** UtionDB 数据库注册表 */
	private Map<String,UtionDB> etionDbMap = new HashMap<String,UtionDB>();

	/** Orm Annotation中字段、Table的缓存，注入时用到 */
	private SqliteAnnotationCache sqliteAnnotationCache;

	/** Bmob对象中字段 缓存，注入时用到 */
	private BmobAnnotationCache bmobAnnotationCache;
	
	public synchronized UtionDB getUtionDbFromUserAccunt(String account){
		UtionDB etionDB = etionDbMap.get(account);
		if(etionDB==null){
			etionDB = new UtionDB(AppContext.getContext().getApplicationContext());
			etionDbMap.put(account, etionDB);
		}
		return etionDB;
	}
	
	public UtionDB getEtionDbFromUserAccunt(){
		String account = AppContext.getInstance().getLastAccount();
		return getUtionDbFromUserAccunt(account);
	}
	
	public void closeAllDB(){
		try {
			for(UtionDB db:etionDbMap.values()){
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static synchronized UtionObjectCache getInstance(){
		if(cache==null){
			cache = new UtionObjectCache();
		}
		return cache;
	}
	
	public void clearCache(){
		if(sqliteAnnotationCache!=null)sqliteAnnotationCache.clear();
		if(bmobAnnotationCache!=null)bmobAnnotationCache.clear();
		if(activityTree!=null)activityTree.clear();
		FilterEventBus.destroy();
		SqliteDao.clear();
		BmobObjectDao.clear();
		System.gc();
	}
	
	public void putActivityTree(Activity activity){
		String className = activity.getClass().getName();
		activityTree.put(className, activity);
	}
	
	public void removeActivityInTree(String className){
		if(activityTree.containsKey(className)){
			activityTree.remove(className);
		}
	}
	
	public Activity getActivityInTree(String className){
		return activityTree.get(className);
	}

	public Activity getTopActivityInTree() {
        Activity result = null;
		for (Entry<String, Activity> entry : activityTree.entrySet()) {
			result = entry.getValue();
		}
        return result;
	}
	
	public synchronized void finishAllActivity(){
		for (Entry<String, Activity> e : activityTree.entrySet()) {
			e.getValue().finish();
		}
	}
	
	public synchronized SqliteAnnotationCache getSqliteAnnotationCache(){
		if(sqliteAnnotationCache==null){
			sqliteAnnotationCache = new SqliteAnnotationCache();
		}
		return sqliteAnnotationCache;
	}

	public synchronized BmobAnnotationCache getBmobAnnotationCache(){
		if(bmobAnnotationCache==null){
			bmobAnnotationCache = new BmobAnnotationCache();
		}
		return bmobAnnotationCache;
	}

	private List<RegionInfoActivity> regionPageCache = new ArrayList<RegionInfoActivity>();

	private String selectedRegionid;//选择好的行政区域id
	private String regionFieldName;//行政区域的字段控件名

	public void cleanRegionCache(){
		cleanRegionPage();
		selectedRegionid = "";
		regionFieldName = "";
	}

	public void cleanRegionPage(){
		for(RegionInfoActivity activity:regionPageCache){
			activity.finish();
		}
		regionPageCache.clear();
	}

	public void putRegionCache(RegionInfoActivity activity,String regionFieldName){
		regionPageCache.add(activity);
		this.regionFieldName = regionFieldName;
	}

	public void setSelectedRegionid(String regionid){
		this.selectedRegionid = regionid;
	}

	public String getSelectedRegionid(){
		return this.selectedRegionid;
	}

	public String getRegionFieldName() {
		return regionFieldName;
	}

	public void setRegionFieldName(String regionFieldName) {
		 this.regionFieldName = regionFieldName;
	}
}