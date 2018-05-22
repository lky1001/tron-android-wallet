package com.devband.tronwalletforandroid.ui.vote;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItem;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItemList;

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

public class VotePresenter extends BasePresenter<VoteView> {

    private AdapterDataModel<VoteItem> mAdapterDataModel;

    public VotePresenter(VoteView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<VoteItem> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        getRepresentativeList();
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

    public void getRepresentativeList() {
        mView.showLoadingDialog();

        Single.fromCallable(() -> Tron.getInstance(mContext).getWitnessList().blockingGet())
        .map(witnessList -> {
            List<VoteItem> representatives = new ArrayList<>();

            int cnt = witnessList.getWitnessesCount();

            long totalVotes = 0;
            long totalMyVotes = 0;

            Protocol.Account myAccount = Tron.getInstance(mContext).queryAccount(Tron.getInstance(mContext).getLoginAddress()).blockingGet();

            for (int i = 0; i < cnt; i++) {
                Protocol.Witness witness = witnessList.getWitnesses(i);

                long myVoteCount = 0;

                for (Protocol.Vote vote : myAccount.getVotesList()) {
                    if (AccountManager.encode58Check(vote.getVoteAddress().toByteArray()).equals(AccountManager.encode58Check(witness.getAddress().toByteArray()))) {
                        myVoteCount = vote.getVoteCount();
                        totalMyVotes += myVoteCount;
                        break;
                    }
                }

                representatives.add(VoteItem.builder()
                        .address(AccountManager.encode58Check(witness.getAddress().toByteArray()))
                        .url(witness.getUrl())
                        .voteCount(witness.getTotalVoteCount())
                        .myVoteCount(myVoteCount)
                        .build());

                totalVotes += witness.getTotalVoteCount();
            }

            Descending descending = new Descending();
            Collections.sort(representatives, descending);

            for (int i = 0; i < cnt; i++) {
                VoteItem representative = representatives.get(i);
                representative.setNo(i + 1);
                representative.setTotalVoteCount(totalVotes);
            }

            long myVotePoint = 0;

            for (Protocol.Account.Frozen frozen : myAccount.getFrozenList()) {
                myVotePoint += frozen.getFrozenBalance();
            }

            myVotePoint = (long) (myVotePoint / Constants.REAL_TRX_AMOUNT);

            return VoteItemList.builder()
                    .voteItemList(representatives)
                    .voteItemCount(cnt)
                    .totalVotes(totalVotes)
                    .totalMyVotes(totalMyVotes)
                    .myVotePoint(myVotePoint)
                    .build();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<VoteItemList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(VoteItemList voteItemList) {
                mView.displayVoteInfo(voteItemList.getTotalVotes(), voteItemList.getVoteItemCount(),
                        voteItemList.getMyVotePoint(), voteItemList.getTotalMyVotes());
                mAdapterDataModel.clear();
                mAdapterDataModel.addAll(voteItemList.getVoteItemList());
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public void showOnlyMyVotes(boolean isMyVotes) {
        if (isMyVotes) {

        } else {
            
        }
    }

    public boolean matchPassword(@NonNull String password) {
        return WalletAppManager.getInstance(mContext).login(password) == WalletAppManager.SUCCESS;
    }

    public void voteRepresentative(String address, long vote) {
        mView.showLoadingDialog();

        Tron.getInstance(mContext).voteWitness(address, vote)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successVote();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.showServerError();
            }
        });
    }

    class Descending implements Comparator<VoteItem> {

        @Override
        public int compare(VoteItem o1, VoteItem o2) {
            return o2.getVoteCount().compareTo(o1.getVoteCount());
        }
    }
}
