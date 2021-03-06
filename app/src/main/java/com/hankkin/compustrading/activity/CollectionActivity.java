package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.adapter.MyProductAdapter;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_main.IMainView;
import com.hankkin.compustrading.mvp_main.presenter.MainPresenter;
import com.hankkin.compustrading.view.RefreshLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CollectionActivity extends  BaseActivity implements IMainView,SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{



    @Bind(R.id.pager)
    ListView pager;
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.tv_head)
    TextView tvHead;
    private RefreshLayout swipeRefreshLayout;



    public static CollectionActivity instance;
    private Handler handler;
    private Person person;
    private String tel;

    //连接服务器
    private String mainUrl=BaseUrl.baseUrl+"likeList";
    private MainPresenter mainPresenter = new MainPresenter(CollectionActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_my_product);
        ButterKnife.bind(this);   //注解的方法初始化，要引入库
        init();
        //取消加载
        dimissDialog();
    }

    /**
     * 初始化数据
     *
     */
    private void init() {
        tvHead.setText("我的收藏");

        /**
         * 更新按钮点击事件
         */
        swipeRefreshLayout = (RefreshLayout) this.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setChildView(pager);
        //设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnLoadListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        //返回
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //判断是否登录了
         person = (Person) getIntent().getSerializableExtra("loginPerson");
         tel =person.getTel();

        //加载数据   连接数据库服务器
        showLoadingDialog();

        mainPresenter.queryMyProducts(tel);



    }


//下滑加载
    @Override
    public void onLoad() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新连接数据库
                mainPresenter.queryMyProducts(tel);
                swipeRefreshLayout.setLoading(false);
            }
        }, 2000);

    }
//上拉刷新
    @Override
    public void onRefresh() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainPresenter.queryMyProducts(tel);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    @Override
    public String getUrl() {
        return mainUrl;
    }

    @Override
    public void showProductList(List<Product> products) {
        if(products.size()==0){
            HankkinUtils.showLToast(this,"请先收藏");
            Intent intent=new Intent(this,MainShowActivity.class);
            startActivity(intent);
        }else {

            final MyProductAdapter myProductAdapter = new MyProductAdapter("我的收藏",this, products);

            pager.setAdapter(myProductAdapter);

            swipeRefreshLayout.setLoading(false);
            swipeRefreshLayout.setRefreshing(false);
            //加载完毕，停止刷新
            dimissDialog();
        }

    }

    @Override
    public void showFailedError() {
        HankkinUtils.showToast(this, "获取列表失败");

    }
}
