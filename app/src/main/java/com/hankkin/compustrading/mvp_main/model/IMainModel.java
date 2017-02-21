package com.hankkin.compustrading.mvp_main.model;


/**
 * 登录业务逻辑处理
 */
public interface IMainModel {
    void queryProducts(String url, OnCheckListener queryOnCheckListener);
    void queryMyProducts(String tel,String url, OnCheckListener queryOnCheckListener);
}
