package com.wty.ution.data.dalex;

import android.content.ContentValues;

import com.wty.ution.data.UtionDB;
import com.wty.ution.data.annotation.DatabaseField;
import com.wty.ution.data.annotation.DatabaseField.FieldType;
import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.annotation.SqliteDao;

import java.util.List;

/**
 * @author wty
 * 扩展属性字段属性描述表
 */
public class FieldDescriptDALEx extends SqliteBaseDALEx {
	
    private static final long serialVersionUID = 1L;
    private static final String XWEXPANDFIELDID = "xwexpandfieldid";
    private static final String XWENTITYREGID = "xwentityregid";
    private static final String XWENTITYLABEL = "xwentitylabel";
    private static final String XWFIELDNAME = "xwfieldname";
    private static final String XWFIELDLABEL = "xwfieldlabel";
    private static final String XWFIELDLENGTH = "xwfieldlength";
    private static final String XWCONTROLTYPE = "xwcontroltype";
    private static final String XWOPTIONAL = "xwoptional";
    private static final String XWISREADONLY = "xwisreadonly";
    private static final String XWISALLOWEMPTY = "xwisallowempty";
    private static final String XWREGX = "xwregx";
    private static final String XWORDER = "xworder";
    private static final String XWSTATUS = "xwstatus";
    private static final String XWFIELDTYPE = "xwfieldtype";
    private static final String XWOPTISVISIBLE = "xwoptisvisible";
    private static final String XWVIEWISVISIBLE = "xwviewisvisible";
    private static final String XWLINENAME = "xwlinename";

    public static final int  FieldType_System = 0;//0 系统
    public static final int  FieldType_Default= 1;//1 默认
    public static final int  FieldType_Expand = 2;//2 自定义

    public static final int ContentType_Text = 1; // 文本
    public static final int ContentType_SingleSelect = 2; // 单选
    public static final int ContentType_MultiSelect = 3; // 多选
    public static final int ContentType_TextArea = 4; // 大文本
    public static final int ContentType_TextInteger = 5; // 整数
    public static final int ContentType_TextDecimal = 6; // 实数（保留三位小数）
    public static final int ContentType_Date = 7; // 日期
    public static final int ContentType_DateTime = 8; // 日期时间
    public static final int ContentType_Telephone = 9; // 手机号
    public static final int ContentType_Divider = 10; // 分组
    public static final int ContentType_Region = 11; // 行政区域
    public static final int ContentType_Product = 12; // 产品
    public static final int ContentType_SellStage = 13; // 销售阶段
    public static final int ContentType_CustScale = 14; // 客户规模
    public static final int ContentType_Industry = 15; // 行业
    public static final int ContentType_Address = 16; // 地址
    public static final int ContentType_Other = 17; // 其他
    public static final int ContentType_CustLevel = 18; // 客户等级

	@DatabaseField(primaryKey=true,Type= FieldType.VARCHAR)
    private String xwexpandfieldid;//字段id
	
	@DatabaseField(Type=FieldType.VARCHAR)
    private String xwentityregid;//实体对象id,expanedInfo表中的id
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwfieldname;//": "xwcustname",  //字段名称
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwfieldlabel;//": "客户名称",  //字段中文名
    
    @DatabaseField(Type=FieldType.INT)
    private int xwfieldlength;//": 0,   //字段长度
    
    @DatabaseField(Type=FieldType.INT)
    private int xwcontroltype;//": 1,   //控件类型
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwoptional;//": null,   //选项：下拉选择项，多个以|分割
    
    @DatabaseField(Type=FieldType.INT)
    private int xwoptisvisible;//": 1,  //新增/编辑/导入是否显示
    
    @DatabaseField(Type=FieldType.INT)
    private int xwviewisvisible;//": 1, //列表/查询/导出是否显示
    
    @DatabaseField(Type=FieldType.INT)
    private int xwisreadonly;//": 0,    //是否只读
    
    @DatabaseField(Type=FieldType.INT)
    private int xwisallowempty;//": 0,  //是否可为空
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwregx;//": null,       //验证规则
    
    @DatabaseField(Type=FieldType.INT)
    private int xworder;//": 1,         //排序
    
    @DatabaseField(Type=FieldType.INT)
    private int xwstatus;//": 1,        //状态
    
    @DatabaseField(Type=FieldType.INT)
    private int xwfieldtype;//": 0,     //属性类型   0系统 1默认  2 自定义
    
    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwlinename;//": "基本信息" //所属分组

    @DatabaseField(Type=FieldType.INT)
    private int xwsearchisvisible; //mobile筛选是否可见：0隐藏1可见

    @DatabaseField(Type=FieldType.VARCHAR)
    private String xwentitylabel;//实体名称 ：商机，客户

    public static FieldDescriptDALEx get(){
        return SqliteDao.getDao(FieldDescriptDALEx.class);
    }

    /**
     * 功能描述：保存单个对象
     **/
    public void save(final FieldDescriptDALEx dalex){
        if(dalex.getXwstatus() == 0){
            //该字段已被停用，直接忽略
            return;
        }
        operatorWithTransaction(new OnTransactionListener() {

            @Override
            public boolean onTransaction(UtionDB db) {
                ContentValues values = dalex.tranform2Values();
                db.save(TABLE_NAME, values);
                return true;
            }
        });
    }
    
    public FieldDescriptDALEx queryById(String id) {
        return findById(id);
    }

    public FieldDescriptDALEx queryByFieldname(String xwfieldname){
        return findOne("select * from " + TABLE_NAME + " where " + XWFIELDNAME + "=?", new String[]{xwfieldname});
    }

    /**
     * 根据expandedInfo中实体id,查询一组扩展属性的描述
     * @param entityregid
     * @return
     */
    public List<FieldDescriptDALEx> queryByEntityregid(String entityregid) {
    	return findList("select a.*,b." + ExpandinfoDALEx.NAME + " from " + TABLE_NAME + " a "
                + " left join " + ExpandinfoDALEx.get().getSqliteTablename() + " b "
                + " on  a." + XWENTITYREGID + " = b." + ExpandinfoDALEx.KEY
                + " where " + XWENTITYREGID + "=? order by " + XWORDER, new String[]{entityregid});
    }
    
    /**
     * 根据expandedInfo中实体名,查询一组扩展属性的描述
     * @param name
     * @return
     */
    public List<FieldDescriptDALEx> queryByEntityregname(String name) {
    	return findList("select a.* from " + TABLE_NAME + " a "
                + " left join " + ExpandinfoDALEx.get().getSqliteTablename() + " b "
                + " on  a." + XWENTITYREGID + " = b." + ExpandinfoDALEx.KEY
                + " where b." + ExpandinfoDALEx.NAME + "=? order by " + XWORDER, new String[]{name});
    }
    
    /**
     * 根据expandedInfo中实体id,查询一组扩展属性的描述
     * @param entityregname
     * @return
     */
    public List<FieldDescriptDALEx> queryByEntityregFieldName(String entityregname) {
    	
    	return findList("select a.*,b."+ExpandinfoDALEx.NAME +" from "+ TABLE_NAME +" a "
                +" left join "+ExpandinfoDALEx.get().getSqliteTablename() +" b "
                +" on  a."+XWENTITYREGID+" = b."+ExpandinfoDALEx.KEY
                +" where b."+ ExpandinfoDALEx.NAME+"=? order by "+XWORDER,new String[] {entityregname});
    }

    /**
     * 保存一系列对象
     **/
    public void save(final FieldDescriptDALEx[] list,final String entitylabel){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (FieldDescriptDALEx dalex : list) {

                    if(dalex.getXwstatus() == 0){
                        //该字段已被停用，直接忽略
                        continue;
                    }

                    ContentValues values = dalex.tranform2Values();
                    values.put(XWENTITYLABEL,entitylabel);
                    if(isExist(XWEXPANDFIELDID,dalex.getXwexpandfieldid())){
                        db.update(TABLE_NAME, values, XWEXPANDFIELDID+"=?", new String[]{dalex.getXwexpandfieldid()});
                    }else{
                        db.save(TABLE_NAME, values);
                    }
                }
                return true;
            }
        });
    }

    /**
     * 保存一系列对象
     **/
    public void save(final List<FieldDescriptDALEx> list,final String entitylabel){
        operatorWithTransaction(new OnTransactionListener() {
            @Override
            public boolean onTransaction(UtionDB db) {
                for (FieldDescriptDALEx dalex : list) {
                    if(dalex.getXwstatus() == 0){
                        //该字段已被停用，直接忽略
                        continue;
                    }

                    ContentValues values = dalex.tranform2Values();
                    values.put(XWENTITYLABEL,entitylabel);
                    if(isExist(XWEXPANDFIELDID,dalex.getXwexpandfieldid())){
                        db.update(TABLE_NAME, values, XWEXPANDFIELDID+"=?", new String[]{dalex.getXwexpandfieldid()});
                    }else{
                        db.save(TABLE_NAME, values);
                    }
                }
                return true;
            }
        });
    }
    
    public String getXwexpandfieldid() {
		return xwexpandfieldid;
	}

	public void setXwexpandfieldid(String xwexpandfieldid) {
		this.xwexpandfieldid = xwexpandfieldid;
	}

	public String getXwentityregid() {
		return xwentityregid;
	}

	public void setXwentityregid(String xwentityregid) {
		this.xwentityregid = xwentityregid;
	}

	public String getXwfieldname() {
		return xwfieldname;
	}

	public void setXwfieldname(String xwfieldname) {
		this.xwfieldname = xwfieldname;
	}

	public String getXwfieldlabel() {
		return xwfieldlabel;
	}

	public void setXwfieldlabel(String xwfieldlabel) {
		this.xwfieldlabel = xwfieldlabel;
	}

	public int getXwfieldlength() {
		return xwfieldlength;
	}

	public void setXwfieldlength(int xwfieldlength) {
		this.xwfieldlength = xwfieldlength;
	}

	public int getXwcontroltype() {
		return xwcontroltype;
	}

	public void setXwcontroltype(int xwcontroltype) {
		this.xwcontroltype = xwcontroltype;
	}

	public String getXwoptional() {
		return xwoptional;
	}

	public void setXwoptional(String xwoptional) {
		this.xwoptional = xwoptional;
	}

	public int getXwoptisvisible() {
		return xwoptisvisible;
	}

	public void setXwoptisvisible(int xwoptisvisible) {
		this.xwoptisvisible = xwoptisvisible;
	}

	public int getXwviewisvisible() {
		return xwviewisvisible;
	}

	public void setXwviewisvisible(int xwviewisvisible) {
		this.xwviewisvisible = xwviewisvisible;
	}

	public int getXwisreadonly() {
		return xwisreadonly;
	}

	public void setXwisreadonly(int xwisreadonly) {
		this.xwisreadonly = xwisreadonly;
	}

	public int getXwisallowempty() {
		return xwisallowempty;
	}

	public void setXwisallowempty(int xwisallowempty) {
		this.xwisallowempty = xwisallowempty;
	}

	public String getXwregx() {
		return xwregx;
	}

	public void setXwregx(String xwregx) {
		this.xwregx = xwregx;
	}

	public int getXworder() {
		return xworder;
	}

	public void setXworder(int xworder) {
		this.xworder = xworder;
	}

	public int getXwstatus() {
		return xwstatus;
	}

	public void setXwstatus(int xwstatus) {
		this.xwstatus = xwstatus;
	}

	public int getXwfieldtype() {
		return xwfieldtype;
	}

	public void setXwfieldtype(int xwfieldtype) {
		this.xwfieldtype = xwfieldtype;
	}

	public String getXwlinename() {
		return xwlinename;
	}

	public void setXwlinename(String xwlinename) {
		this.xwlinename = xwlinename;
	}
	
	/**
     * 根据id删除
     * @param key
     */
    public void deleteByEntityregid(String key){
    	UtionDB db = null;
    	try {
			db = getDB();
			db.delete(TABLE_NAME, XWENTITYREGID +"=?", new String[]{key});
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public int getXwsearchisvisible() {
        return xwsearchisvisible;
    }

    public void setXwsearchisvisible(int xwsearchisvisible) {
        this.xwsearchisvisible = xwsearchisvisible;
    }

    public String getXwentitylabel() {
        return xwentitylabel;
    }

    public void setXwentitylabel(String xwentitylabel) {
        this.xwentitylabel = xwentitylabel;
    }

}