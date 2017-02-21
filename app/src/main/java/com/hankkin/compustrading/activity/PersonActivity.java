package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankkin.compustrading.FileUploadListener;
import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.BitmapUtils;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.PersonShow;
import com.hankkin.compustrading.mvp_person.IPersonView;
import com.hankkin.compustrading.mvp_person.presenter.PersonPresenter;
import com.hankkin.compustrading.view.PullToZoomScrollViewEx;
import com.hankkin.compustrading.view.RippleView;
import com.hankkin.compustrading.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

public class PersonActivity extends BaseActivity implements IPersonView {
    @Bind(R.id.tv_back)
    TextView tvBack;
    private PullToZoomScrollViewEx scrollView;
    private List<PersonShow> data = new ArrayList<>();

    private TextView tvLogin, tvRegister,tvUserName;
    private RoundedImageView ivUserIcon;

    View headView;
    View zoomView;
    View contentView;

    public static PersonActivity instance;
    private String filePath = "";
    private RippleView rvLogout,rvPersonInfo;
    private RippleView rvBuy;
    private RippleView rvSale;
    private TextView tvLogout,tvMeProduct,tvCollection;

    String iconUrl=BaseUrl.baseUrl+"updateIconUrl";
    private PersonPresenter personPresenter=new PersonPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);   //注解的方法初始化，要引入库
        initViews();
        initData();
    }

    private void initViews() {

        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.my_pull_scoll);
        loadViewForCode();

        scrollView.getPullRootView().findViewById(R.id.tv_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

        Person person= getCurrentPerson(this);
        String tel =person.getTel();

        if (!(tel.equals("测试"))) {
            if(person.getUserIconUrl()!=null){
                ImageLoader.getInstance().displayImage(BaseUrl.baseUrl + person.getUserIconUrl(), ivUserIcon);
            }else{
                ivUserIcon.setImageDrawable(getResources().getDrawable(R.drawable.defaut));
            }
            tvUserName.setText(person.getNickname());
            tvLogin.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);
            tvLogout.setText("退出账号");
        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvRegister.setVisibility(View.VISIBLE);
            ivUserIcon.setImageDrawable(getResources().getDrawable(R.drawable.defaut));
            tvLogout.setText("登录");
        }
    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.my_pull_scoll);
        headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        contentView = LayoutInflater.from(this).inflate(R.layout.profile_contect_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        tvUserName = (TextView) headView.findViewById(R.id.tv_user_name);

        tvLogin = (TextView) headView.findViewById(R.id.tv_login);
        tvRegister = (TextView) headView.findViewById(R.id.tv_register);
        ivUserIcon = (RoundedImageView) headView.findViewById(R.id.iv_user_head);
        rvLogout = (RippleView) contentView.findViewById(R.id.rv_logout);
        tvLogout = (TextView) contentView.findViewById(R.id.tv_logout);
        //我的发布
        tvMeProduct= (TextView) contentView.findViewById(R.id.tv_me_product);
        //我的收藏
        tvCollection = (TextView) contentView.findViewById(R.id.tv_collection);
        //个人信息
        rvPersonInfo= (RippleView) contentView.findViewById(R.id.rv_personInfo);
        rvPersonInfo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent =null;
                Person person=getCurrentPerson(PersonActivity.this);
                if(person.getTel().equals("测试")){
                    intent = new Intent(PersonActivity.this,LoginActivity.class);
                    HankkinUtils.showLToast(PersonActivity.this,"请先登录");

                }else{
                    intent = new Intent(PersonActivity.this,PersonInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loginPerson", person);
                    intent.putExtras(bundle);
                }

                startActivity(intent);
                finish();
            }
        });
        //我的发布
        tvMeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = getCurrentPerson(PersonActivity.this);
                String tel = person.getTel();

                if (!(tel.equals("测试"))) {
                    //连接数据库，获得发布的信息
                    Intent intent = new Intent(PersonActivity.this, MyProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loginPerson", person);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HankkinUtils.showToast(PersonActivity.this, "请先登录");
                    finish();
                }

            }
        });
        //我的收藏
        tvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = getCurrentPerson(PersonActivity.this);
                String tel = person.getTel();

                if (!(tel.equals("测试"))) {
                    //连接数据库，获得发布的信息
                    Intent intent = new Intent(PersonActivity.this, CollectionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loginPerson", person);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HankkinUtils.showToast(PersonActivity.this, "请先登录");
                    finish();
                }

            }
        });
        //退出帐号
     /*   rvLogout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {*/
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("退出账号".equals(tvLogout.getText())){
                            HankkinUtils.showToast(PersonActivity.this, "已注销");

                            Intent intent = new Intent(PersonActivity.this, MainShowActivity.class);
                            Bundle bundle = new Bundle();
                            Person person = getCurrentPerson(PersonActivity.this);
                            person.setTel("测试");
                            bundle.putSerializable("loginPerson", person);
                            intent.putExtras(bundle);
                            startActivity(intent);


                            initData();
                            finish();

                        }else if("登录".equals(tvLogout.getText())){
                            Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                          /*  Bundle bundle = new Bundle();
                            Person person = getCurrentPerson(PersonActivity.this);
                            person.setTel("测试");
                            bundle.putSerializable("loginPerson", person);
                            intent.putExtras(bundle);*/
                            startActivity(intent);
                            initData();
                            finish();

                        }


                    }
                });


         //   }
       // });

        rvBuy = (RippleView) contentView.findViewById(R.id.rv_buy);
        rvBuy.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                finish();
            }
        });

        rvSale = (RippleView) contentView.findViewById(R.id.rv_sale);
        rvSale.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Person person = getCurrentPerson(PersonActivity.this);
                String tel =person.getTel();

                if (!(tel.equals("测试"))) {

                    Intent intent = new Intent(PersonActivity.this, NewProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("loginPerson", person);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HankkinUtils.showToast(PersonActivity.this, "请先登录");
                    finish();
                }


            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        ivUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person =  getCurrentPerson(PersonActivity.this);
                String tel =person.getTel();

                if (!(tel.equals("测试"))) {
                    showMDdialog();
                }else{
                    Intent intent = new Intent(PersonActivity.this,LoginActivity.class);
                    startActivity(intent);
                    HankkinUtils.showLToast(PersonActivity.this,"请先登录");
                }

            }
        });
    }

    /**
     * 选择图片对话框
     *
     */
    private void showMDdialog() {
        View view = LayoutInflater.from(PersonActivity.this).inflate(R.layout.view_select_img, null, false);
        final MaterialDialog dialog = new MaterialDialog(this).setView(view);
        dialog.show();
        TextView tvGallery = (TextView) view.findViewById(R.id.tv_gallery);
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getImageFromGallery(PersonActivity.this);
            }
        });
        TextView tvCamera = (TextView) view.findViewById(R.id.tv_camera);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                filePath = iniFilePath(PersonActivity.this);
                goCamera(PersonActivity.this, filePath);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            showLoadingDialog();
            //更换头像
            filePath = getPath(PersonActivity.this, data.getData());
            if (!TextUtils.isEmpty(filePath)) {
                uploadImg(filePath, PersonActivity.this, new FileUploadListener() {

                    @Override
                    public void success(final String url) {
                        Person person =  getCurrentPerson(PersonActivity.this);
                        String tel =person.getTel();

                        if (!(tel.equals("测试"))) {
                            person.setUserIconUrl(url);
                            //连接数据库，更新信息
                            personPresenter.updateIconUrl(person);

                           /* person.setUserIconUrl(BaseUrl.baseUrl+url);
                            ImageLoader.getInstance().displayImage(url, ivUserIcon);
                            updateUser(person);*/
                        }
                    }

                    @Override
                    public void fail() {
                        dimissDialog();
                        HankkinUtils.showToast(PersonActivity.this, "上传失败");
                    }
                });
            }
        } else if (requestCode == REQUST_CODE_CAMERA) {
            showLoadingDialog();
            if (!TextUtils.isEmpty(filePath)) {
                Bitmap tempBitmap = BitmapUtils.getCompressedBitmap(PersonActivity.this, filePath);
                if (BitmapUtils.readPictureDegree(filePath) == 90) {
                    tempBitmap = BitmapUtils.toturn(tempBitmap);
                }
                filePath = BitmapUtils.saveBitmap(tempBitmap,new Date().getTime() + "");
                uploadImg(filePath, PersonActivity.this, new FileUploadListener() {

                    @Override
                    public void success(String url) {
                        Person person =  getCurrentPerson(PersonActivity.this);
                        String tel =person.getTel();

                        if (!(tel.equals("测试"))) {
                            person.setUserIconUrl(url);
                            //连接数据库，更新信息
                            personPresenter.updateIconUrl(person);

                           /* person.setUserIconUrl(url);
                            ImageLoader.getInstance().displayImage(url, ivUserIcon);
                            updateUser(person);*/
                        }
                    }

                    @Override
                    public void fail() {
                        dimissDialog();
                        HankkinUtils.showToast(PersonActivity.this, "上传失败");
                    }
                });
            }
        }
    }



    @Override
    public String getUrl() {
        return iconUrl;
    }

    @Override
    public void showSuccess(Person person) {
        Person personNew =  getCurrentPerson(PersonActivity.this);
        personNew.setUserIconUrl(personNew.getUserIconUrl());
        Log.e("上传头像+++++",person+"");
        ImageLoader.getInstance().displayImage(BaseUrl.baseUrl+person.getUserIconUrl(), ivUserIcon);
        dimissDialog();
        HankkinUtils.showToast(PersonActivity.this, "上传成功,下次登录生效");

    }

    @Override
    public void showFailedError() {
        dimissDialog();
        HankkinUtils.showToast(PersonActivity.this, "上传失败");

    }
}
