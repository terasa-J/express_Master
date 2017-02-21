package com.hankkin.compustrading.mvp_person.presenter;

import android.os.Handler;

import com.hankkin.compustrading.model.Person;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_person.IPersonView;
import com.hankkin.compustrading.mvp_person.model.PersonModelImp;
import com.hankkin.compustrading.mvp_person.model.OnCheckListener;


public class PersonPresenter {
    private PersonModelImp personModelImp;
    private IPersonView iPersonView;
    private Handler handler=new Handler();
    //传入对应的view
    public PersonPresenter(IPersonView iPersonView){
        personModelImp =new PersonModelImp();
        this.iPersonView = iPersonView;
    }
    public void updateIconUrl(Person person){
        personModelImp.updateIconUrl(person, iPersonView.getUrl(), new OnCheckListener() {
            @Override
            public void updateSuccess(final Person person1) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iPersonView.showSuccess(person1);
                    }
                });

            }

            @Override
            public void updateFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iPersonView.showFailedError();
                    }
                });

            }
        });

    }
    //更新个人信息
    public void updatePerson(Person person){
        personModelImp.updatePerson(person, iPersonView.getUrl(), new OnCheckListener() {
            @Override
            public void updateSuccess(final Person person) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iPersonView.showSuccess(person);
                    }
                });

            }

            @Override
            public void updateFail() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iPersonView.showFailedError();
                    }
                });

            }
        });

    }
}
