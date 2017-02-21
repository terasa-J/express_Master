package com.hankkin.compustrading.mvp_login.model;

import com.hankkin.compustrading.model.Person;


/**
 * 登录是否成功，页面的跳转
 */
public interface OnCheckListener {
    void loginSuccess(Person person);
    void loginFail();
}
