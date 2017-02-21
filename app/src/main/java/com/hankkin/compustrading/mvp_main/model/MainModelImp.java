package com.hankkin.compustrading.mvp_main.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 * 获得快递列表业务逻辑
 */
public class MainModelImp implements IMainModel {

    /**
     * get请求后台
     */

    @Override
    public void queryProducts(String url, final OnCheckListener queryOnCheckListener) {
        OkHttp.getAsync(url, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("queryProduct----", "error");
                queryOnCheckListener.queryFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Log.e("queryProduct----", "success"+result);

                Gson gson=new Gson();
                //返回列表书写方式不一样
                List<Product> productList= gson.fromJson(result, new TypeToken<List<Product>>() {}.getType());

                queryOnCheckListener.querySuccess(productList);
            }
        });

    }

    //查询个人发布快递列表
    @Override
    public void queryMyProducts(String tel,String url, final OnCheckListener onCheckListener) {
        Map<String,String> myProducts =new HashMap<>();
        myProducts.put("tel",tel);
        OkHttp.postAsync(url, myProducts, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                onCheckListener.queryFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson=new Gson();
                List<Product> productList=gson.fromJson(result, new TypeToken<List<Product>>() {
                }.getType());
                onCheckListener.querySuccess(productList);

            }
        });
    }
}
