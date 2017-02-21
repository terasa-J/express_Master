package com.hankkin.compustrading.activity;

import android.content.Intent;
import android.os.Environment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


import com.hankkin.compustrading.R;
import com.hankkin.compustrading.model.Person;


import java.io.File;




public class SplasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        long size = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "compustrading" +"/app-release.apk").length();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    queryCategory();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 界面跳转
     *
     */
    private void queryCategory() {

        Intent intent = new Intent(SplasActivity.this,MainShowActivity.class);
        Bundle bundle = new Bundle();

        Person person=new Person("测试","");
        bundle.putSerializable("loginPerson", person);

        intent.putExtras(bundle);
        startActivity(intent);

    }

}
