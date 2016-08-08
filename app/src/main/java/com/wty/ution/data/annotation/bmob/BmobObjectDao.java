package com.wty.ution.data.annotation.bmob;

import com.wty.ution.data.bmob.BaseBmobObject;

import java.util.HashMap;
import java.util.Map;

public class BmobObjectDao {
	private static Map<Class<? extends BaseBmobObject>, BaseBmobObject> daos = new HashMap<Class<? extends BaseBmobObject>,BaseBmobObject>();
	
	public static <T extends BaseBmobObject> T getDao(Class<? extends BaseBmobObject> clazz){
		BaseBmobObject dao = daos.get(clazz);
		if(dao==null){
			try {
				dao = clazz.newInstance();
				daos.put(clazz, dao);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return (T)dao;
	}
	
	public static void clear(){
		daos.clear();
	}
}
