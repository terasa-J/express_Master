package com.hankkin.compustrading.mvp_del.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_del.IDelView;
import com.hankkin.compustrading.mvp_del.model.DelModelImp;
import com.hankkin.compustrading.mvp_del.model.OnCheckListener;

import java.util.List;


public class DelPresenter {
    private DelModelImp delModelImp;
    private IDelView iDelView;
    private Handler handler = new Handler();

    //传入对应的view
    public DelPresenter(IDelView iDelView) {
        delModelImp = new DelModelImp();
        this.iDelView = iDelView;
    }

    //删除发布的快递
    public void delProduct(int id,String del){
        delModelImp.delMyProduct(del,id, iDelView.getUrl(), new OnCheckListener() {
            @Override
            public void delSuccess(String message) {
                iDelView.showSuccess(message);
            }

            @Override
            public void delFail() {
                iDelView.showFailedError();
            }
        });
    }
}
