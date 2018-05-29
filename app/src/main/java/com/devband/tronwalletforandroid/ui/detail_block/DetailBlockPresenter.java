package com.devband.tronwalletforandroid.ui.detail_block;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.detail_block.model.BaseModel;
import com.devband.tronwalletforandroid.ui.detail_block.model.BlockStatModel;
import com.devband.tronwalletforandroid.ui.detail_block.model.HeaderModel;
import com.devband.tronwalletforandroid.ui.detail_block.model.TransferHistoryModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2018. 5. 28..
 */

public class DetailBlockPresenter extends BasePresenter<DetailBlockView> {

    public DetailBlockPresenter(DetailBlockView view) {
        super(view);
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

    public void loadData(String address) {
        mView.showLoadingDilaog();

        Single.zip(
                loadBlockStat(address),
                loadTransferHistory(address),
                (blockStat, transfers) -> {
                    List<BaseModel> viewModels = new ArrayList<>();

                    //add block stat
                    viewModels.add(blockStat);

                    //add transfer list
                    if (transfers != null && transfers.size() > 0) {
                        viewModels.add(new HeaderModel(mContext.getString(R.string.list_header_transfers)));
                        for (Transfer transfer : transfers) {
                            viewModels.add(new TransferHistoryModel(transfer));
                        }
                    }
                    return viewModels;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewModels -> {
                            if (mView != null) {
                                mView.dataLoadSuccess(viewModels);
                            }
                        }, t -> {
                            if (mView != null) {
                                mView.showServerError();
                            }
                        }
                );
    }

    private Single<BlockStatModel> loadBlockStat(String address) {
        return Single.zip(
                TronNetwork.getInstance().getAccountMedia(address),
                TronNetwork.getInstance().getTransactionStats(address),
                (accountMedia, transactionStats) ->
                        new BlockStatModel(
                                address,
                                accountMedia.getImageUrl(),
                                transactionStats.getTransactionsIn(),
                                transactionStats.getTransactionsOut()
                        )
        );
    }

    private Single<List<Transfer>> loadTransferHistory(String address) {
        return TronNetwork.getInstance().getTransfers(0, 25, "-timestamp", true, address)
                .map(value -> value.getData());
    }
}
