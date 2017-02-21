package com.hankkin.compustrading.mvp_detail.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_detail.IDetailView;
import com.hankkin.compustrading.mvp_detail.model.DetailModelImp;
import com.hankkin.compustrading.mvp_detail.model.LikeListener;
import com.hankkin.compustrading.mvp_detail.model.OnCheckListener;

import java.util.List;


public class DetailPresenter {
    private DetailModelImp detailModelImp;
    private IDetailView iDetailView;
    private Handler handler=new Handler();
    //传入对应的view
    public DetailPresenter(IDetailView iDetailView){
        detailModelImp=new DetailModelImp();
        this.iDetailView = iDetailView;
    }
//查询分类
    public void queryCategory(){
        detailModelImp.queryCategory(iDetailView.getUrl(), iDetailView.getCategoryId(), new OnCheckListener() {
            @Override
            public void querySuccess(final Category category) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.showSuccess(category);
                    }
                });
            }

            @Override
            public void queryFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.showFailedError();
                    }
                });

            }
        });

    }
    //个人收藏
    public void likeProduct(int productId,String tel){
        detailModelImp.likeProduct(iDetailView.getLikeUrl(), productId, tel, new LikeListener() {
            @Override
            public void likeSuccess(final String message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.likeSuccess(message);
                    }
                });
            }

            @Override
            public void likeFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDetailView.likeFail();

                    }
                });

            }
        });

    }
}
