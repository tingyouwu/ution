package com.wty.ution.base;


import java.util.List;

/**
 * 功能描述：Bmob查询包装类
 * @author wty
 **/
public interface ImpQuery<T> {
    void onSuccess(List<T> var1);
    void onError(int var1, String var2);
}