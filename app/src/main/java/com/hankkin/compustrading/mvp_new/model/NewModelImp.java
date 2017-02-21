package com.hankkin.compustrading.mvp_new.model;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 添加快递 业务逻辑
 */
public class NewModelImp implements INewModel {

    /**
     * post请求后台
     */

    @Override
    public void addProduct(Product product, String url, final OnCheckListener addOnCheckListener) {

        Map<String,String> addProduct =new HashMap<>();

        addProduct.put("content",product.getContent());
        addProduct.put("cid",(product.getCid()+1)+"");
        addProduct.put("nickname",product.getNickname());
        addProduct.put("price",product.getPrice());
        //获得上传的图片
        addProduct.put("productUrl",Uri.decode(product.getProductUrl()));

        addProduct.put("tel",product.getTel());
        addProduct.put("phone",product.getPhone());
        addProduct.put("userIconUrl",product.getUserIconUrl());
        addProduct.put("createat",product.getCreateat());
        addProduct.put("school", product.getSchool());

        OkHttp.postAsync(url, addProduct, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("addProduct----", "error");
                addOnCheckListener.addFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Log.e("添加结果++++++",result);
                Gson gson =new Gson();

                int isAdd=gson.fromJson(result , Integer.class);

                Log.e("添加结果----------", isAdd+"" );

                addOnCheckListener.addSuccess(isAdd);

            }
        });


    }
}
