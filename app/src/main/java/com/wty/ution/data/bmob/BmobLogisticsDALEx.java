package com.wty.ution.data.bmob;

import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.bmob.BmobObjectDao;
import com.wty.ution.data.dalex.LogisticsDALEx;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：物流记录表  Bmob
 *          跟本地是一样的,需要去掉主键id
 * @author wty
 **/
public class BmobLogisticsDALEx extends BaseBmobObject{

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

    public BmobLogisticsDALEx(){
        //由于限制  类名不能超过20个字
        super(BmobLogisticsDALEx.class.getSimpleName());
    }

    public static BmobLogisticsDALEx get(){
        return BmobObjectDao.getDao(BmobLogisticsDALEx.class);
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

    public String getXwcreatetime() {
        return xwcreatetime;
    }

    public void setXwcreatetime(String xwcreatetime) {
        this.xwcreatetime = xwcreatetime;
    }

    public String getXwupdatetime() {
        return xwupdatetime;
    }

    public void setXwupdatetime(String xwupdatetime) {
        this.xwupdatetime = xwupdatetime;
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

    public int getXwstatus() {
        return xwstatus;
    }

    public void setXwstatus(int xwstatus) {
        this.xwstatus = xwstatus;
    }

    public void save(final List<BmobLogisticsDALEx> list){
        List<LogisticsDALEx> localdalex = new ArrayList<LogisticsDALEx>();
        for(BmobLogisticsDALEx bmob:list){
            LogisticsDALEx dalex = new LogisticsDALEx();
            dalex.setAnnotationField(bmob.getAnnotationFieldValue());
            dalex.setLogisticsid(bmob.getObjectId());
            localdalex.add(dalex);
        }
        LogisticsDALEx.get().save(localdalex);
    }

    public List<LogisticsDALEx> saveReturn(final List<BmobLogisticsDALEx> list){
        List<LogisticsDALEx> localdalex = new ArrayList<LogisticsDALEx>();
        for(BmobLogisticsDALEx bmob:list){
            LogisticsDALEx dalex = new LogisticsDALEx();
            dalex.setAnnotationField(bmob.getAnnotationFieldValue());
            dalex.setLogisticsid(bmob.getObjectId());
            localdalex.add(dalex);
        }
        LogisticsDALEx.get().save(localdalex);
        return localdalex;
    }

}