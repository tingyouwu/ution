package com.wty.ution.data;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {
    private String name;
    private String address;
    private int age;
    private Integer num;

    @Override
    public void setTableName(String tableName) {
        super.setTableName(tableName);
    }
}