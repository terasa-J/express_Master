package com.hankkin.compustrading.mvp_main.model;


import com.hankkin.compustrading.model.Product;

import java.util.List;


/**
 * 登录是否成功，页面的跳转
 */
public interface OnCheckListener {
    void querySuccess(List<Product> productList);
    void queryFail();
}
