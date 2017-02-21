package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;

import com.hankkin.compustrading.adapter.ProductAdapter;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_main.IMainView;
import com.hankkin.compustrading.mvp_main.presenter.MainPresenter;
import com.hankkin.compustrading.view.RefreshLayout;
import com.hankkin.compustrading.view.RippleView;
import com.hankkin.compustrading.view.RoundedImageView;
import com.hankkin.compustrading.view.floatbutton.FloatingActionButton;
import com.hankkin.compustrading.view.floatbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainShowActivity extends  BaseActivity implements IMainView,SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;



    @Bind(R.id.rv_usericon)
    RoundedImageView rvUser;
    @Bind(R.id.tv_person)
    TextView tvPerson;
    @Bind(R.id.rv_logreg)
    RippleView rvLogReg;
    @Bind(R.id.tv_username)
    TextView tvName;
    @Bind(R.id.rv_buy)
    RippleView rvBuy;
    @Bind(R.id.rv_sale)
    RippleView rvSale;
    @Bind(R.id.rv_sina)
    RippleView rvSina;
    @Bind(R.id.rv_qq)
    RippleView rvQQ;
    @Bind(R.id.tv_qq)
    TextView tvQQ;
    @Bind(R.id.tv_sina)
    TextView tvSina;
    @Bind(R.id.tv_buy)
    TextView tvBuy;
    @Bind(R.id.tv_sale)
    TextView tvSale;
    @Bind(R.id.tv_show)
    TextView tvShow;

    @Bind(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

    @Bind(R.id.pager)
    ListView pager;

    public static MainShowActivity instance;
    private Handler handler;
    private Person person;
    private String tel;

    private RefreshLayout swipeRefreshLayout;

    //连接服务器
    private String mainUrl=BaseUrl.baseUrl+"productList";
    private MainPresenter mainPresenter = new MainPresenter(MainShowActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main_show);
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
       drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerToggle = new ActionBarDrawerToggle(MainShowActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
       drawerLayout.setDrawerListener(drawerToggle);

        //设定左上角突变可点击
        getSupportActionBar().setHomeButtonEnabled(true);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle(getResources().getString(R.string.action_title)+"School");

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
/*圆圈的　　更新　　按钮*/
       FloatingActionButton fbUpdate = (FloatingActionButton) findViewById(R.id.fb_update);
        fbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainPresenter.queryProducts();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        /**
         * 圆圈新建按钮点击事件
         */
      FloatingActionButton fbWrite = (FloatingActionButton) findViewById(R.id.fb_new);
       fbWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Person person = getCurrentPerson(MainShowActivity.this);
                String tel =person.getTel();

                if (!(tel.equals("测试"))) {

                    Intent intent = new Intent(MainShowActivity.this, NewProductActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("loginPerson", person);

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(MainShowActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HankkinUtils.showToast(MainShowActivity.this, "请先登录");
                }
            }
        });
//圆圈个人中心
        FloatingActionButton fbMy = (FloatingActionButton) findViewById(R.id.fb_person);
        fbMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainShowActivity.this, PersonActivity.class);
                Bundle bundle = new Bundle();

                Person person=getCurrentPerson(MainShowActivity.this);
                bundle.putSerializable("loginPerson", person);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        //判断是否登录了
         person = getCurrentPerson(MainShowActivity.this);
         tel =person.getTel();
        if (!(tel.equals("测试"))) {
            rvBuy.setVisibility(View.VISIBLE);
            rvSale.setVisibility(View.VISIBLE);
            rvQQ.setVisibility(View.GONE);
            rvSina.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(person.getUserIconUrl())) {
                ImageLoader.getInstance().displayImage(BaseUrl.baseUrl+person.getUserIconUrl(), rvUser);
            }
            tvPerson.setText("个人中心");
            if (!TextUtils.isEmpty(person.getNickname())) {
                tvName.setText(person.getNickname());
            } else {
                tvName.setText("用户" + person.getTel().substring(0, 3));
            }
            tvShow.setText("我的");
        } else {
           // rvUser.setBackground(getResources().getDrawable(R.drawable.defaut));
            tvPerson.setText("登录或注册");
            tvName.setText("");
            rvBuy.setVisibility(View.GONE);
            rvSale.setVisibility(View.GONE);
            rvQQ.setVisibility(View.VISIBLE);
            rvSina.setVisibility(View.VISIBLE);
            tvShow.setText("其他登录方式");
        }


        rvLogReg.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (!(tel.equals("测试"))) {
                    Intent intent = new Intent(MainShowActivity.this, PersonActivity.class);
                    Bundle bundle = new Bundle();

                    Person person=getCurrentPerson(MainShowActivity.this);
                    bundle.putSerializable("loginPerson", person);

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainShowActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        rvBuy.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                drawerLayout.closeDrawers();
            }
        });
        rvSale.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(MainShowActivity.this, NewProductActivity.class);
                Bundle bundle = new Bundle();
                Person person=getCurrentPerson(MainShowActivity.this);
                bundle.putSerializable("loginPerson", person);

                intent.putExtras(bundle);
                startActivity(intent);
                drawerLayout.closeDrawers();
            }
        });


     //加载product数据
        //加载数据   连接数据库服务器
        showLoadingDialog();
        mainPresenter.queryProducts();


    }





    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        drawerToggle.syncState();
    }

    /** 设备配置改变时 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_person) {
            Intent intent = new Intent(MainShowActivity.this, PersonActivity.class);

            Bundle bundle = new Bundle();

            Person person=getCurrentPerson(MainShowActivity.this);
            bundle.putSerializable("loginPerson", person);

            intent.putExtras(bundle);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (floatingActionsMenu.isExpanded()) {
                floatingActionsMenu.collapse();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//下滑加载
    @Override
    public void onLoad() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新连接数据库
                mainPresenter.queryProducts();
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
                mainPresenter.queryProducts();
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

        final ProductAdapter productAdapter = new ProductAdapter(this, products);
        //自定义回调函数
        productAdapter.setOncheckChanged(new ProductAdapter.OnMyCheckChangedListener() {

            @Override
            public void setSelectID(int selectID) {
                productAdapter.setSelectID(selectID);                //选中位置
                productAdapter.notifyDataSetChanged();        //刷新适配器
            }
        });
        pager.setAdapter(productAdapter);

//点击跳转
        pager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", (Serializable) productAdapter.getItem(position));

                Person person=getCurrentPerson(MainShowActivity.this);
                bundle.putSerializable("loginPerson", person);
                tel =person.getTel();
                if (tel.equals("测试")) {
                     intent = new Intent(MainShowActivity.this,LoginActivity.class);
                     HankkinUtils.showLToast(MainShowActivity.this,"请先登录");
                }else{
                     intent = new Intent(MainShowActivity.this,ProductDetailActivity.class);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setLoading(false);
        swipeRefreshLayout.setRefreshing(false);
        //加载完毕，停止刷新
        dimissDialog();

    }

    @Override
    public void showFailedError() {
        HankkinUtils.showToast(this, "获取快递列表失败");

    }
}
