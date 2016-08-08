package com.wty.ution.data.exfieldvalue;

import java.util.Map;

/**
 * 扩展字段接口
 */
public interface IExfieldDALEx {
    public void setExpandfields(Map<String, String> expandfields);
    public Map<String,String> getExpandfields();

}
