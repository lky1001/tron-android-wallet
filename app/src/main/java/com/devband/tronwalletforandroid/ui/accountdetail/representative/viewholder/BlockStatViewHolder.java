package com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.BlockStatModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 29..
 */

public class BlockStatViewHolder extends BaseViewHolder<BlockStatModel> {

    @BindView(R.id.area_represent_image)
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

        Glide.with(itemView.getContext())
                .load(imagePath)
                .apply(new RequestOptions().placeholder(R.drawable.ic_default_image).error(R.drawable.ic_default_image))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (mImgRepresent.getVisibility() != View.VISIBLE) {
                            mImgRepresent.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                })
                .into(mImgRepresent);
    }

    private void bindStatInfo(BlockStatModel model) {
        mTxtAddress.setText(model.getAddress());
        long in = model.getTransactionIn();
        long out = model.getTransactionOut();
        mTxtStatsIn.setText(in <= 0L ? "0" : String.valueOf(in));
        mTxtStatsOut.setText(out <= 0L ? "0" : String.valueOf(out) + "");
    }
}
