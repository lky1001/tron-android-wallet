package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.AccountVote;
import com.devband.tronlib.dto.AccountVotes;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class VotePresenter extends BasePresenter<VoteView> {

    private AdapterDataModel<AccountVote> mAdapterDataModel;

    public VotePresenter(VoteView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<AccountVote> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {

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

    public void getVotes(@NonNull String address, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        TronNetwork.getInstance().getAccountVotes(address, startIndex, pageSize, "-votes")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AccountVotes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountVotes accountVotes) {
                        mAdapterDataModel.addAll(accountVotes.getData());
                        mView.finishLoading(accountVotes.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
