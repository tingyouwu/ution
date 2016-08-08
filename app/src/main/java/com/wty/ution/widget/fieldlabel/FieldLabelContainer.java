package com.wty.ution.widget.fieldlabel;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import com.wty.ution.data.annotation.SqliteBaseDALEx;
import com.wty.ution.data.dalex.FieldDescriptDALEx;
import com.wty.ution.data.exfieldvalue.IExfieldDALEx;
import com.wty.ution.widget.fieldlabel.factory.AbstractFactory;
import com.wty.ution.widget.fieldlabel.factory.ExpandContentFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wty
 * */
public class FieldLabelContainer extends LinearLayout {

    /**
     *  "xwexpandfieldid": "2f9dd0ec-97ca-6346-9b5b-2f6a2a8ad38b",
        "xwentityregid": "1f5ad321-753e-a7e7-812c-b4e20d50e586",  //对应的实体id
        "xwfieldname": "xwcontactname",//字段名称
        "xwfieldlabel": "姓名",//字段标示
        "xwfieldlength": 10,//字段长度
        "xwcontroltype": 1,//字段控制类型
        "xwoptional": null,//字段选项
        "xwoptisvisible": 1,//新增或者编辑是否显示该字段
        "xwviewisvisible": 1,//详情页面是否显示该字段
        "xwsearchisvisible": 0,//搜索页面是否显示该字段
        "xwisreadonly": 0,//是否只读
        "xwisallowempty": 0,//允许空
        "xwregx": null,//正则表达式
        "xworder": 1,//排序排位
        "xwstatus": 1,//是否禁用
        "xwfieldtype": 0,//字段类型  系统字段  自定义字段
        "xwlinename": "基本信息"
     **/

    /** 默认工厂 */
    AbstractFactory defaultFactory = new ExpandContentFactory();

    private Map<String,IContent> let_fix = new HashMap<String,IContent>();
    private Map<String,IContent> let_expand = new HashMap<String,IContent>();
    private List<IContent> let_all = new ArrayList<IContent>();

    private Mode mode = Mode.ADD;

    public FieldLabelContainer(Context context) {
        super(context);
        init();
    }

    public FieldLabelContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(LinearLayout.VERTICAL);
        removeAllViews();
    }

    /**
     * 根据描述生成控件
     * @param descripts
     */
    public void addLabel(List<FieldDescriptDALEx> descripts){
        addLabel(descripts, defaultFactory, true);
    }

    public void addLabel(List<FieldDescriptDALEx> descripts,boolean addDivide){
        addLabel(descripts,defaultFactory, addDivide);
    }

    public void addLabel(List<FieldDescriptDALEx> descripts,AbstractFactory factory){
        addLabel(descripts, factory, true);
    }

    public void addLabel(List<FieldDescriptDALEx> descripts,AbstractFactory factory,boolean addDivide){
        for(int i=0;i<descripts.size();i++){
            FieldDescriptDALEx descript = descripts.get(i);
            boolean addViewAble = true;
            boolean readOnly = false;
            if(this.mode == Mode.ADD){
                readOnly  = descript.getXwisreadonly() == 1;
                addViewAble = descript.getXwoptisvisible()==1;
            }else if(mode == Mode.INFO){
                readOnly = true;
                addViewAble = descript.getXwviewisvisible()==1;
            }else if(mode == Mode.EDIT){
                readOnly  = descript.getXwisreadonly() == 1;
                addViewAble = descript.getXwoptisvisible()==1;
            }
            if(descript.getXwstatus() == 0){
                //停用
                continue ;
            }

            if(addViewAble){

                IContent content = factory.initByType(getContext(), descript);
                if(content==null)continue;
                content.setFieldReadOnly(readOnly);
                if(!(content instanceof View)){
                    System.out.println("该控件不是view类型，不能添加进去");
                    continue ;
                }

                if(let_all.size()!=0){
                    IContent icontent = let_all.get(let_all.size()-1);
                    FieldDescriptDALEx lastDescript = icontent.getFieldDescript();
                    String linename = descript.getXwlinename();
                    if(lastDescript!=null && !lastDescript.getXwlinename().equals(linename)){
                        if(addDivide) {
                            ContentDivide contentDivide = new ContentDivide(getContext());
                            contentDivide.tv.setText(linename);
                            addView(contentDivide);
                        }
                    }
                }else{
                    //第一行
                    if(!TextUtils.isEmpty(descript.getXwlinename())){
                        //第一行，加上分组名
                        ContentDivide contentDivide = new ContentDivide(getContext());
                        contentDivide.tv.setText(descript.getXwlinename());
                        addView(contentDivide);
                    }
                }

                this.addView((View)content);
                let_all.add(content);

                if(descript.getXwfieldtype() == FieldDescriptDALEx.FieldType_Expand){
                    let_expand.put(descript.getXwfieldname(), content);
                }else{
                    let_fix.put(descript.getXwfieldname(), content);
                }

            }

        }
    }

    public List<IContent> getAllLabel(){
        return let_all;
    }

    public IContent getLabel(String name){
        IContent let = let_fix.get(name);
        if(let==null){
            let = let_expand.get(name);
        }

        return let;
    }



    /**
     * 给一组label设置值
     * @param fieldValue
     */
    public void setFieldValue(Map<String,String> fieldValue){
        if(fieldValue==null)return;

        for (Map.Entry<String, String> entry : fieldValue.entrySet()) {
            setFieldValue(entry.getKey(),entry.getValue());
        }
    }

    /**
     * 给一组label设置值
     * @param object
     */
    public <T extends SqliteBaseDALEx> void setFieldValue(T object){
        if(object == null)return;
        setFieldValue(object.getAnnotationFieldValue());
        if(object instanceof IExfieldDALEx){
            setFieldValue(((IExfieldDALEx) object).getExpandfields());
        }
    }

    /**
     * 给里面Label设置值
     * @param key
     * @param value
     */
    public void setFieldValue(String key,String value){
        IContent let = getLabel(key);
        if(let!=null){
            let.setFieldValue(value);
        }
    }


    /**
     * 根据字段名获取值
     * @param name
     * @return
     */
    public String getFieldValue(String name){
        IContent let = getLabel(name);
        if(let!=null){
            return let.getFieldValue();
        }else{
            return "";
        }
    }

    /**
     * 获取扩展字段的所有值
     * @return
     */
    public Map<String,String> getExpandFieldValue(){
        Map<String,String> result = new HashMap<String,String>();
        for (Map.Entry<String, IContent> entry : let_expand.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue().getFieldValue();
            result.put(name, value);
        }
        return result;
    }

    /**
     * 获取固定字段的所有值
     * @return
     */
    public Map<String,String> getFixFieldValue(){
        Map<String,String> result = new HashMap<String,String>();
        for (Map.Entry<String, IContent> entry : let_fix.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue().getFieldValue();
            result.put(name, value);
        }
        return result;
    }

    /**
     * 检验容器内所有控件的值
     * @return
     */
    public String validate(){
        for (IContent let : let_all) {
            String validate = let.fieldValidate();
            if(!TextUtils.isEmpty(validate)){
                return validate;
            }
        }
        return "";
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode{
        ADD,INFO,EDIT
    }

    public void clearValue(){
        for(IContent content :let_all){
            content.clearFieldValue();
        }
    }

    public void clear(){
        let_fix.clear();
        let_expand.clear();
        let_all.clear();

        let_fix = null;
        let_expand = null;
        let_all = null;

    }
}