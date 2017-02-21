package com.hankkin.compustrading.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hankkin.compustrading.FileUploadListener;
import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.BitmapUtils;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_new.INewView;
import com.hankkin.compustrading.mvp_new.presenter.NewPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import me.drakeet.materialdialog.MaterialDialog;

public class NewProductActivity extends BaseActivity implements INewView{

    @Bind(R.id.et_tel)
    EditText etTel;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.spinner_school)
    BetterSpinner spinnerSchool;
    @Bind(R.id.tv_cate)
    TextView tvCate;
    @Bind(R.id.btn_fabu)
    Button btnFabu;
    @Bind(R.id.iv_add_pro)
    ImageView ivAddPro;
    @Bind(R.id.tv_back)
    TextView tvBack;

    private String[] schools;
    private String filePath = "";
    private int cid=0;     //显示送往的地方

    //电话权限
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public static String IMEI = null;

    private String newProUrl= BaseUrl.baseUrl+"addProduct";
    private NewPresenter newPresenter = new NewPresenter(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);

        init();

    }

    private void init(){
        schools = getResources().getStringArray(R.array.school);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, schools);
        spinnerSchool.setAdapter(adapter);

        String tel = getPhoneNumber(NewProductActivity.this);

        if (!TextUtils.isEmpty(tel)){
            etTel.setText(tel);
        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 选择分类对话框

     */
    @OnClick(R.id.tv_cate)
    void showCate(){
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        final String[] cates = getResources().getStringArray(R.array.cate);
        for (int j = 0; j < cates.length; j++) {

            arrayAdapter.add(cates[j]);
        }

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        final MaterialDialog alert = new MaterialDialog(this).setTitle(
                "请选择分类").setContentView(listView);

        alert.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cate = cates[position];
                tvCate.setText(cate);
                cid = position;
                alert.dismiss();

            }
        });
    }


    /**
     * 选择图片对话框
     */
    @OnClick(R.id.iv_add_pro)
    void showMDdialog() {
        View view = LayoutInflater.from(NewProductActivity.this).inflate(R.layout.view_select_img, null, false);
        final MaterialDialog dialog = new MaterialDialog(this).setView(view);
        dialog.show();
        TextView tvGallery = (TextView) view.findViewById(R.id.tv_gallery);
        //从照片中选择
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getImageFromGallery(NewProductActivity.this);
            }
        });
        TextView tvCamera = (TextView) view.findViewById(R.id.tv_camera);
        //拍照
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                filePath = iniFilePath(NewProductActivity.this);
                goCamera(NewProductActivity.this, filePath);
            }
        });
    }

    /**
     * 接受选择照片的结果显示
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY) {    //从相册中得到
            if(data == null){
                return;
            }
            filePath = getPath(NewProductActivity.this, data.getData());
            //  data.getData() uri
            Log.e("filePath+++++++++++++01",filePath);

            Log.e("filePath++++++55555555", String.valueOf(data.getData()));

            if (!TextUtils.isEmpty(filePath)) {
                ivAddPro.setImageBitmap(BitmapUtils.getCompressedBitmap(NewProductActivity.this, filePath));
            }

        } else if (requestCode == REQUST_CODE_CAMERA) {
            if (!TextUtils.isEmpty(filePath)) {

              Bitmap  temp  =  BitmapUtils.getCompressedBitmap(NewProductActivity.this, filePath);
                if(temp==null){
                    return;
                }
                ivAddPro.setImageBitmap(temp);
            }
        }
    }

    /**
     * 发布按钮点击事件
     * 上传图片， 创建新快递
     */
    @OnClick(R.id.btn_fabu)
    void addNewPro(){
        if (!HankkinUtils.isMobileNO(etTel.getText().toString().trim())){
            HankkinUtils.showToast(NewProductActivity.this,"请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(etPrice.getText().toString().trim())||
                TextUtils.isEmpty(etTel.getText().toString().trim())||
                TextUtils.isEmpty(etName.getText().toString().trim())){
            HankkinUtils.showToast(NewProductActivity.this,"请完善信息");
            return;
        }
        if (TextUtils.isEmpty(tvCate.getText())||TextUtils.isEmpty(spinnerSchool.getText().toString())){
            HankkinUtils.showToast(NewProductActivity.this,"请完善信息");
            return;
        }
        if (!TextUtils.isEmpty(filePath)){
            showLoadingDialog();
            Bitmap tempBitmap = BitmapUtils.getCompressedBitmap(NewProductActivity.this, filePath);
            if (BitmapUtils.readPictureDegree(filePath) == 90) {
                tempBitmap = BitmapUtils.toturn(tempBitmap);
            }
          //  filePath = BitmapUtils.saveBitmap(tempBitmap,new Date().getTime() + "");

            Log.e("filePath+++++++++02222",filePath);

            uploadImg(filePath, NewProductActivity.this, new FileUploadListener() {
                @Override
                public void success(String url) {
             Product newProduct = new Product();

            newProduct.setContent(etName.getText().toString());
            newProduct.setCid(cid);
            newProduct.setNickname(getCurrentPerson(NewProductActivity.this).getNickname());
            newProduct.setPrice(etPrice.getText().toString());
            newProduct.setSchool(spinnerSchool.getText().toString());

            newProduct.setProductUrl(url);

            newProduct.setTel(getCurrentPerson(NewProductActivity.this).getTel());//发布者的联系方式
            newProduct.setPhone(etTel.getText().toString());
            newProduct.setUserIconUrl(getCurrentPerson(NewProductActivity.this).getUserIconUrl());
            newProduct.setCreateat(HankkinUtils.getCurrentTime());  //获得发布时
            // 连接数据库
            newPresenter.addProduct(newProduct);

                }

                @Override
                public void fail() {
                    dimissDialog();
                    HankkinUtils.showToast(NewProductActivity.this, "发布失败");
                }
            });
        }
    }


//获得本机电话号码
    public String  getPhoneNumber(Context context) {
        String result="";
        int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (osVersion > 22) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                result= getImei();
            }
        } else {
            //如果SDK小于6.0则不去动态申请权限
            result= getImei();
        }
        return result;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {

            getImei();
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"授权拒绝",Toast.LENGTH_SHORT).show();
        }
    }
    //电话权限的 Imei
    public String getImei(){
        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();
        String mtype = android.os.Build.MODEL;

        Log.d("Main",mtype);
        Toast.makeText(this,"IMEI的值为："+mtype,Toast.LENGTH_SHORT).show();

        return tm.getLine1Number();
    }

    @Override
    public String getUrl() {
        return newProUrl;
    }


    @Override
    public void showAddSuccess(int result) {
        Log.e("发布成功+++++++++++",result+"");
        dimissDialog();
        HankkinUtils.showToast(NewProductActivity.this, "发布成功");
        finish();

    }

    @Override
    public void showFailedError() {
        dimissDialog();
        HankkinUtils.showToast(NewProductActivity.this,"发布失败");

    }
}
