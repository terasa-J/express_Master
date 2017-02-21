package com.hankkin.compustrading.mvp_login;

import com.hankkin.compustrading.model.Person;


public interface ILoginView {
    String getUserName();
    String getPassword();
    String getUrl();
    void toMainActivity(Person person);
    void showFailedError();
}
