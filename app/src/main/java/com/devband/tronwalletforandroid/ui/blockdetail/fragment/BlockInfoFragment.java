package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Block;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.blockdetail.BlockDetailActivity;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockInfoFragment extends CommonFragment implements BlockInfoView {

    @Inject
    BlockInfoPresenter mBlockInfoPresenter;

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

    public static Fragment newInstance(@NonNull long blockNumber) {
        BlockInfoFragment fragment = new BlockInfoFragment();
        Bundle args = new Bundle(1);
        args.putLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, blockNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_info, container, false);
        ButterKnife.bind(this, view);

        mBlockNumber = getArguments().getLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, 0L);
        if (mBlockNumber == 0L) {
            getActivity().finish();
        }

        mBlockInfoPresenter.onCreate();
        mBlockInfoPresenter.getBlock(mBlockNumber);

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
        if (!isAdded()) {
            return;
        }
        hideDialog();
        if (block.isConfirmed()) {
            mTxtStatus.setText(R.string.confirmed);
            mTxtStatus.setTextColor(getResources().getColor(R.color.confirm_text_color));
        } else {
            mTxtStatus.setText(R.string.unconfirmed);
            mTxtStatus.setTextColor(getResources().getColor(R.color.unconfirm_text_color));
        }
        mTxtHash.setText(block.getHash());
        mTxtBlockHeight.setText("#"+ Utils.getCommaNumber(mBlockNumber));
        mTxtTimestamp.setText(Constants.sdf.format(new Date(block.getTimestamp())));
        mTxtTransaction.setText(block.getNrOfTrx() + "");
        mTxtParentHash.setText(block.getParentHash());
        mTxtWitnessAddress.setText(block.getWitnessAddress());
        mTxtBlockSize.setText(Utils.getCommaNumber(block.getSize()) + " " + getString(R.string.block_size_bytes));
    }

    @Override
    protected void refresh() {
    }
}
