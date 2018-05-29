package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Block;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.blockdetail.BlockDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockOverviewFragment extends BaseFragment implements BlockInfoView {
    public static BaseFragment newInstance(@NonNull long blockNumber) {
        BlockOverviewFragment fragment = new BlockOverviewFragment();
        Bundle args = new Bundle(1);
        args.putLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, blockNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.txt_status)
    TextView mTxtStatus;

    @BindView(R.id.txt_hash)
    TextView mTxtHash;

    @BindView(R.id.txt_height)
    TextView mTxtBlockHeight;

    @BindView(R.id.txt_timestamp)
    TextView mTxtTimestamp;

    @BindView(R.id.txt_transaction)
    TextView mTxtTransaction;

    @BindView(R.id.txt_parent_hash)
    TextView mTxtParentHash;

    @BindView(R.id.txt_witness_address)
    TextView mTxtWitnessAddress;

    @BindView(R.id.txt_block_size)
    TextView mTxtBlockSize;

    private long mBlockNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_info, container, false);
        ButterKnife.bind(this, view);

        mBlockNumber = getArguments().getLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, 0l);
        if (mBlockNumber == 0l) {
            getActivity().finish();
        }

        mPresenter = new BlockInfoPresenter(this);
        mPresenter.onCreate();

        ((BlockInfoPresenter) mPresenter).getBlock(mBlockNumber);

        return view;
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishLoading(@NonNull Block block) {
        hideDialog();
        mTxtStatus.setText(block.isConfirmed() ? "CONFIRMED" : "UNCONFIRMED");
        mTxtHash.setText(block.getHash());
        mTxtBlockHeight.setText(mBlockNumber + "");
        mTxtTimestamp.setText(block.getTimestamp() + "");
        mTxtTransaction.setText(block.getNrOfTrx() + "");
        mTxtParentHash.setText(block.getParentHash());
        mTxtWitnessAddress.setText(block.getWitnessAddress());
        mTxtBlockSize.setTag(block.getSize());
    }

    @Override
    protected void refresh() {
    }
}
