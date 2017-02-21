package com.hankkin.compustrading.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hankkin.compustrading.FileUploadListener;
import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Person;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;


import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基本Activity
 */
public class BaseActivity extends AppCompatActivity {

    /*请求相机Code*/
    public static final int REQUST_CODE_CAMERA = 0;
    /*请求相册Code*/
    public static final int REQUEST_CODE_GALLERY = 1;
    /*发布商品Code*/
    public static final int REQUEST_CODE_FABU = 2;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    public MaterialDialog loadDialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    public Person getCurrentPerson(Context context){

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

       Person person= (Person) bundle.getSerializable("loginPerson");
        return person;
    }

    /**
     * 调用相册

     * @param act
     */
    public  void getImageFromGallery(Activity act) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "选择图片时需要读取权限",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, REQUEST_CODE_GALLERY);
        }
    }


    /**
     * 上传图片
         void  String filepath
     */

    public void uploadImg(String filepath,Context context, final FileUploadListener listener){

        final String imagePath = Uri.decode(filepath);
        final OkHttpClient mOkHttpClient = new OkHttpClient();


        MultipartBody.Builder builder = new MultipartBody.Builder();

        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        final Request request = reqBuilder
                .url(BaseUrl.baseUrl + "uploadimage")
                .post(requestBody)
                .build();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = mOkHttpClient.newCall(request).execute();
                    String resultValue=null;
                    if (response.isSuccessful()) {
                        resultValue = response.body().string();

                        Log.e("resultValue-----11", resultValue);
                        listener.success(resultValue);
                    }else{
                        Log.e("resultValue-----00", "error");

                        listener.fail();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("dianji-----",  e.toString());
                }

            }
        }).start();

    }


    public void showLoadingDialog(){
        loadDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.loading,null,false);
        ProgressWheel wheel = (ProgressWheel) view.findViewById(R.id.pw_loading);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,80);
        params.height = HankkinUtils.dip2px(this,80);
        params.width = HankkinUtils.dip2px(this,80);
        wheel.setLayoutParams(params);
        wheel.setBackgroundColor(getResources().getColor(R.color.light_white));
        loadDialog.setView(view);
        loadDialog.setBackgroundResource(getResources().getColor(R.color.transparent));
        loadDialog.show();
    }

    public void dimissDialog(){
        if (loadDialog!=null){
            loadDialog.dismiss();
        }
    }

    /**
     * 初始化图片路径
     *
     * @return
     */
    public static String iniFilePath(Activity act) {
        String filepath = null;
        String path = null;
        File fileSD = null;

        // 准备存储位置
        boolean sdExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (!sdExist) {
            HankkinUtils.showLToast(act, "没有找到SD存储卡");
            return null;

        } else {
            //TODO 内容提示完善
            path = Environment.getExternalStorageDirectory().getPath() + "/compustrading/Camera";
            fileSD = new File(path);
            if (fileSD.exists()) {
                filepath = path + "/" + System.currentTimeMillis() + ".jpg";
            } else {
                fileSD.mkdir();
                filepath = fileSD.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            }
            return filepath;
        }
    }

    /**
     * 调用相机
     *
     * @param act
     */
    public  void  goCamera(Activity act, String filepath) {
        /*File file = new File(filepath);
        // 启动Camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        act.startActivityForResult(intent, REQUST_CODE_CAMERA);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "拍照时需要存储权限", REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {

            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filepath)));
            startActivityForResult(takeIntent, REQUST_CODE_CAMERA);
        }
    }

    /**
     * android系统版本选择图库图片解决方法---获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = MediaStore.MediaColumns._ID + "=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * android系统版本选择图库图片解决方法--获取数据
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    /**
     * 请求权限
     *
     * 如果权限被拒绝过，则提示用户需要权限
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog("权限需求", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, "确定", null, "取消");
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }
    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        AlertDialog mAlertDialog = builder.show();
    }

}
