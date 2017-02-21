package com.hankkin.compustrading.mvp_new;

import com.hankkin.compustrading.model.Product;

import java.util.List;


public interface INewView {

    String getUrl();


    void showAddSuccess(int result);

    void showFailedError();
}
