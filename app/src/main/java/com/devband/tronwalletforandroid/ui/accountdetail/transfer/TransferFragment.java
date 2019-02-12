package com.devband.tronwalletforandroid.ui.accountdetail.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.adapter.AccountTransferAdapter;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferFragment extends CommonFragment implements TransferView {

    @Inject
    TransferPresenter mTransferPresenter;
    
    private String mAddress;

    private static final int PAGE_SIZE = 25;

    @BindView(R.id.recycler_view)
    RecyclerView mListView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private AccountTransferAdapter mAdapter;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    public static Fragment newInstance(@NonNull String address) {
        TransferFragment fragment = new TransferFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, address);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        ButterKnife.bind(this, view);
        initUi();

        mAddress = getArguments().getString(AccountDetailActivity.EXTRA_ADDRESS);

        mTransferPresenter.setAdapterDataModel(mAdapter);
        mTransferPresenter.onCreate();

        return view;
    }

    private void initUi() {
        mAdapter = new AccountTransferAdapter(getActivity(), mOnItemClickListener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mListView.setLayoutManager(mLayoutManager);
        mListView.addItemDecoration(new DividerItemDecoration(0));
        mListView.setAdapter(mAdapter);
        mListView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mAdapterView = mAdapter;
    }

    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mIsLoading && !mIsLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    mIsLoading = true;
                    mTransferPresenter.getTransfers(mAddress, mStartIndex, PAGE_SIZE);
                }
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mListView.getChildLayoutPosition(v);
            TransferInfo item = mAdapter.getItem(pos);

            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title(R.string.title_transfer_text)
                    .titleColorRes(android.R.color.black)
                    .contentColorRes(android.R.color.black)
                    .backgroundColorRes(android.R.color.white)
                    .customView(R.layout.dialog_transfer, false)
                    .positiveText(R.string.close_text);

            MaterialDialog dialog = builder.build();

            TextView hashText = (TextView) dialog.getCustomView().findViewById(R.id.hash_text);
            TextView blockText = (TextView) dialog.getCustomView().findViewById(R.id.block_text);
            TextView sendText = (TextView) dialog.getCustomView().findViewById(R.id.send_address_text);
            TextView toText = (TextView) dialog.getCustomView().findViewById(R.id.to_address_text);
            TextView statusText = (TextView) dialog.getCustomView().findViewById(R.id.status_text);
            TextView amountText = (TextView) dialog.getCustomView().findViewById(R.id.amount_text);
            TextView dateText = (TextView) dialog.getCustomView().findViewById(R.id.date_text);


            long amount = item.getAmount();

            if (item.getTokenName().equalsIgnoreCase(Constants.TRON_SYMBOL)) {
                amount = (long) (amount / Constants.ONE_TRX);
            }

            hashText.setText(item.getHash());
            blockText.setText(Constants.numberFormat.format(item.getBlock()));
            sendText.setText(item.getTransferFromAddress());
            toText.setText(item.getTransferToAddress());
            statusText.setText(item.isConfirmed() ? getString(R.string.confirmed_text) : getString(R.string.unconfirmed_text));
            amountText.setText(Constants.tokenBalanceFormat.format(amount) + " " + item.getTokenName());
            dateText.setText(Constants.sdf.format(new Date(item.getTimestamp())));

            dialog.show();
        }
    };

    @Override
    protected void refresh() {
        if (!mIsLastPage && isAdded()) {
            mTransferPresenter.getTransfers(mAddress, mStartIndex, PAGE_SIZE);
        }
    }

    @Override
    public void finishLoading(long total) {
        if (!isAdded()) {
            return;
        }

        boolean isFirstLoading = mStartIndex == 0;

        mStartIndex += PAGE_SIZE;

        if (mStartIndex >= total) {
            mIsLastPage = true;
        }

        mIsLoading = false;

        mAdapterView.refresh();

        if (isFirstLoading && total == 0) {
            mListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        } else {
            if (mListView.getVisibility() != View.VISIBLE) {
                mListView.setVisibility(View.VISIBLE);
            }
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                mEmptyView.setVisibility(View.GONE);
            }
        }

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (!isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
