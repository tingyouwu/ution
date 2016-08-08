package com.wty.ution.data.bmob;

import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.bmob.BmobObjectDao;
import com.wty.ution.data.dalex.GlassesDALEx;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：眼镜销售记录表  Bmob
 *          跟本地是一样的,需要去掉主键id
 * @author wty
 **/
public class BmobGlassesDALEx extends BaseBmobObject{

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

    public BmobGlassesDALEx(){
        //由于限制  类名不能超过20个字
        super(BmobGlassesDALEx.class.getSimpleName());
    }

    public static BmobGlassesDALEx get(){
        return BmobObjectDao.getDao(BmobGlassesDALEx.class);
    }

    public int getXwstatus() {
        return xwstatus;
    }

    public void setXwstatus(int xwstatus) {
        this.xwstatus = xwstatus;
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

    public void save(final List<BmobGlassesDALEx> list){
        List<GlassesDALEx> localdalex = new ArrayList<GlassesDALEx>();
        for(BmobGlassesDALEx bmob:list){
            GlassesDALEx dalex = new GlassesDALEx();
            dalex.setAnnotationField(bmob.getAnnotationFieldValue());
            dalex.setGlassesid(bmob.getObjectId());
            localdalex.add(dalex);
        }
        GlassesDALEx.get().save(localdalex);
    }

    public List<GlassesDALEx> saveReturn(final List<BmobGlassesDALEx> list){
        List<GlassesDALEx> localdalex = new ArrayList<GlassesDALEx>();
        for(BmobGlassesDALEx bmob:list){
            GlassesDALEx dalex = new GlassesDALEx();
            dalex.setAnnotationField(bmob.getAnnotationFieldValue());
            dalex.setGlassesid(bmob.getObjectId());
            localdalex.add(dalex);
        }
        GlassesDALEx.get().save(localdalex);
        return localdalex;
    }

}