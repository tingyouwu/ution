package com.wty.ution.data.dalex.basedata;

import android.content.ContentValues;
import android.database.Cursor;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;

import java.util.ArrayList;
import java.util.List;

public class IndustryinfoDALEx extends SqliteBaseDALEx {
    
	private static final long serialVersionUID = 1L;
	public  static final String XWINDUSTRYID = "xwindustryid";
	public  static final String XWINDUSTRYNAME = "xwindustryname";
	public  static final String XWSTATUS = "xwstatus";
    
    @DatabaseField(primaryKey=true,Type = FieldType.VARCHAR)
    private String xwindustryid;
    
    @DatabaseField(Type = FieldType.VARCHAR)
    private String xwindustryname;
    
    @DatabaseField(Type = FieldType.INT)
    private int xwstatus;
    
    public static IndustryinfoDALEx get() {
		return (IndustryinfoDALEx) SqliteDao.getDao(IndustryinfoDALEx.class);
	}

    /**
     * 保存一系列对象
     **/
    public void save(final IndustryinfoDALEx[] list,UtionDB db){
        db.execSQL("delete from "+ TABLE_NAME);
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (IndustryinfoDALEx dalex : list) {
                    ContentValues values = dalex.tranform2Values();
                    db.save(TABLE_NAME, values);
                }
                return true;
            }
        });
    }
    
    public List<IndustryinfoDALEx> queryAll() {
        List<IndustryinfoDALEx> list = new ArrayList<IndustryinfoDALEx>();
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find("select * from "+ TABLE_NAME +" where "+XWSTATUS+" =1 ",new String[] {});
                while (cursor != null && cursor.moveToNext()) {
                    IndustryinfoDALEx dalex = new IndustryinfoDALEx();
                    dalex.setAnnotationField(cursor);
                    list.add(dalex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }
    
    public String getXwindustryid() {
        return xwindustryid;
    }
    public void setXwindustryid(String xwindustryid) {
        this.xwindustryid = xwindustryid;
    }
    public String getXwindustryname() {
        return xwindustryname;
    }
    public void setXwindustryname(String xwindustryname) {
        this.xwindustryname = xwindustryname;
    }
	public int getXwstatus() {
		return xwstatus;
	}
	public void setXwstatus(int xwstatus) {
		this.xwstatus = xwstatus;
	}
    
    
}
