package com.wty.ution.data.annotation;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.wty.ution.base.AppContext;
import com.wty.ution.base.UtionObjectCache;
import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField.FieldType;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class SqliteBaseDALEx implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 1L;
	protected String TABLE_NAME = "";
	protected String SQL_CREATETABLE = "";
	protected int indexId;

	public SqliteBaseDALEx() {
		if(TextUtils.isEmpty(TABLE_NAME)){
			TABLE_NAME = createTableName();
		}
	}
	
	protected String createTableName(){
		Class<? extends SqliteBaseDALEx> cls = this.getClass();
		String packageName = cls.getName();
		String[] pa = packageName.split("\\.");
		return "wty"+pa[pa.length-1];
	}
	
	public String getTableName(){
		return TABLE_NAME;
	}

	protected static UtionDB getDB(){
		String lastAccount =  AppContext.getInstance().getLastAccount();
		return UtionObjectCache.getInstance().getUtionDbFromUserAccunt(lastAccount);
	}
	
	/**
	 * 得到构建表的sql语句
	 */
	protected String SqlCreateTable(){
		if(!TextUtils.isEmpty(SQL_CREATETABLE))return SQL_CREATETABLE;

		//遍历带注解的字段
		List<SqliteAnnotationField> safs =  getSqliteAnnotationField();
        List<String> fieldsStr = new ArrayList<String>();
        fieldsStr.add("`id` integer primary key autoincrement");

		for(SqliteAnnotationField saf:safs){
            fieldsStr.add("`"+ saf.getColumnName() +"`" +" "+saf.getType()+(saf.isPrimaryKey()?" COLLATE NOCASE ":""));
		}
        //拼接初始化表的语句
        SQL_CREATETABLE = String.format("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( %s )", TextUtils.join(",",fieldsStr));
		return SQL_CREATETABLE;
		
	}
	
	protected void createTable(UtionDB db){
		if (!db.isTableExits(TABLE_NAME)) {
			if(TextUtils.isEmpty(SQL_CREATETABLE)) SQL_CREATETABLE = SqlCreateTable();
			if(TextUtils.isEmpty(TABLE_NAME)) TABLE_NAME = createTableName();
		 	db.creatTable(SQL_CREATETABLE);
		}
	}
	
	public boolean isTableEmpty(){
		int count = 0;
		Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find("select count(*) from "+ TABLE_NAME,new String[] {});
                if (cursor != null && cursor.moveToNext()) {
                	count = cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
		return count == 0;
    }
	
	/**
	 * 扩展字段控件用的时候，把注解的值取出，并转换成字符串
	 */
	public Map<String,String> getAnnotationFieldValue(){
		Map<String,String> values = new HashMap<String,String>();
		
		for(SqliteAnnotationField saf:getSqliteAnnotationField()){
			Field f = saf.getField();
			try {
				f.setAccessible(true);
				Object value = f.get(this);
				if (value != null)
					values.put(f.getName(), value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return values;
	}
	
	/**
	 * 获取对象中所有注解的值
	 */
	public Map<String,Object> getAFValue(){
		Map<String,Object> values = new HashMap<String,Object>();
		
		for(SqliteAnnotationField saf:getSqliteAnnotationField()){
			Field f = saf.getField();
			try {
				f.setAccessible(true);
				Object value = f.get(this);
				if (value != null)
					values.put(f.getName(), value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return values;
	}
	
	/**
	 * 获取注解值并转换成JSON对象
	 */
	public JSONObject getJsonAnnotationFieldValue(){
		JSONObject jb = new JSONObject();
		for(SqliteAnnotationField saf:getSqliteAnnotationField()){
			Field f = saf.getField();
			try {
				f.setAccessible(true);
				Object value = f.get(this);
				if (value != null)
					jb.put(f.getName(), value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jb;
	}
	
	/**
	 * 把map中的值填充到字段中
	 */
	public void setAnnotationField(Map<String,String> values){
		
		for(SqliteAnnotationField af:getSqliteAnnotationField()){
			try {
				Field f = af.getField();
				DatabaseField.FieldType type = af.getType();

				f.setAccessible(true);
				if (!values.containsKey(f.getName())) {
					continue;
				}
				String value = values.get(f.getName());
				if (value == null)
					value = "";

				if (type == DatabaseField.FieldType.INT) {
					if (value.equals("")) {
						f.set(this, 0);
					} else {
						f.set(this, Integer.valueOf(value));
					}
				} else if (type == FieldType.VARCHAR) {
					f.set(this, value);
				} else if (type == FieldType.REAL) {
					if (value.equals("")) {
						f.set(this, 0);
					} else {
						f.set(this, Float.valueOf(value));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    protected void onSetCursorValueComplete(Cursor cursor){}

    /**
     * 功能描述：通过游标赋值
     */
	public final void setAnnotationField(Cursor cursor){
		setAnnotationField(cursor,null);
	}

    /**
     * 功能描述：通过游标赋值
     */
    private final void setAnnotationField(Cursor cursor,Map<String, Integer> cursorIndex){
    	try {
    		SqliteAnnotationCache cache = UtionObjectCache.getInstance().getSqliteAnnotationCache();
    		SqliteAnnotationTable table = cache.getTable(TABLE_NAME, this.getClass());

            int indexId_InCursor;
            if(cursorIndex!=null){
                indexId_InCursor = cursorIndex.get("id");
            }else{
                indexId_InCursor = cursor.getColumnIndex("id");
            }

            if(indexId_InCursor!= -1 ){
                this.indexId = cursor.getInt(indexId_InCursor);
            }
	    	for(SqliteAnnotationField saf:table.getFields()){
				Field f = saf.getField();
				try {
					f.setAccessible(true);
					
					int index;
					if(cursorIndex!=null){
						index = cursorIndex.get(saf.getColumnName());
					}else{
						index = cursor.getColumnIndex(saf.getColumnName());
					}
					
					FieldType t = saf.getType();
					if (t == FieldType.INT) {
						f.set(this, cursor.getInt(index));
					} else if (t == FieldType.VARCHAR) {
						f.set(this, cursor.getString(index));
					} else if (t == FieldType.REAL) {
						f.set(this, cursor.getFloat(index));
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(f.getName() +" 未能正常赋值 ");
				}
				
			}
            onSetCursorValueComplete(cursor);

	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    }

    /**
     * 功能描述：把用户对象的属性转换成键值对
     */
    protected ContentValues tranform2Values() {
		
        ContentValues values = new ContentValues();
		for(SqliteAnnotationField saf:getSqliteAnnotationField()){
			try {
				Field f = saf.getField();
				f.setAccessible(true);
				Object v = f.get(this);
				if (v != null){
					String value = v.toString();
					values.put(saf.getColumnName(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
        return values;
    }
    
    public boolean isExist(String id){
    	return isExist(getPrimaryKey(), id);
    }
    
	public boolean isExist(String primaryKey,String id){
    	boolean result = false;
    	 Cursor cursor = null;
         try {
             UtionDB db = getDB();
             if (db.isTableExits(TABLE_NAME)) {
                 cursor = db.find("select "+ primaryKey +" from "+ TABLE_NAME +" where "+primaryKey +" =? ",new String[] {id});
                 if (cursor != null && cursor.moveToNext()) {
                    result = true;
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (cursor != null && !cursor.isClosed()) {
                 cursor.close();
             }
         }
         return result;
    }

	/**
	 * 获取注解字段
	 */
	public List<SqliteAnnotationField> getSqliteAnnotationField(){
		
		SqliteAnnotationCache cache = UtionObjectCache.getInstance().getSqliteAnnotationCache();
		SqliteAnnotationTable table = cache.getTable(TABLE_NAME,this.getClass());
		return table.getFields();
		
	}

	/**
	 * 获取表主键
	 */
	public String getPrimaryKey(){
		SqliteAnnotationCache cache = UtionObjectCache.getInstance().getSqliteAnnotationCache();
		SqliteAnnotationTable table = cache.getTable(TABLE_NAME,this.getClass());
		return table.getPrimaryKey();
	}
	
	/**
	 * 通过事务操作，效率高
	 */
	public boolean operatorWithTransaction(OnTransactionListener listener){
		UtionDB db = null;
        try {
            db = getDB();
            if(db.getConnection().isOpen()){
                db.getConnection().beginTransaction();
                return listener.onTransaction(db);
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.getConnection().isOpen()) {
                db.getConnection().setTransactionSuccessful();
                db.getConnection().endTransaction();
            }
        }
	}

    public int getIndexId() {
        return indexId;
    }

    protected interface OnTransactionListener{
		boolean onTransaction(UtionDB db);
	}

	protected interface OnQueryListener{
        <T extends SqliteBaseDALEx> void onResult(Cursor cursor, T t);
		void onException(Exception e);
	}
	

	private <T extends SqliteBaseDALEx> SqliteBaseDALEx newDALExInstance(){ 
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} 
	}

	/**
	 * 获取该对象的id值
	 */
	public String getPrimaryId(){
		String result = "";
		String key = getPrimaryKey();
		if(TextUtils.isEmpty(key))return null;
		SqliteAnnotationCache cache = UtionObjectCache.getInstance().getSqliteAnnotationCache();
		SqliteAnnotationTable table = cache.getTable(TABLE_NAME,this.getClass());
		SqliteAnnotationField field = table.getField(key);
		
		Field f = field.getField();
		f.setAccessible(true);
		try {
			result = (String) f.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** ------------------------------- 基本查询方法 -------------------------------*/

	public <T extends SqliteBaseDALEx> T findById(String id){
		return (T)findById(id, null);
	}

	public <T extends SqliteBaseDALEx> T findById(String id,OnQueryListener listener){
		String sql = "select * from "+TABLE_NAME+" where "+getPrimaryKey()+" =? ";
		return findOne(sql, new String[]{id}, listener);
	}

	public <T extends SqliteBaseDALEx> T findOne(String sql, String[] params){
		return findOne(sql, params, null);
	}

	public <T extends SqliteBaseDALEx> T findOne(String sql, String[] params, OnQueryListener listener){
        SqliteBaseDALEx dalex;
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {

                cursor = db.find(sql,params);
                if (cursor != null && cursor.moveToNext()) {
                    dalex = newDALExInstance();
                    dalex.setAnnotationField(cursor);
                    if(listener!=null)listener.onResult(cursor,dalex);
                    return (T)dalex;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(listener!=null)listener.onException(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

	public  <T extends SqliteBaseDALEx> List<T> findAll(){
		return findList("select * from "+TABLE_NAME, new String[]{});
	}
	
	public  <T extends SqliteBaseDALEx> List<T> findList(String sql,String[] params){
		return findList(sql, params,null);
	}
	
	public  <T extends SqliteBaseDALEx> List<T> findList(String sql,String[] params,OnQueryListener listener){
		List<SqliteBaseDALEx> list = new ArrayList<SqliteBaseDALEx>(); 
		Cursor cursor = null;
		SqliteBaseDALEx baseDalex = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find(sql,params);
                
                SqliteAnnotationCache cache = UtionObjectCache.getInstance().getSqliteAnnotationCache();
        		SqliteAnnotationTable table = cache.getTable(TABLE_NAME,this.getClass());
        		Map<String, Integer> cursorIndex = table.getCursorIndex(cursor);
        		
                while (cursor != null && cursor.moveToNext()) {
                	if(baseDalex==null){
                		baseDalex = newDALExInstance();
                	}
                    SqliteBaseDALEx dalex = (SqliteBaseDALEx) baseDalex.clone();
                	dalex.setAnnotationField(cursor,cursorIndex);

                	list.add(dalex);
					if(listener!=null)listener.onResult(cursor,null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if(listener!=null)listener.onException(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }
		
		return (List<T>)list;
	}
	
	
	
	public void deleteById(String id){
		UtionDB db;
    	try {
			db = getDB();
			db.delete(TABLE_NAME, getPrimaryKey() +"=?", new String[]{id});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** ------------------------------- save -------------------------------*/
    public void saveOrUpdate(final SqliteBaseDALEx[] dalex){
        operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                for(SqliteBaseDALEx model:dalex){
                    String id = model.getPrimaryId();
                    if(TextUtils.isEmpty(id))continue;
                    createTable(db);
                    ContentValues values = model.tranform2Values();
                    if (!TextUtils.isEmpty(id) && isExist(id)) {
                        db.update(TABLE_NAME, values, getPrimaryKey() + "=?", new String[]{id});
                    } else {
                        db.save(TABLE_NAME, values);
                    }
                }

                return true;
            }
        });
    }

	public void saveOrUpdate(){

        UtionDB db = getDB();
        String id = getPrimaryId();
        if(TextUtils.isEmpty(id))return;
        createTable(db);
        ContentValues values = tranform2Values();
        if(!TextUtils.isEmpty(id) && isExist(id)){
            db.update(TABLE_NAME, values, getPrimaryKey()+"=?", new String[]{id});
        }else{
            db.save(TABLE_NAME, values);
        }

	}


    //sql语句要写对啊
    public int count(String sql,String[] params){
        int result = 0;
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find(sql,params);
                if (cursor != null && cursor.moveToNext()) {
                    result = cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }


}