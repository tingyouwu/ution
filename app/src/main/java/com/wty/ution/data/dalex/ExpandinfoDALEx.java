package com.wty.ution.data.dalex;

import android.content.ContentValues;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;

import java.util.List;


public class ExpandinfoDALEx extends SqliteBaseDALEx {
    /**
     * 扩展信息表
     */
    
    public static final String Name_Logistics = "物流";
    public static final String Name_Glass = "眼镜";

    private static final long serialVersionUID = 1L;
    
    public static final String KEY  	= "key";
    public static final String NAME    	= "name";
    public static final String TABLENAME= "tablename";
    public static final String TIMESPAN = "timespan";
    
    @DatabaseField(primaryKey=true,Type= FieldType.VARCHAR)
    private String key;
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String name;
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String tablename; 
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String timespan;
    
    private FieldDescriptDALEx[] fieldDescript; 
    
    public static ExpandinfoDALEx get() {
        return SqliteDao.getDao(ExpandinfoDALEx.class);
    }

    /**
     * key值为info表中的主键
     */
    public ExpandinfoDALEx queryByKey(String key) {
        return findOne("select * from " + TABLE_NAME + " where " + KEY + "=?", new String[]{key});
    }
    
    /**
     * 根据实体的对象名，查询info中的key值
     */
    public ExpandinfoDALEx queryByName(String name) {
       return findOne("select * from "+ TABLE_NAME +" where "+ NAME +"=?",new String[] {name});
    }

    /**
     * 保存一系列物流对象
     **/
    public void save(final ExpandinfoDALEx[] list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (ExpandinfoDALEx dalex : list) {
                    ContentValues values = dalex.tranform2Values();

                    if (isExist(KEY, dalex.getKey())) {
                        db.update(TABLE_NAME, values, KEY + "=?",new String[] { dalex.getKey() });
                    } else {
                        db.save(TABLE_NAME, values);
                    }

                    //在字段描述表中，清除该实体对应的描述数据
                    FieldDescriptDALEx.get().deleteByEntityregid(dalex.getKey());
                    //重新保存
                    if(dalex.getFieldDescript()!=null && dalex.getFieldDescript().length!=0){
                        FieldDescriptDALEx.get().save(dalex.getFieldDescript(), dalex.getName());
                    }

                    //存储自定义扩展字段
                    if(dalex.getName().equals(ExpandinfoDALEx.Name_Logistics)){
//                        CustExfieldValueDALEx.get().fillColumnByDescript(CustExfieldValueDALEx.get().getTableName(),
//                                dalex.getFieldDescript());
                    }

                }
                return true;
            }
        });
    }

    /**
     * 保存一系列物流对象
     **/
    public void save(final List<ExpandinfoDALEx> list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (ExpandinfoDALEx dalex : list) {
                    ContentValues values = dalex.tranform2Values();

                    if (isExist(KEY, dalex.getKey())) {
                        db.update(TABLE_NAME, values, KEY + "=?",new String[] { dalex.getKey() });
                    } else {
                        db.save(TABLE_NAME, values);
                    }

                    //在字段描述表中，清除该实体对应的描述数据
                    FieldDescriptDALEx.get().deleteByEntityregid(dalex.getKey());
                    //重新保存
                    if(dalex.getFieldDescript()!=null && dalex.getFieldDescript().length!=0){
                        FieldDescriptDALEx.get().save(dalex.getFieldDescript(), dalex.getName());
                    }

                    //存储自定义扩展字段
                    if(dalex.getName().equals(ExpandinfoDALEx.Name_Logistics)){
//                        CustExfieldValueDALEx.get().fillColumnByDescript(CustExfieldValueDALEx.get().getTableName(),
//                                dalex.getFieldDescript());
                    }

                }
                return true;
            }
        });
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTablename() {
        return tablename;
    }

    public String getSqliteTablename() {
    	return super.getTableName();
    }
    
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

	public FieldDescriptDALEx[] getFieldDescript() {
		return fieldDescript;
	}

	public void setFieldDescript(FieldDescriptDALEx[] fieldDescript) {
		this.fieldDescript = fieldDescript;
	}

}
