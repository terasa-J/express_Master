package com.hankkin.compustrading.mvp_del.model;


/**
 * 登录业务逻辑处理
 */
public interface IDelModel {
    void delMyProduct(String del,int id,String url, OnCheckListener onCheckListener);

}
