package com.hankkin.compustrading.mvp_detail.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 * 获得分类
 */
public class DetailModelImp implements IDetailModel {

    /**
     * post请求后台
     */
    @Override
    public void queryCategory(String url, int CategoryId, final OnCheckListener queryOnCheckListener) {
        Map<String,String> categoryMap = new HashMap<>();
        //服务器端要转换为Integer类型
        categoryMap.put("cid",CategoryId+"");

        OkHttp.postAsync(url, categoryMap, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                queryOnCheckListener.queryFail();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson=new Gson();
                Category category=gson.fromJson(result,Category.class);
                queryOnCheckListener.querySuccess(category);

            }
        });
    }
//收藏
    @Override
    public void likeProduct(String url, int productId, String tel, final LikeListener likeListener) {
        Map<String,String> likeMap=new HashMap<>();
        likeMap.put("productId",productId+"");
        likeMap.put("tel",tel);

        OkHttp.postAsync(url, likeMap, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                likeListener.likeFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson=new Gson();
                String message=gson.fromJson(result,String.class);
                likeListener.likeSuccess(message);

            }
        });

    }
}
