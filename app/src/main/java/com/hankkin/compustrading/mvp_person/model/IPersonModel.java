package com.hankkin.compustrading.mvp_person.model;


import com.hankkin.compustrading.model.Person;

/**
 * 更新头像
 */
public interface IPersonModel {
    void updateIconUrl(Person person, String url, OnCheckListener addOnCheckListener);
    void updatePerson(Person person, String url, OnCheckListener onCheckListener);
}
