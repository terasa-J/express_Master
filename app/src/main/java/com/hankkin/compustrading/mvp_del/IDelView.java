package com.hankkin.compustrading.mvp_del;

import com.hankkin.compustrading.model.Product;

import java.util.List;


public interface IDelView {

    String getUrl();
    void showSuccess(String message);
    void showFailedError();
}
