package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.mvp_login.ILoginView;
import com.hankkin.compustrading.mvp_login.presenter.LoginPresenter;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.sharepreference.MySP;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements ILoginView{

    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.et_login_name)
    EditText etName;
    @Bind(R.id.et_login_pwd)
    EditText etPwd;
    @Bind(R.id.pw_loading)
    ProgressWheel wheel;

    public static LoginActivity instance;

    private LoginPresenter loginPresenter=new LoginPresenter(this);
    private String loginUrl= BaseUrl.baseUrl+"login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initViews();
    }


    private void initViews() {
        wheel.stopSpinning();
    }

    @OnClick(R.id.tv_register)
    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        wheel.spin();
        final String name = getUserName() ;
        final String pwd = getPassword();
        if (TextUtils.isEmpty(name)) {
            HankkinUtils.showToast(LoginActivity.this, "用户名不能为空");
            wheel.stopSpinning();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            HankkinUtils.showToast(LoginActivity.this, "密码不能为空");
            wheel.stopSpinning();
            return;
        }

        loginPresenter.login();

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
        return loginUrl;
    }

    @Override
    public void toMainActivity(Person person) {
        MySP.setPASSWoRD(LoginActivity.this, person.getPassword());
        MySP.setUSERNAME(LoginActivity.this, person.getTel());
        wheel.stopSpinning();
        HankkinUtils.showToast(LoginActivity.this, "登录成功");
        Intent intent = new Intent(LoginActivity.this, MainShowActivity.class);

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

    @Override
    public void showFailedError() {
        HankkinUtils.showToast(LoginActivity.this, "登录失败，密码或用户名有误");

    }
}
