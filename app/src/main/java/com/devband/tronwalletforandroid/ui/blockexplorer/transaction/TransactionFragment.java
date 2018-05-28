package com.devband.tronwalletforandroid.ui.blockexplorer.transaction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.blockexplorer.account.AccountPresenter;
import com.devband.tronwalletforandroid.ui.blockexplorer.adapter.TransactionAdapter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 24..
 */

public class TransactionFragment extends BaseFragment implements TransactionView {
    private static final int PAGE_SIZE = 25;

    @BindView(R.id.recycler_view)
    RecyclerView mListView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private TransactionAdapter mAdapter;

    private int mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    public static BaseFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, view);
        initUi();

        mPresenter = new TransactionPresenter(this);
        ((TransactionPresenter) mPresenter).setAdapterDataModel(mAdapter);
        mPresenter.onCreate();

        return view;
    }

    private void initUi() {
        mAdapter = new TransactionAdapter(getActivity(), mOnItemClickListener);
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
                    ((AccountPresenter) mPresenter).getTronAccounts(mStartIndex, PAGE_SIZE);
                }
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mListView.getChildLayoutPosition(v);
            Transfer item = mAdapter.getModel(pos);

            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title(R.string.title_transfer_text)
                    .titleColorRes(android.R.color.black)
                    .contentColorRes(android.R.color.black)
                    .backgroundColorRes(android.R.color.white)
                    .customView(R.layout.dialog_transaction, false)
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

            if (!TextUtils.isEmpty(item.getTokenName()) && item.getTokenName().equalsIgnoreCase(Constants.TRON_SYMBOL)) {
                amount = (long) (amount / Constants.ONE_TRX);
            }

            hashText.setText(item.getHash());
            blockText.setText(Constants.numberFormat.format(item.getBlock()));
            sendText.setText(item.getTransferFromAddress());
            toText.setText(item.getTransferToAddress());
            statusText.setText(item.isConfirmed() ? getString(R.string.confirmed_text) : getString(R.string.unconfirmed_text));
            amountText.setText(Constants.tronBalanceFormat.format(amount) + " " + item.getTokenName());
            dateText.setText(Constants.sdf.format(new Date(item.getTimestamp())));

            dialog.show();
        }
    };

    @Override
    protected void refresh() {
        if (!mIsLastPage) {
            ((TransactionPresenter) mPresenter).getTransactions(mStartIndex, PAGE_SIZE);
        }
    }

    @Override
    public void finishLoading(long total) {
        mStartIndex += PAGE_SIZE;

        if (mStartIndex >= total) {
            mIsLastPage = true;
        }

        mIsLoading = false;
        mAdapterView.refresh();

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        mIsLoading = false;
        hideDialog();
        Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }
}
