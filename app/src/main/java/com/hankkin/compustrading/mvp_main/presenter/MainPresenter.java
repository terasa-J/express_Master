package com.hankkin.compustrading.mvp_main.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_main.IMainView;
import com.hankkin.compustrading.mvp_main.model.MainModelImp;
import com.hankkin.compustrading.mvp_main.model.OnCheckListener;

import java.util.List;


public class MainPresenter {
    private MainModelImp mainModelImp;
    private IMainView iMainView;
    private Handler handler=new Handler();
    //传入对应的view
    public MainPresenter(IMainView iMainView){
        mainModelImp=new MainModelImp();
        this.iMainView = iMainView;
    }
    public void queryProducts() {
        mainModelImp.queryProducts(iMainView.getUrl(), new OnCheckListener() {
            @Override
            public void querySuccess(final List<Product> productList) {
                //网络请求都要异步处理
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //页面跳转
                        iMainView.showProductList(productList);
                    }
                });

            }

            @Override
            public void queryFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //显示查询失败信息
                        iMainView.showFailedError();
                    }
                });

            }
        });

    }
//查询个人快递
    public void queryMyProducts(String tel){
        mainModelImp.queryMyProducts(tel, iMainView.getUrl(), new OnCheckListener() {
            @Override
            public void querySuccess(List<Product> productList) {
                iMainView.showProductList(productList);
            }

            @Override
            public void queryFail() {
                iMainView.showFailedError();
            }
        });
    }
}
