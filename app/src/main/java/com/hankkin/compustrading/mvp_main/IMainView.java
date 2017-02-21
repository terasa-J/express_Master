package com.hankkin.compustrading.mvp_main;

import com.hankkin.compustrading.model.Product;

import java.util.List;


public interface IMainView {

    String getUrl();
    void showProductList(List<Product> products);
    void showFailedError();
}
