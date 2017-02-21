package com.hankkin.compustrading.mvp_login.presenter;

import android.os.Handler;

import com.hankkin.compustrading.mvp_login.ILoginView;
import com.hankkin.compustrading.mvp_login.model.OnCheckListener;
import com.hankkin.compustrading.mvp_login.model.LoginModelImp;
import com.hankkin.compustrading.model.Person;


public class LoginPresenter {
    private LoginModelImp loginModelImp;
    private ILoginView iLoginView;
    private Handler handler=new Handler();
    //传入对应的view
    public LoginPresenter(ILoginView iLoginView){
        loginModelImp=new LoginModelImp();
        this.iLoginView = iLoginView;
    }
    public void login() {
        loginModelImp.login(iLoginView.getUrl(), iLoginView.getUserName(), iLoginView.getPassword(), new OnCheckListener() {
            @Override
            public void loginSuccess(final Person person) {
                //网络请求都要异步处理
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //页面跳转
                        iLoginView.toMainActivity(person);
                    }
                });

            }

            @Override
            public void loginFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //显示失败信息
                        iLoginView.showFailedError();
                    }
                });

            }
        });
    }


}
