package com.devband.tronwalletforandroid.ui.detail_block.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.detail_block.model.BlockStatModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 29..
 */

public class BlockStatViewHolder extends BaseViewHolder<BlockStatModel> {

    @BindView(R.id.area_image_center)
    View mAreaRepresent;
    @BindView(R.id.img_represent)
    ImageView mImgRepresent;

    @BindView(R.id.txt_address_value)
    TextView mTxtAddress;
    @BindView(R.id.txt_stats_in)
    TextView mTxtStatsIn;
    @BindView(R.id.txt_stats_out)
    TextView mTxtStatsOut;

    public BlockStatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(BlockStatModel model) {
        bindRepresendImage(model.getMediaUrl());
        bindStatInfo(model);
    }

    private void bindRepresendImage(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            mAreaRepresent.setVisibility(View.GONE);
            return;
        }
    }

    private void bindStatInfo(BlockStatModel model) {
        mTxtAddress.setText(model.getAddress());
        long in = model.getTransactionIn();
        long out = model.getTransactionOut();
        mTxtStatsIn.setText(in <= 0l ? 0l + "" : in + "");
        mTxtStatsOut.setText(out <= 0l ? 0l + "" : out + "");
    }
}
