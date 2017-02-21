package com.hankkin.compustrading.mvp_person.model;


import com.hankkin.compustrading.model.Person;

/**
 * 更新成功
 */
public interface OnCheckListener {
    void updateSuccess(Person person);
    void updateFail();
}
