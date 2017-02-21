package com.hankkin.compustrading.mvp_register.model;

import android.util.Log;

import com.google.gson.Gson;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Person;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


/**
 * 登录业务逻辑的具体实现
 */
public class RegisterModelImp implements IRegisterModel {

    /**
     * post请求后台
     *
     * @param username
     * @param password
     */

    @Override
    public void register(String url, final String username, final String password, final OnCheckListener userOnCheckListener)
      {
          Map<String, String> registerMap =new HashMap<String,String>();
          registerMap.put("tel",username);
          registerMap.put("password",password);
          registerMap.put("nickname","新用户");
          registerMap.put("sex","男性");
          registerMap.put("birth","2017-01-01");

          OkHttp.postAsync(url, registerMap, new OkHttp.DataCallBack() {
              @Override
              public void requestFailure(Request request, IOException e) {
                  userOnCheckListener.registerFail();
                  Log.e("注册失败-------","error");

              }

              @Override
              public void requestSuccess(String result) throws Exception {
                  Log.e("注册结果----------", "if:"+result );

                  /*
	               result放的是你的解析出啦的json数据
 	                */
                  Gson gson = new Gson();

                  int isRegister = gson.fromJson(result, Integer.class);

                  Log.e("注册结果----------", isRegister+"" );

                  Person person=new Person("测试","");
                  userOnCheckListener.registerSuccess(person);

              }
          });

    }
}
