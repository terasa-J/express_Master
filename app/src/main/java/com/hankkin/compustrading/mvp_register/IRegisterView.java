package com.hankkin.compustrading.mvp_register;

import com.hankkin.compustrading.model.Person;


public interface IRegisterView {
    String getUserName();
    String getPassword();
    String getUrl();
    void toMainActivity(Person person);
    void showFailedError();
}
