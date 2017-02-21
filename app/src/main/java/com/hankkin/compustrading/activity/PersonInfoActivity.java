package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.mvp_login.ILoginView;
import com.hankkin.compustrading.mvp_login.presenter.LoginPresenter;
import com.hankkin.compustrading.mvp_person.IPersonView;
import com.hankkin.compustrading.mvp_person.presenter.PersonPresenter;
import com.hankkin.compustrading.sharepreference.MySP;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonInfoActivity extends BaseActivity implements IPersonView {

    @Bind(R.id.btn_update)
    Button btnUpdate;
    @Bind(R.id.tv_back)
    TextView tvBack;

    @Bind(R.id.et_nick_name)
    EditText etName;
    @Bind(R.id.et_sex)
    EditText etSex;
    @Bind(R.id.et_birth)
    EditText etBirth;

    @Bind(R.id.pw_loading)
    ProgressWheel wheel;

    public static PersonInfoActivity instance;

    private String url=BaseUrl.baseUrl+"updateInfo";
    private PersonPresenter personPresenter =new PersonPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_person_info);

        ButterKnife.bind(this);
        initData();
        //停止加载
        initViews();
    }

    private void initData(){
        //关闭软键盘 (测试)
        etName.setInputType(InputType.TYPE_NULL);
        etSex.setInputType(InputType.TYPE_NULL);
        etBirth.setInputType(InputType.TYPE_NULL);

        Person person=getCurrentPerson(this);
        etName.setText("   "+person.getNickname());
        etSex.setText("   " + person.getSex());
        etBirth.setText("   " + person.getBirth());

    }
    private void initViews() {
        wheel.stopSpinning();
    }


    @OnClick(R.id.tv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_update)
    public void update() {
        String info = (String) btnUpdate.getText();
        if("修改个人信息".equals(info)){
           // wheel.spin();
            //开启软键盘
            etName.setInputType(InputType.TYPE_CLASS_TEXT);
            etSex.setInputType(InputType.TYPE_CLASS_TEXT);
            etBirth.setInputType(InputType.TYPE_CLASS_TEXT);
            btnUpdate.setText("确定");
            HankkinUtils.showToast(PersonInfoActivity.this, "可以进行编辑了");

        }else if("确定".equals(info)){
            String nickName= String.valueOf(etName.getText()).trim();
            String sex= String.valueOf(etSex.getText()).trim();
            String birth= String.valueOf(etBirth.getText()).trim();
            //判断空
            if (TextUtils.isEmpty(nickName)) {
                HankkinUtils.showToast(PersonInfoActivity.this, "昵称不能为空");
                return;
            }
            if (TextUtils.isEmpty(sex)) {
                HankkinUtils.showToast(PersonInfoActivity.this, "性别不能为空");
                return;
            }
            if (TextUtils.isEmpty(birth)) {
                HankkinUtils.showToast(PersonInfoActivity.this, "破蛋日不能为空");
                return;
            }
            //加载
            wheel.spin();

            Person person=getCurrentPerson(this);
            person.setNickname(nickName);
            person.setBirth(birth);
            person.setSex(sex);
            //连接数据库
            personPresenter.updatePerson(person);
            btnUpdate.setText("修改个人信息");

        }


    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void showSuccess(Person person) {
        //关闭软键盘
        etName.setInputType(InputType.TYPE_NULL);
        etSex.setInputType(InputType.TYPE_NULL);
        etBirth.setInputType(InputType.TYPE_NULL);

        Person personInfo=getCurrentPerson(this);
        personInfo.setNickname(person.getNickname());
        personInfo.setBirth(person.getBirth());
        personInfo.setTel(person.getTel());

        etName.setText("   "+person.getNickname());
        etSex.setText("   " + person.getSex());
        etBirth.setText("   " + person.getBirth());
        //停止加载
        initViews();
        HankkinUtils.showLToast(this,"更新成功,重新登录有效");


    }

    @Override
    public void showFailedError() {
        HankkinUtils.showLToast(this,"更新失败，可能服务器连接问题");
    }
}
