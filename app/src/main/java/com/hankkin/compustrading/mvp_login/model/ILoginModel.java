package com.hankkin.compustrading.mvp_login.model;


/**
 * 登录业务逻辑处理
 */
public interface ILoginModel {
    void login(String url, String username, String password, OnCheckListener userOnCheckListener);
}
