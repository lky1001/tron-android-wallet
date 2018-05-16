package com.devband.tronwalletforandroid.ui.tronaccount;

import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.tronaccount.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.tronaccount.dto.TronAccountList;

import org.tron.protos.Protocol;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TronAccountPresenter extends BasePresenter<TronAccountView> {

    private AdapterDataModel<TronAccount> mAdapterDataModel;

    public TronAccountPresenter(TronAccountView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<TronAccount> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        mView.showLoadingDialog();
        getTronAccountList();
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

    public void getTronAccountList() {
        Single.fromCallable(() -> Tron.getInstance(mContext).getTronAccountList().blockingGet())
        .map(accountList -> {
            List<TronAccount> tronAccountList = new ArrayList<>();

            int cnt = accountList.getAccountsCount();

            long highestTrx = 0;

            for (int i = 0; i < cnt; i++) {
                Protocol.Account account = accountList.getAccounts(i);

                tronAccountList.add(TronAccount.builder()
                        .name(new String(account.getAccountName().toByteArray(), Charset.forName("UTF-8")))
                        .address(AccountManager.encode58Check(account.getAddress().toByteArray()))
                        .balance(account.getBalance())
                        .build());

                if (account.getBalance() > highestTrx) {
                    highestTrx = account.getBalance();
                }
            }

            Descending descending = new Descending();
            Collections.sort(tronAccountList, descending);

            // max 100
            tronAccountList = tronAccountList.subList(0, 99);

            return TronAccountList.builder()
                    .accountList(tronAccountList)
                    .accountCount(cnt)
                    .highestBalance(highestTrx)
                    .build();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<TronAccountList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(TronAccountList tronAccountList) {
                mAdapterDataModel.addAll(tronAccountList.getAccountList());
                mView.displayTronAccountInfo(tronAccountList.getAccountCount(), tronAccountList.getHighestBalance());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    class Descending implements Comparator<TronAccount> {

        @Override
        public int compare(TronAccount o1, TronAccount o2) {
            return o2.getBalance().compareTo(o1.getBalance());
        }
    }
}
