package com.wty.ution.data.dalex.basedata;

import android.content.ContentValues;
import android.database.Cursor;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;
import com.wty.ution.widget.navigation.NavigateAble;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：保存省市地址信息
 **/
public class RegionInfoDALEx extends SqliteBaseDALEx implements NavigateAble{
    
    private static final long serialVersionUID = 1L;

    private static final String REGIONID         = "regionid";
    private static final String REGIONNAME       = "regionname";
    private static final String REGIONTYPE       = "regiontype";
    private static final String REGIONYPENAME    = "regionypename";
    private static final String REGIONCODE       = "regioncode";
    private static final String IDPATH           = "idpath";
    private static final String CODEPATH         = "codepath";
    private static final String NAMEPATH         = "namepath";
    private static final String PREGIONID        = "pregionid";
    public static final int defaultId = 100000;//默认中国

    @DatabaseField(primaryKey=true,Type = FieldType.INT)
    private int                 regionid;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              regionname;
    @DatabaseField(Type = FieldType.INT)
    private int                 regiontype;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              regionypename;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              regioncode;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              idpath;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              codepath;
    @DatabaseField(Type = FieldType.VARCHAR)
    private String              namepath;
    @DatabaseField(Type = FieldType.INT)
    private int                 pregionid;
    
    public static RegionInfoDALEx get() {
        return (RegionInfoDALEx) SqliteDao.getDao(RegionInfoDALEx.class);
    }

    /**
     * 保存一系列对象
     **/
    public void save(final RegionInfoDALEx[] list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (RegionInfoDALEx dalex : list) {
                    ContentValues values = dalex.tranform2Values();
                    db.save(TABLE_NAME, values);
                }
                return true;
            }
        });
    }
    
    /**
     * 查询所有
     * @return
     */
    public List<RegionInfoDALEx> queryAll() {
        List<RegionInfoDALEx> list = new ArrayList<RegionInfoDALEx>();
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find("select * from "+ TABLE_NAME ,new String[] {});
                while (cursor != null && cursor.moveToNext()) {
                    RegionInfoDALEx dalex = new RegionInfoDALEx();
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
    
    /**
     * @param pregionid
     * @return
     */
    public List<RegionInfoDALEx> queryPregions(int pregionid) {
        
        List<RegionInfoDALEx> list = new ArrayList<RegionInfoDALEx>();
        Cursor cursor = null;
        try {
            UtionDB db = getDB();
            if (db.isTableExits(TABLE_NAME)) {
                cursor = db.find("select * from "+ TABLE_NAME +" where "+PREGIONID+" =? ",new String[] {""+pregionid});
                while (cursor != null && cursor.moveToNext()) {
                    RegionInfoDALEx dalex = new RegionInfoDALEx();
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
    
    
    /**
     * 
     * @param id
     * @return
     */
    public RegionInfoDALEx queryById(int id) {
        return findById(String.valueOf(id));
    }

    public int getRegionid() {
        return regionid;
    }

    public void setRegionid(int regionid) {
        this.regionid = regionid;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public int getRegiontype() {
        return regiontype;
    }

    public void setRegiontype(int regiontype) {
        this.regiontype = regiontype;
    }

    public String getRegionypename() {
        return regionypename;
    }

    public void setRegionypename(String regionypename) {
        this.regionypename = regionypename;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public String getIdpath() {
        return idpath;
    }

    public void setIdpath(String idpath) {
        this.idpath = idpath;
    }

    public String getCodepath() {
        return codepath;
    }

    public void setCodepath(String codepath) {
        this.codepath = codepath;
    }

    public String getNamepath() {
        return namepath;
    }

    public void setNamepath(String namepath) {
        this.namepath = namepath;
    }

    public int getPregionid() {
        return pregionid;
    }

    public void setPregionid(int pregionid) {
        this.pregionid = pregionid;
    }

    @Override
    public String getNavigateLabel() {
        return regionname;
    }
}







