package com.devband.tronwalletforandroid.ui.blockexplorer.block;

import android.util.Log;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by user on 2018. 5. 25..
 */

public class BlockPresenter extends BasePresenter<BlockView> {

    private static final int DEFAULT_LIMIT = 15;
    private int mLimit = DEFAULT_LIMIT;
    private int mStart = 0;

    public BlockPresenter(BlockView view) {
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

    public void loadBlockData() {
        mView.showLoadingDialog();

        TronNetwork.getInstance().getBlocks(mLimit, mStart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        blocks -> {
                            mView.blockDataLoadSuccess(blocks, mStart != 0);
                            mStart += mLimit;
                        }, t -> {
                            Log.d("hanseon--", "loadBlockData fail : " + t.getMessage());
                            mView.showServerError();
                        }
                );
    }
}
