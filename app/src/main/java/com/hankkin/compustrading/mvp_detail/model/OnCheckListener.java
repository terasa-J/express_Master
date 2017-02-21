package com.hankkin.compustrading.mvp_detail.model;


import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Product;

import java.util.List;



public interface OnCheckListener {
    void querySuccess(Category category);
    void queryFail();


}
