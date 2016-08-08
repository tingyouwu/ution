package com.wty.ution.data.dalex;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;
import com.wty.ution.data.dalex.basedata.RegionInfoDALEx;
import com.wty.ution.util.CommonUtil;
import com.wty.ution.widget.filter.base.IFilterModel;
import com.xtion.sheet.model.ISheetRowModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能描述：物流信息表
 **/
public class LogisticsDALEx extends SqliteBaseDALEx implements ISheetRowModel{

    private static final long serialVersionUID = 1L;

    public static final String LOGISTICSID = "logisticsid";
    public static final String XWDATE = "xwdate";
    public static final String XWPRICE = "xwprice";
    public static final String XWSOURCE = "xwsource";
    public static final String XWSOURCEPATH = "xwsourceidpath";
    public static final String XWDESRTINATION = "xwdestination";
    public static final String XWDESRTINATIONPATH = "xwdestinationidpath";
    public static final String XWSTATUS = "xwstatus";

    public static final int Status_Delete = 0;
    public static final int Status_Normal = 1;

    @DatabaseField(primaryKey = true, Type = DatabaseField.FieldType.VARCHAR)
    private String logisticsid;                             // 物流信息id

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwcontactid;                             // 联系人id

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwsource;                          //出发地

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwdestination;               //目标地址

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwprice;               //价格

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwcreatetime;                //创建时间

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwupdatetime;                //修改时间

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String xwdate;                //发货时间

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String  xwsourceidpath;//出发地组合路径

    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String  xwdestinationidpath;//目标地址组合路径

    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int xwstatus;                          //是否是删除状态

    public static LogisticsDALEx get() {
        return (LogisticsDALEx) SqliteDao.getDao(LogisticsDALEx.class);
    }

    /**
     * 保存单个物流对象
     **/
    public void save(final LogisticsDALEx dalex){
        operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                saveOne(dalex,db);
                return true;
            }
        });
    }

    /**
     * 保存单个物流对象
     **/
    public boolean saveReturn(final LogisticsDALEx dalex){
        return operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                saveOne(dalex,db);
                return true;
            }
        });
    }

    /**
     * 保存一系列物流对象
     **/
    public void save(final LogisticsDALEx[] list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (LogisticsDALEx dalex : list) {
                    saveOne(dalex,db);
                }
                return true;
            }
        });
    }

    /**
     * 保存一系列物流对象
     **/
    public void save(final List<LogisticsDALEx> list){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (LogisticsDALEx dalex : list) {
                    saveOne(dalex,db);
                }
                return true;
            }
        });
    }

    private void saveOne(LogisticsDALEx dalex,UtionDB db){
        ContentValues values = dalex.tranform2Values();
        if (isExist(LOGISTICSID, dalex.getLogisticsid())) {
            db.update(TABLE_NAME, values, LOGISTICSID + "=?", new String[]{dalex.getLogisticsid()});
        } else {
            db.save(TABLE_NAME, values);
        }
    }

    /**
     * 根据物流id返回一个物流对象
     **/
    public LogisticsDALEx queryById(final String projectid) {
        LogisticsDALEx logis = findById(projectid, new OnQueryListener() {
            @Override
            public void onResult(Cursor cursor, SqliteBaseDALEx dalex) {
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        });
        return logis;
    }

    /**
     * 从第offset条记录开始开始返回limit条数据
     **/
    public List<LogisticsDALEx> queryLimitByOffset(int limit,int offset) {
        //Log.v("性能调试", " logistics queryByFilters queryAll");
        String sql = new StringBuilder().append(" select * from ")
                .append(TABLE_NAME)
                .append(" where "+ XWSTATUS + " = 1")
                .append(" order by datetime("+XWDATE+")desc ")
                .append(" limit " + limit + " offset " + offset)
                .toString();
        List<LogisticsDALEx> result = findList(sql, new String[]{});
        return result;
    }

    /**
     * 返回本月发货量
     **/
    public int countCurrentMonthAmount() {
        Log.v("性能调试", " logistics queryByFilters countCurrentMonthAmount");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select count(*) from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 1 ")
                .append(" and date(" + XWDATE + ") ")
                .append(" between ").append(" date('now','start of month','+1 second') ")
                .append(" and ").append("date('now','start of month','+1 month','-1 second')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " logistics queryByFilters 耗时 countCurrentMonthAmount"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 返回本月总运费
     **/
    public int countCurrentMonthMoney() {
        Log.v("性能调试", " logistics queryByFilters countCurrentMonthMoney");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select sum(" + XWPRICE +") from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 1")
                .append(" and date(" + XWDATE + ") ")
                .append(" between ").append(" date('now','start of month','+1 second') ")
                .append(" and ").append("date('now','start of month','+1 month','-1 second')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " logistics queryByFilters 耗时 countCurrentMonthMoney"+(System.currentTimeMillis() -t));
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
        Log.v("性能调试", " logistics queryByFilters countDayAmount");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select count(*) from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 1 ")
                .append(" and date(" + XWDATE + ") = ")
                .append("date('" + datetime + "')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " logistics queryByFilters 耗时 countDayAmount"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 返回今天总运费
     **/
    public int countTodayMoney() {
        Log.v("性能调试", " logistics queryByFilters countTodayMoney");
        long t = System.currentTimeMillis();
        String sql = new StringBuilder().append(" select sum(" + XWPRICE + ") from ")
                .append(TABLE_NAME)
                .append(" where " + XWSTATUS + " = 1 ")
                .append(" and date(" + XWDATE + ") = ")
                .append("date('"+ CommonUtil.getTime() + "')")
                .toString();
        int result = count(sql, new String[]{});
        Log.v("性能调试", " logistics queryByFilters 耗时 countTodayMoney"+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据筛选条件过滤出物流列表
     */
    @SuppressWarnings("unchecked")
    public List<LogisticsDALEx> queryByFilters(List<IFilterModel> filters,IFilterModel order) {
        return queryByFilters(filters,order,"");
    }

    /**
     * 根据筛选条件过滤出物流列表
     */
    @SuppressWarnings("unchecked")
    public List<LogisticsDALEx> queryByFilters(List<IFilterModel> filters,IFilterModel order,String defaultCondition) {

        Log.v("性能调试", "筛选 LogisticsDALEx queryByFilters ");
        long t = System.currentTimeMillis();
        List<String> conditions = new ArrayList<String>();
        if(filters!=null){

            for(IFilterModel filter: filters){
                //组装过滤条件

                if(filter.getFilterId().equals(XWSOURCE) && !TextUtils.isEmpty(filter.toSql())){
                    conditions.add(XWSOURCEPATH + " like '%" + filter.getValue() + "%' ");
                    continue;
                }

                if(filter.getFilterId().equals(XWDESRTINATION) && !TextUtils.isEmpty(filter.toSql())){
                    conditions.add(XWDESRTINATIONPATH + " like '%" + filter.getValue() + "%' ");
                    continue;
                }

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
                + (TextUtils.isEmpty(condition)?" where " + XWSTATUS + " = 1 ":" where " + XWSTATUS + " = 1 and "+condition)
                + orderBy;
        List<LogisticsDALEx> result = findList(sqlArg, new String[]{});
        Log.v("性能调试", "筛选 LogisticsDALEx queryByFilters 耗时 "+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据筛选条件计算运费总和
     */
    @SuppressWarnings("unchecked")
    public long countMoneyByFilters(List<IFilterModel> filters,IFilterModel order) {

        long result =0;
        Log.v("性能调试", "运费 LogisticsDALEx queryByFilters ");
        long t = System.currentTimeMillis();
        List<String> conditions = new ArrayList<String>();
        if(filters!=null){
            for(IFilterModel filter: filters){
                //组装过滤条件

                if(filter.getFilterId().equals(XWSOURCE) && !TextUtils.isEmpty(filter.toSql())){
                    conditions.add(XWSOURCEPATH + " like '%" + filter.getValue() + "%' ");
                    continue;
                }

                if(filter.getFilterId().equals(XWDESRTINATION) && !TextUtils.isEmpty(filter.toSql())){
                    conditions.add(XWDESRTINATIONPATH + " like '%" + filter.getValue() + "%' ");
                    continue;
                }

                if(!TextUtils.isEmpty(filter.toSql())){
                    conditions.add(filter.toSql());
                }
            }
        }

        String condition = TextUtils.join(" and ", conditions);

        String sqlArg = " select sum(" + XWPRICE + ") from "+ TABLE_NAME
                + (TextUtils.isEmpty(condition)?" where " + XWSTATUS + " = 1 ":" where " + XWSTATUS + " = 1 and "+condition);

        result = count(sqlArg, new String[]{});
        Log.v("性能调试", "运费 LogisticsDALEx queryByFilters 耗时 "+(System.currentTimeMillis() -t));
        return result;
    }

    /**
     * 根据id删除
     */
    public void deleteById(String id){
        UtionDB db;
        try {
            db = getDB();
            db.delete(TABLE_NAME, LOGISTICSID +"=?", new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLogisticsid() {
        return logisticsid;
    }

    public void setLogisticsid(String logisticsid) {
        this.logisticsid = logisticsid;
    }

    public String getXwcontactid() {
        return xwcontactid;
    }

    public void setXwcontactid(String xwcontactid) {
        this.xwcontactid = xwcontactid;
    }

    public int getXwsource() {
        return xwsource;
    }

    public void setXwsource(int xwsource) {
        this.xwsource = xwsource;
    }

    public String getXwcreatetime() {
        return xwcreatetime;
    }

    public void setXwcreatetime(String xwcreatetime) {
        this.xwcreatetime = xwcreatetime;
    }

    public int getXwdestination() {
        return xwdestination;
    }

    public void setXwdestination(int xwdestination) {
        this.xwdestination = xwdestination;
    }

    public String getXwprice() {
        return xwprice;
    }

    public void setXwprice(String xwprice) {
        this.xwprice = xwprice;
    }

    public String getXwdate() {
        return xwdate;
    }

    public void setXwdate(String xwdate) {
        this.xwdate = xwdate;
    }

    public String getXwsourceidpath() {
        return xwsourceidpath;
    }

    public void setXwsourceidpath(String xwsourceidpath) {
        this.xwsourceidpath = xwsourceidpath;
    }

    public String getXwdestinationidpath() {
        return xwdestinationidpath;
    }

    public void setXwdestinationidpath(String xwdestinationidpath) {
        this.xwdestinationidpath = xwdestinationidpath;
    }

    public String getXwupdatetime() {
        return xwupdatetime;
    }

    public void setXwupdatetime(String xwupdatetime) {
        this.xwupdatetime = xwupdatetime;
    }

    public int getXwstatus() {
        return xwstatus;
    }

    public void setXwstatus(int xwstatus) {
        this.xwstatus = xwstatus;
    }

    @Override
    public String getCellText() {
        return "";
    }

    @Override
    public Map<String, String> getCellValue() {
        Map<String, String> result = new HashMap<String,String>();

        result.put("xwsource",RegionInfoDALEx.get().queryById(xwsource).getNamepath());
        result.put("xwdestination",RegionInfoDALEx.get().queryById(xwdestination).getNamepath());

        result.put("xwprice",xwprice);
        result.put("xwdate", CommonUtil.dateToYYYYMMdd(xwdate));

        return result;
    }
}