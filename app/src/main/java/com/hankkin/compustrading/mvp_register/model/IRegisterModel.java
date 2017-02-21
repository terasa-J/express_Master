package com.hankkin.compustrading.mvp_register.model;


/**
 * 注册业务逻辑处理
 */
public interface IRegisterModel {
    void register(String url, String username, String password, OnCheckListener userOnCheckListener);
}
