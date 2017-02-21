package com.hankkin.compustrading.mvp_new.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_new.INewView;
import com.hankkin.compustrading.mvp_new.model.NewModelImp;
import com.hankkin.compustrading.mvp_new.model.OnCheckListener;

import java.util.List;


public class NewPresenter {
    private NewModelImp newModelImp;
    private INewView iNewView;
    private Handler handler=new Handler();
    //传入对应的view
    public NewPresenter(INewView iNewView){
        newModelImp=new NewModelImp();
        this.iNewView = iNewView;
    }
    public void addProduct(Product product) {
        newModelImp.addProduct(product, iNewView.getUrl(), new OnCheckListener() {
            @Override
            public void addSuccess(final int result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //显示添加成功
                        iNewView.showAddSuccess(result);
                    }
                });
            }

            @Override
            public void addFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //显示添加失败信息
                        iNewView.showFailedError();
                    }
                });

            }
        });


    }

}
