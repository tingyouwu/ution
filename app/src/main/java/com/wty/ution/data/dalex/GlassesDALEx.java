package com.wty.ution.data.dalex;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.filter.base.IFilterModel;
import com.xtion.sheet.model.ISheetRowModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能描述：眼镜销售记录表
 * @author wty
 **/
public class GlassesDALEx extends SqliteBaseDALEx implements ISheetRowModel{

    private static final long serialVersionUID = 1L;

    public static final String GLASSESID = "glassesid";
    public static final String XWDATE = "xwdate";
    public static final String XWCOSTPRICE = "xwcostprice";
    public static final String XWSELLPRICE = "xwsellprice";
    public static final String XWPROFIT = "xwprofit";
    public static final String XWTYPE = "xwtype";
    public static final String XWSTATUS = "xwstatus";

    public static final int Status_Normal = 0;
    public static final int Status_Delete = 1;

    @DatabaseField(primaryKey = true, Type = DatabaseField.FieldType.VARCHAR)
    private String glassesid;                             // 眼镜销售记录id

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwcostprice;               //成本价

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwsellprice;               //售价

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwprofit;               //利润

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwtype;//眼镜款式

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwdate;                //发货时间

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwsize;                //码数

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwcreatetime;                //创建时间

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwupdatetime;                //修改时间

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwstatus;                          //是否是删除状态

    public static GlassesDALEx get() {
        return (GlassesDALEx) SqliteDao.getDao(GlassesDALEx.class);
    }

    /**
     * 保存单个对象
     **/
    public void save(final GlassesDALEx dalex){
        operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                saveOne(dalex,db);
                return true;
            }
        });
    }

    /**
     * 保存单个对象
     **/
    public boolean saveReturn(final GlassesDALEx dalex){
        return operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                saveOne(dalex,db);
                return true;
            }
        });
    }

    /**
     * 保存一系列对象
     **/
    public void save(final GlassesDALEx[] list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (GlassesDALEx dalex : list) {
                    saveOne(dalex,db);
                }
                return true;
            }
        });
    }

    /**
     * 保存一系列对象
     **/
    public void save(final List<GlassesDALEx> list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (GlassesDALEx dalex : list) {
                    saveOne(dalex, db);
                }
                return true;
            }
        });
    }

    private void saveOne(GlassesDALEx dalex,UtionDB db){
        ContentValues values = dalex.tranform2Values();
        if (isExist(GLASSESID, dalex.getGlassesid())) {
            db.update(TABLE_NAME, values, GLASSESID + "=?", new String[]{dalex.getGlassesid()});
        } else {
            db.save(TABLE_NAME, values);
        }
    }

    /**
     * 根据物流id返回一个物流对象
     **/
    public GlassesDALEx queryById(final String projectid) {
        GlassesDALEx glass = findById(projectid, new OnQueryListener() {
            @Override
            public void onResult(Cursor cursor, SqliteBaseDALEx dalex) {
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        });
        return glass;
    }

    /**
     * 从第offset条记录开始开始返回limit条数据
     **/
    public List<GlassesDALEx> queryLimitByOffset(int limit,int offset) {
        //Log.v("性能调试", " logistics queryByFilters queryAll");
        String sql = new StringBuilder().append(" select * from ").append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 0 ")
                .append(" order by datetime(" + XWDATE + ") desc ")
                .append(" limit " + limit + " offset " + offset)
                .toString();
        List<GlassesDALEx> result = findList(sql, new String[]{});
        return result;
    }

    /**
     * 返回本月发货量
     **/
    public int countCurrentMonthAmount() {
        Log.v("性能调试", " glass queryByFilters countCurrentMonthAmount");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select count(*) from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 0 ")
                .append(" and date(" + XWDATE + ") ")
                .append(" between ").append(" date('now','start of month','+1 second') ")
                .append(" and ").append("date('now','start of month','+1 month','-1 second')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " glass queryByFilters 耗时 countCurrentMonthAmount"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 返回本月销售额
     **/
    public int countCurrentMonthMoney() {
        Log.v("性能调试", " glass queryByFilters countCurrentMonthMoney");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select sum(" + XWSELLPRICE +") from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 0 ")
                .append(" and date(" + XWDATE + ") ")
                .append(" between ").append(" date('now','start of month','+1 second') ")
                .append(" and ").append("date('now','start of month','+1 month','-1 second')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " glass queryByFilters 耗时 countCurrentMonthMoney"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 返回今天发货量
     **/
    public int countTodayAmount() {
        return countDayAmount(CommonUtil.getTime());
    }

    /**
     * 返回某天发货量
     **/
    public int countDayAmount(String datetime) {
        Log.v("性能调试", " glass queryByFilters countDayAmount");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select count(*) from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 0 ")
                .append(" and date(" + XWDATE + ") = ")
                .append("date('" + datetime + "')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " glass queryByFilters 耗时 countDayAmount"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 返回今天总销售额
     **/
    public int countTodayMoney() {
        Log.v("性能调试", " glass queryByFilters countTodayMoney");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select sum(" + XWSELLPRICE + ") from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 0 ")
                .append(" and date(" + XWDATE + ") = ")
                .append("date('"+ CommonUtil.getTime() + "')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " glass queryByFilters 耗时 countTodayMoney"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据筛选条件过滤出列表
     */
    @SuppressWarnings("unchecked")
    public List<GlassesDALEx> queryByFilters(List<IFilterModel> filters,IFilterModel order) {
        return queryByFilters(filters,order,"");
    }

    /**
     * 根据筛选条件过滤出列表
     */
    @SuppressWarnings("unchecked")
    public List<GlassesDALEx> queryByFilters(List<IFilterModel> filters,IFilterModel order,String defaultCondition) {

        Log.v("性能调试", "筛选列表 GlassesDALEx queryByFilters ");
        long t = System.currentTimeMillis();
        List<String> conditions = new ArrayList<String>();
        if(filters!=null){

            for(IFilterModel filter: filters){
                //组装过滤条件
                if(!TextUtils.isEmpty(filter.toSql())){
                    conditions.add(filter.toSql());
                }
            }
        }

        if(!TextUtils.isEmpty(defaultCondition))
            conditions.add(defaultCondition);

        String orderBy = (order!=null && !TextUtils.isEmpty(order.toSql())) ? " order by "+ order.toSql():"";
        String condition = TextUtils.join(" and ", conditions);

        String sqlArg = " select * from "+ TABLE_NAME
                + (TextUtils.isEmpty(condition)?" where " + XWSTATUS + " = 0 ":" where " + XWSTATUS + " = 0 and "+condition)
                + orderBy;
        List<GlassesDALEx> result = findList(sqlArg, new String[]{});
        Log.v("性能调试", "筛选 GlassesDALEx queryByFilters 耗时 "+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据筛选条件计算利润总和
     */
    @SuppressWarnings("unchecked")
    public long countProfitByFilters(List<IFilterModel> filters) {
        return countTotalByFilters(XWPROFIT,filters);
    }

    /**
     * 根据筛选条件计算销售总和
     */
    @SuppressWarnings("unchecked")
    public long countSellByFilters(List<IFilterModel> filters) {
        return countTotalByFilters(XWSELLPRICE,filters);
    }

    /**
     * 根据筛选条件计算成本总和
     */
    @SuppressWarnings("unchecked")
    public long countCostByFilters(List<IFilterModel> filters) {
        return countTotalByFilters(XWCOSTPRICE,filters);
    }

    /**
     * 根据筛选条件计算总和
     */
    @SuppressWarnings("unchecked")
    public long countTotalByFilters(String key,List<IFilterModel> filters) {

        long result =0;
        Log.v("性能调试", "成本 Glass queryByFilters ");
        long t = System.currentTimeMillis();
        List<String> conditions = new ArrayList<String>();
        if(filters!=null){
            for(IFilterModel filter: filters){
                //组装过滤条件
                if(!TextUtils.isEmpty(filter.toSql())){
                    conditions.add(filter.toSql());
                }
            }
        }

        String condition = TextUtils.join(" and ", conditions);

        String sqlArg = " select sum(" + key + ") from "+ TABLE_NAME
                + (TextUtils.isEmpty(condition)?" where " + XWSTATUS + " = 0 ":" where " + XWSTATUS + " = 0 and "+condition);

        result = count(sqlArg, new String[]{});
        Log.v("性能调试", "成本 Glass queryByFilters 耗时 "+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据id删除
     */
    public void deleteById(String id){
        UtionDB db;
        try {
            db = getDB();
            db.delete(TABLE_NAME, GLASSESID +"=?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGlassesid() {
        return glassesid;
    }

    public void setGlassesid(String glassesid) {
        this.glassesid = glassesid;
    }

    public int getXwcostprice() {
        return xwcostprice;
    }

    public void setXwcostprice(int xwcostprice) {
        this.xwcostprice = xwcostprice;
    }

    public int getXwsellprice() {
        return xwsellprice;
    }

    public void setXwsellprice(int xwsellprice) {
        this.xwsellprice = xwsellprice;
    }

    public int getXwprofit() {
        return xwprofit;
    }

    public void setXwprofit(int xwprofit) {
        this.xwprofit = xwprofit;
    }

    public String getXwtype() {
        return xwtype;
    }

    public void setXwtype(String xwtype) {
        this.xwtype = xwtype;
    }

    public String getXwcreatetime() {
        return xwcreatetime;
    }

    public void setXwcreatetime(String xwcreatetime) {
        this.xwcreatetime = xwcreatetime;
    }

    public int getXwstatus() {
        return xwstatus;
    }

    public void setXwstatus(int xwstatus) {
        this.xwstatus = xwstatus;
    }

    public String getXwdate() {
        return xwdate;
    }

    public void setXwdate(String xwdate) {
        this.xwdate = xwdate;
    }

    public String getXwupdatetime() {
        return xwupdatetime;
    }

    public void setXwupdatetime(String xwupdatetime) {
        this.xwupdatetime = xwupdatetime;
    }

    public int getXwsize() {
        return xwsize;
    }

    public void setXwsize(int xwsize) {
        this.xwsize = xwsize;
    }

    @Override
    public String getCellText() {
        return "";
    }

    @Override
    public Map<String, String> getCellValue() {
        Map<String, String> result = new HashMap<String,String>();
        result.put(GlassesDALEx.XWTYPE,""+xwtype);
        result.put(GlassesDALEx.XWDATE,CommonUtil.dateToYYYYMMdd(xwdate));
        result.put(GlassesDALEx.XWCOSTPRICE,""+xwcostprice);
        result.put(GlassesDALEx.XWSELLPRICE,""+xwsellprice);
        result.put(GlassesDALEx.XWPROFIT,""+xwprofit);
        return result;
    }
}