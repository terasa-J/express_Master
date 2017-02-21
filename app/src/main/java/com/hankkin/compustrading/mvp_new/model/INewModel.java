package com.hankkin.compustrading.mvp_new.model;


import com.hankkin.compustrading.model.Product;

/**
 * 登录业务逻辑处理
 */
public interface INewModel {
    void addProduct(Product product,String url, OnCheckListener addOnCheckListener);
}
