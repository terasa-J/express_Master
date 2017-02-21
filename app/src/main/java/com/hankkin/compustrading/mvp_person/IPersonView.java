package com.hankkin.compustrading.mvp_person;


import com.hankkin.compustrading.model.Person;

public interface IPersonView {

    String getUrl();

    void showSuccess(Person person);

    void showFailedError();
}
