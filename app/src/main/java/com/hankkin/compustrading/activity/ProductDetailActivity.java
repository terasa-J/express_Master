package com.hankkin.compustrading.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Category;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_detail.IDetailView;
import com.hankkin.compustrading.mvp_detail.presenter.DetailPresenter;
import com.hankkin.compustrading.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProductDetailActivity extends BaseActivity implements IDetailView {

    private Product product=null;
    private Person person =null;
    private TextView tvDesc,tvProName,tvTime,tvSchool,tvPrice,tvUsername;
    private ImageView ivPro,ivTel,ivSms,ivLike;
    private RoundedImageView ivUserHead;
    //电话权限
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    //连接数据库
    private String url=BaseUrl.baseUrl+"queryCategory";
    private DetailPresenter detailPresenter = new DetailPresenter(this);

    private String likeUrl=BaseUrl.baseUrl+"likeProduct";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
      //  ButterKnife.bind(this);   //注解的方法初始化，要引入库
        product = (Product) getIntent().getSerializableExtra("product");
        person = getCurrentPerson(ProductDetailActivity.this);

        tvDesc = (TextView) findViewById(R.id.tv_pro_desc);
        tvProName = (TextView) findViewById(R.id.tv_pro_name);
        ivPro = (ImageView) findViewById(R.id.iv_product);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        ivTel= (ImageView) findViewById(R.id.iv_tel);
        ivSms= (ImageView) findViewById(R.id.iv_sms);
        ivLike= (ImageView) findViewById(R.id.iv_like);

        tvUsername= (TextView) findViewById(R.id.tv_username);
        ivUserHead= (RoundedImageView) findViewById(R.id.iv_user_head);

        //连接数据库

        detailPresenter.queryCategory();


        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_prodect_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        HankkinUtils.showToast(ProductDetailActivity.this, "分享");
                        break;
                    case R.id.action_settings:
                        HankkinUtils.showToast(ProductDetailActivity.this, "举报");
                        break;
                    case R.id.tv_back:
                        finish();
                        break;
                }
                return false;
            }
        });
 //收藏按钮
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接数据库
                int productId=product.getProductid();
                String tel=person.getTel();

                detailPresenter.likeProduct(productId,tel);
            }
        });
        //打电话
        ivTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(ProductDetailActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProductDetailActivity.this,
                            Manifest.permission.CALL_PHONE)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(ProductDetailActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(ProductDetailActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                }else {
                    // 已经获得授权，可以打电话
                    CallPhone();
                }
            }
        });
//发短信
        ivSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send SMS", "");

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                //收信人的电话   new String("25252")

                smsIntent.putExtra("address", new String( product.getPhone() ));

                //收信人的信息
                smsIntent.putExtra("sms_body", "请输入已收快递信息");
                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProductDetailActivity.this,
                            "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        CollapsingToolbarLayout collapsingAvatarToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingAvatarToolbar.setBackgroundColor(getResources().getColor(R.color.theme_color));
        collapsingAvatarToolbar.setExpandedTitleGravity(Gravity.CENTER_VERTICAL);
        collapsingAvatarToolbar.setExpandedTitleColor(getResources().getColor(R.color.theme_color));
        //登录者 信息
        TextView tv = (TextView) findViewById(R.id.username);
        RoundedImageView usericon= (RoundedImageView) findViewById(R.id.usericon);

        //获得登录者信息
        Person person=getCurrentPerson(this);
        if(person.getTel().equals("测试")){
            tv.setText(person.getTel());
            usericon.setImageDrawable(getResources().getDrawable(R.drawable.defaut));
        }else {
            tv.setText(person.getNickname());   //登录者的名字
            if(person.getUserIconUrl().length()==0){
                usericon.setImageDrawable(getResources().getDrawable(R.drawable.defaut));
            }else{
                ImageLoader.getInstance().displayImage(BaseUrl.baseUrl + person.getUserIconUrl(), usericon);
             }
        }

        tvProName.setText(product.getContent());
        tvTime.setText(product.getCreateat());   //接收时间

        ImageLoader.getInstance().displayImage(BaseUrl.baseUrl + product.getProductUrl(), ivPro);
        tvPrice.setText("报酬 ￥" + product.getPrice());
        tvSchool.setText(product.getSchool());
        tvDesc.setText("目的地"+product.getCid());   //发布时间,送往地方
        tvUsername.setText(product.getNickname());

        if(product.getUserIconUrl().length()==0){
            ivUserHead.setImageDrawable(getResources().getDrawable(R.drawable.defaut));

        }else {
            ImageLoader.getInstance().displayImage(BaseUrl.baseUrl + product.getUserIconUrl(), ivUserHead);
        }
    }
//拨打电话
    private void CallPhone() {

        // 拨号：激活系统的拨号组件
        String phone=product.getPhone();

        Log.e("phone++++", phone );


        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        startActivity(intent); // 激活Activity组件

    }


    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    CallPhone();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getCategoryId() {
        //从 0开始，记得加1
        return product.getCid();
    }

    @Override
    public void showSuccess(Category category) {
        tvDesc.setText("目的地:"+category.getType());

       // HankkinUtils.showLToast(this,"查询分类成功");

    }

    @Override
    public void showFailedError() {
        HankkinUtils.showLToast(this,"查询分类失败");
    }

    @Override
    public String getLikeUrl() {
        return likeUrl;
    }

    @Override
    public void likeSuccess(String message) {
        HankkinUtils.showLToast(this,message);

    }

    @Override
    public void likeFail() {
        HankkinUtils.showLToast(this,"收藏失败");
    }
}
