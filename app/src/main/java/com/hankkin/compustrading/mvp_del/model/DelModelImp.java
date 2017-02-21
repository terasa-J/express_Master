package com.hankkin.compustrading.mvp_del.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 *快递列表业务逻辑
 */
public class DelModelImp implements IDelModel {

/*删除发布的快递 */
    @Override
    public void delMyProduct(String del,int id, String url, final OnCheckListener onCheckListener) {
        Map<String,String> delMap=new HashMap<>();
        delMap.put("productid",id+"");
        delMap.put("del",del);

        OkHttp.postAsync(url, delMap, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                onCheckListener.delFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson =new Gson();
                String message =gson.fromJson(result,String.class);
                onCheckListener.delSuccess(message);

            }
        });

    }
}
