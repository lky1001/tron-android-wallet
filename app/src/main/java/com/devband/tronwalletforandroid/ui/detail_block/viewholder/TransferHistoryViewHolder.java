package com.devband.tronwalletforandroid.ui.detail_block.viewholder;

import android.view.View;
import android.widget.TextView;

import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.detail_block.model.TransferHistoryModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 29..
 */

public class TransferHistoryViewHolder extends BaseViewHolder<TransferHistoryModel> {

    @BindView(R.id.from_address_text)
    TextView mTxtFromAddress;
    @BindView(R.id.to_address_text)
    TextView mTxtToAddress;
    @BindView(R.id.txt_amout)
    TextView mTxtAmout;

    public TransferHistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TransferHistoryModel model) {
        Transfer transfer = model.getTransfer();

        mTxtFromAddress.setText(transfer.getTransferFromAddress());
        mTxtToAddress.setText(transfer.getTransferToAddress());

        StringBuilder sb = new StringBuilder(transfer.getAmount() + "")
                .append(" ").append(transfer.getTokenName());
        mTxtAmout.setText(sb.toString());
    }
}
