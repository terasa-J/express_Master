package com.hankkin.compustrading.mvp_person.model;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


/**
 * 更新个人信息
 */
public class PersonModelImp implements IPersonModel {
    /**
     * post请求后台,修改头像
     */
    @Override
    public void updateIconUrl(Person person, String url, final OnCheckListener addOnCheckListener) {
        Map<String,String> iconUrlMap = new HashMap<>();
        iconUrlMap.put("userIconUrl",person.getUserIconUrl());
        iconUrlMap.put("tel",person.getTel());
        iconUrlMap.put("password",person.getPassword());

        OkHttp.postAsync(url, iconUrlMap, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                addOnCheckListener.updateFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson=new Gson();
                Person person=gson.fromJson(result,Person.class);
                addOnCheckListener.updateSuccess(person);


            }
        });

    }

    @Override
    public void updatePerson(Person person, String url, final OnCheckListener onCheckListener) {
        Map<String,String> infoMap = new HashMap<>();
        infoMap.put("tel",person.getTel());
        infoMap.put("password",person.getPassword());
        infoMap.put("nickname",person.getNickname());
        infoMap.put("sex",person.getSex());
        infoMap.put("birth",person.getBirth());

        OkHttp.postAsync(url, infoMap, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                onCheckListener.updateFail();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson=new Gson();
                Person newPerson=gson.fromJson(result,Person.class);
                onCheckListener.updateSuccess(newPerson);

            }
        });
    }
}
