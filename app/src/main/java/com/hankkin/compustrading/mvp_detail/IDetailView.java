package com.hankkin.compustrading.mvp_detail;

import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Product;

import java.util.List;


public interface IDetailView {

    String getUrl();
    int getCategoryId();
    void showSuccess(Category category);
    void showFailedError();

    String getLikeUrl();
    void likeSuccess(String message);
    void likeFail();
}
