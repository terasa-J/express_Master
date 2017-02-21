package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.mvp_register.IRegisterView;
import com.hankkin.compustrading.mvp_register.presenter.RegisterPresenter;
import com.hankkin.compustrading.sharepreference.MySP;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.et_login_name)
    EditText etName;
    @Bind(R.id.et_login_pwd)
    EditText etPwd;
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.pw_loading)
    ProgressWheel wheel;

    private RegisterPresenter registerPresenter=new RegisterPresenter(this); //业务处理
    private String url= BaseUrl.baseUrl+"register";   //服务器端的地址




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        initViews();


    }

    private void initViews() {
        //停止刷新
        wheel.stopSpinning();
    }

    /**
     * 注册用户
     */
    @OnClick(R.id.btn_register)
    public void register(View view) {
        //开始刷新
        wheel.spin();
        final String name = getUserName();
        final String pwd = getPassword();
        if (TextUtils.isEmpty(name)) {
            HankkinUtils.showToast(RegisterActivity.this, "手机号不能为空");
            initViews();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            HankkinUtils.showToast(RegisterActivity.this, "密码不能为空");
            initViews();
            return;
        }
        if (!HankkinUtils.isMobileNO(name)) {
            HankkinUtils.showToast(RegisterActivity.this, "请输入正确的手机号");
            initViews();
            return;
        }
        registerPresenter.register();

    }

/*
返回按钮
 */
    @OnClick(R.id.tv_back)
    void back() {
        finish();
    }



    @Override
    public String getUserName() {
        return etName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPwd.getText().toString().trim();
    }

    @Override
    public String getUrl() {
        return url;
    }


    //成功跳转界面
    @Override
    public void toMainActivity(Person person) {
        MySP.setPASSWoRD(RegisterActivity.this, person.getPassword());
        MySP.setUSERNAME(RegisterActivity.this, person.getTel());
        wheel.stopSpinning();
        HankkinUtils.showToast(RegisterActivity.this, "注册成功,请登录");

        Intent intent = new Intent(RegisterActivity.this,MainShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("loginPerson", person);
        intent.putExtras(bundle);
        startActivity(intent);

        finish();
        if (LoginActivity.instance != null) {
            LoginActivity.instance.finish();
        }
        if (PersonActivity.instance != null) {
            PersonActivity.instance.finish();
        }
        if (MainShowActivity.instance != null) {
            MainShowActivity.instance.finish();
        }

    }
//失败显示信息
    @Override
    public void showFailedError() {
        wheel.stopSpinning();
        HankkinUtils.showToast(RegisterActivity.this, "注册失败,用户名已经存在");
    }
}
