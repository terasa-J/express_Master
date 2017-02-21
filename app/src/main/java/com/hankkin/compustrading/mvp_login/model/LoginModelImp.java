package com.hankkin.compustrading.mvp_login.model;

import android.util.Log;

import com.google.gson.Gson;
import com.hankkin.compustrading.Utils.OkHttp;
import com.hankkin.compustrading.model.Person;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录业务逻辑的具体实现
 */
public class LoginModelImp implements ILoginModel {

    /**
     * post请求后台
     *
     * @param username
     * @param password
     */

    @Override
    public void login(String url, final String username, final String password, final OnCheckListener userOnCheckListener)
      {

          Map<String, String> loginMap =new HashMap<String,String>();
          loginMap.put("tel",username);
          loginMap.put("password",password);

          //调用工具类
          OkHttp.postAsync(url, loginMap, new OkHttp.DataCallBack() {
              @Override
              public void requestFailure(okhttp3.Request request, IOException e) {
                  userOnCheckListener.loginFail();
                  Log.e("结果", "error" );

              }

              @Override
              public void requestSuccess(String result) throws Exception {
                  Log.e("登录结果----------", "if:"+result );

                  /*
	                reult放的是你的解析出啦的json数据
 	                */
                  Gson gson = new Gson();

                  //Person person=new Person(username,password);


                 Person person = gson.fromJson(result, Person.class);
                  Log.e("登录结果=========", person.toString() );
                  userOnCheckListener.loginSuccess(person);

              }
          });

    }
}
