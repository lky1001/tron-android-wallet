package com.devband.tronwalletforandroid.ui.createaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class CreateAccountPresenter extends BasePresenter<CreateAccountView> {

    public CreateAccountPresenter(CreateAccountView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        Tron.getInstance(mContext).registerWaller("ㅇㅇㅇㅇㅇㅇㅇㅇ");
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void changedPassword(String password) {
        if (password != null && password.length() >= Tron.MIN_PASSWORD_LENGTH) {
            if (Tron.getInstance(mContext).registerWaller(password) == Tron.SUCCESS) {
                if (Tron.getInstance(mContext).registerWaller(password) == Tron.SUCCESS) {
                    String priKey = Tron.getInstance(mContext).getPrivateKey();
                    String address = Tron.getInstance(mContext).getAddress();
                } else {

                }
            } else {

            }
        } else {

        }
    }
}
