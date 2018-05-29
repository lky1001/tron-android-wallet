package com.devband.tronwalletforandroid.ui.token;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TokenPresenter extends BasePresenter<TokenView> {

    private AdapterDataModel<Token> mAdapterDataModel;

    public TokenPresenter(TokenView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<Token> adapterDataModel) {
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

    public void loadItems(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        TronNetwork.getInstance().getTokens(startIndex, pageSize, "-name", "ico")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Tokens>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Tokens tokens) {
                mAdapterDataModel.addAll(tokens.getData());
                mView.finishLoading(tokens.getTotal());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }
}
