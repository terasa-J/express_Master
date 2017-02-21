package com.hankkin.compustrading.mvp_register.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.mvp_register.IRegisterView;
import com.hankkin.compustrading.mvp_register.model.OnCheckListener;
import com.hankkin.compustrading.mvp_register.model.RegisterModelImp;



public class RegisterPresenter {
    private RegisterModelImp registerModelImp;
    private IRegisterView iRegisterView;
    private Handler handler=new Handler();
    //传入对应的view
    public RegisterPresenter(IRegisterView iRegisterView){
        registerModelImp=new RegisterModelImp();
        this.iRegisterView=iRegisterView;
    }
    public void register() {
        registerModelImp.register(iRegisterView.getUrl(), iRegisterView.getUserName(), iRegisterView.getPassword(), new OnCheckListener() {
            @Override
            public void registerSuccess(final Person person) {
                //网络请求都要异步处理
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //页面跳转
                        iRegisterView.toMainActivity(person);
                    }
                });

            }

            @Override
            public void registerFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //显示失败信息
                        iRegisterView.showFailedError();
                    }
                });

            }
        });
    }


}
