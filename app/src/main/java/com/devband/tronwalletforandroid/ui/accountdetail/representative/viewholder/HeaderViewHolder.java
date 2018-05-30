package com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder;

import android.view.View;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.HeaderModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 29..
 */

public class HeaderViewHolder extends BaseViewHolder<HeaderModel> {

    @BindView(R.id.txt_header)
    TextView mTxtHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(HeaderModel model) {
        mTxtHeader.setText(model.getTitle());
    }
}
