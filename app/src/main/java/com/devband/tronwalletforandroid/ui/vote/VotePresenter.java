package com.devband.tronwalletforandroid.ui.vote;

import android.support.annotation.NonNull;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Representative;
import com.devband.tronlib.dto.Witness;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItem;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItemList;

import org.tron.protos.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class VotePresenter extends BasePresenter<VoteView> {

    private AdapterDataModel<VoteItem> mAdapterDataModel;

    private List<VoteItem> mAllVotes;

    private List<VoteItem> mMyVotes;
    private Tron mTron;
    private TronNetwork mTronNetwork;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public VotePresenter(VoteView view, Tron tron, TronNetwork tronNetwork, WalletAppManager walletAppManager,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<VoteItem> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        getRepresentativeList(false);
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

    public void getRepresentativeList(boolean isMyVotes) {
        mView.showLoadingDialog();

        Single.zip(mTronNetwork.getVoteWitnesses(), mTron.queryAccount(mTron.getLoginAddress()), (votes, myAccount) -> {
            List<VoteItem> representatives = new ArrayList<>();

            int cnt = votes.getData().size();

            long totalMyVotes = 0;

            for (int i = 0; i < cnt; i++) {
                Witness witness = votes.getData().get(i);

                long myVoteCount = 0;

                for (Protocol.Vote vote : myAccount.getVotesList()) {
                    if (AccountManager.encode58Check(vote.getVoteAddress().toByteArray()).equals(witness.getAddress())) {
                        myVoteCount = vote.getVoteCount();
                        totalMyVotes += myVoteCount;
                        break;
                    }
                }

                representatives.add(VoteItem.builder()
                        .address(witness.getAddress())
                        .url(witness.getUrl())
                        .totalVoteCount(votes.getTotalVotes())
                        .lastCycleVoteCount(witness.getLastCycleVotes())
                        .realTimeVoteCount(witness.getRealTimeVotes())
                        .hasTeamPage(witness.isHasPage())
                        .votesPercentage(witness.getVotesPercentage())
                        .changeVotes(witness.getChangeVotes())
                        .myVoteCount(myVoteCount)
                        .build());
            }

            Descending descending = new Descending();
            Collections.sort(representatives, descending);

            for (int i = 0; i < cnt; i++) {
                VoteItem representative = representatives.get(i);
                representative.setNo(i + 1);
                representative.setTotalVoteCount(votes.getTotalVotes());
            }

            long myVotePoint = 0;

            for (Protocol.Account.Frozen frozen : myAccount.getFrozenList()) {
                myVotePoint += frozen.getFrozenBalance();
            }

            myVotePoint = (long) (myVotePoint / Constants.ONE_TRX);

            mAllVotes = new ArrayList<>();
            mMyVotes = new ArrayList<>();
            mAllVotes.addAll(representatives);

            for (int i = 0; i < mAllVotes.size(); i++) {
                VoteItem voteItem = mAllVotes.get(i);

                if (voteItem.getMyVoteCount() > 0) {
                    mMyVotes.add(voteItem);
                }
            }

            return VoteItemList.builder()
                    .voteItemList(representatives)
                    .voteItemCount(cnt)
                    .totalVotes(votes.getTotalVotes())
                    .totalMyVotes(totalMyVotes)
                    .myVotePoint(myVotePoint)
                    .build();
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(voteItemList -> {
            mView.displayVoteInfo(voteItemList.getTotalVotes(), voteItemList.getVoteItemCount(),
                    voteItemList.getMyVotePoint(), voteItemList.getTotalMyVotes());
            showOnlyMyVotes(isMyVotes);
        }, e -> mView.showServerError());
    }

    public void showOnlyMyVotes(boolean isMyVotes) {
        mAdapterDataModel.clear();

        if (isMyVotes) {
            mAdapterDataModel.addAll(mMyVotes);
        } else {
            mAdapterDataModel.addAll(mAllVotes);
        }

        mView.refreshList();
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void voteRepresentative(@NonNull String password, String address, long vote, boolean includeOtherVotes) {
        mView.showLoadingDialog();

        Single.fromCallable(() -> {
            Map<String, String> witness = new HashMap<>();

            long totalVote = 0;

            if (includeOtherVotes) {
                for (int i = 0; i < mMyVotes.size(); i++) {
                    VoteItem voteItem = mMyVotes.get(i);

                    if (!address.equalsIgnoreCase(voteItem.getAddress())) {
                        witness.put(voteItem.getAddress(), String.valueOf(voteItem.getMyVoteCount()));
                        totalVote += voteItem.getMyVoteCount();
                    }
                }
            }

            if (vote != 0) {
                witness.put(address, String.valueOf(vote));
                totalVote += vote;
            }

            if (totalVote < 1) {
                throw new IllegalStateException();
            }

            return witness;
        })
        .flatMap(witness -> mTron.voteWitness(password, witness))
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
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

                if (e instanceof IllegalStateException) {
                    mView.showInvalidVoteError();
                } else {
                    mView.showServerError();
                }
            }
        });
    }

    class Descending implements Comparator<VoteItem> {

        @Override
        public int compare(VoteItem o1, VoteItem o2) {
            return o2.getRealTimeVoteCount().compareTo(o1.getRealTimeVoteCount());
        }
    }
}
