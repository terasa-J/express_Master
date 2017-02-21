package com.hankkin.compustrading.mvp_detail.model;


/**
 * 登录业务逻辑处理
 */
public interface IDetailModel {
    void queryCategory (String url,int CategoryId, OnCheckListener queryOnCheckListener);

    void likeProduct(String url,int productId,String tel,LikeListener likeListener);
}
