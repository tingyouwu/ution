package com.wty.ution.data.dalex;

import android.content.ContentValues;
import android.database.Cursor;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;


/**
 * 功能描述：增量更新数据 时间记录表
 * @author wty
 **/
public class IncUpdateDALEx extends SqliteBaseDALEx {
    private static final long serialVersionUID = 1L;
    private static final String INCREMENTNAME = "incrementname";         // id(guid) 手机端生成
    private static final String UPDATETIME = "updatetime";                 // 消息id手机端生成uuid
    
    public static final String Update_Glass = "update_Glass";
    public static final String Update_Logistics = "update_Logistics";
    public static final String Update_Incrementdata = "update_incrementdata";

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String incrementname;//对应更新类型
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String updatetime;//上一次更新时间
    
    public static IncUpdateDALEx get() {
        return SqliteDao.getDao(IncUpdateDALEx.class);
    }

    /**
     * 保存单个对象
     **/
    public void save(final IncUpdateDALEx dalex){
        operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                saveOne(dalex,db);
                return true;
            }
        });
    }

    private void saveOne(IncUpdateDALEx dalex,UtionDB db){
        ContentValues values = dalex.tranform2Values();
        if (isExist(INCREMENTNAME, dalex.getIncrementname())) {
            db.update(TABLE_NAME, values, INCREMENTNAME + "=?", new String[]{dalex.getIncrementname()});
        } else {
            db.save(TABLE_NAME, values);
        }
    }


    public IncUpdateDALEx queryByName(String id) {
        IncUpdateDALEx dalex = null;
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find("select * from "+ TABLE_NAME + "  where " + INCREMENTNAME + "=? ",new String[] { id });
                if (cursor!=null && cursor.moveToNext()) {
                    dalex = new IncUpdateDALEx();
                    dalex.setAnnotationField(cursor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return dalex;
    }
    

	public String getIncrementname() {
		return incrementname;
	}

	public void setIncrementname(String incrementname) {
		this.incrementname = incrementname;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
    
}