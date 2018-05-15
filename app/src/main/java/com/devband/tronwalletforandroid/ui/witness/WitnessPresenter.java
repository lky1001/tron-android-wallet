package com.devband.tronwalletforandroid.ui.witness;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.witness.dto.Witness;
import com.devband.tronwalletforandroid.ui.witness.dto.WitnessList;

import org.tron.protos.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WitnessPresenter extends BasePresenter<WitnessView> {

    private AdapterDataModel<Witness> mAdapterDataModel;

    public WitnessPresenter(WitnessView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<Witness> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        mView.showLoadingDialog();
        getWitnessNodeList();
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

    public void getWitnessNodeList() {
        Single.fromCallable(() -> Tron.getInstance(mContext).getWitnessList().blockingGet())
        .map(witnessList -> {
            List<Witness> witnesses = new ArrayList<>();

            int cnt = witnessList.getWitnessesCount();

            long highestVotes = 0;

            for (int i = 0; i < cnt; i++) {
                Protocol.Witness witness = witnessList.getWitnesses(i);

                witnesses.add(Witness.builder()
                        .url(witness.getUrl())
                        .latestBlockNum(witness.getLatestBlockNum())
                        .totalProduced(witness.getTotalProduced())
                        .totalMissed(witness.getTotalMissed())
                        .voteCount(witness.getVoteCount())
                        .build());

                if (witness.getVoteCount() > highestVotes) {
                    highestVotes = witness.getVoteCount();
                }
            }

            Descending descending = new Descending();
            Collections.sort(witnesses, descending);

            return WitnessList.builder()
                    .witnessList(witnesses)
                    .witnessCount(cnt)
                    .highestVotes(highestVotes)
                    .build();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<WitnessList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(WitnessList witnessList) {
                mAdapterDataModel.addAll(witnessList.getWitnessList());
                mView.displayWitnessInfo(witnessList.getWitnessCount(), witnessList.getHighestVotes());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    class Descending implements Comparator<Witness> {

        @Override
        public int compare(Witness o1, Witness o2) {
            return o2.getVoteCount().compareTo(o1.getVoteCount());
        }
    }
}
