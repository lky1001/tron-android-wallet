package com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.TransferHistoryModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

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

    @BindView(R.id.copy_from_address)
    View mViewFromAddress;
    @BindView(R.id.copy_to_address)
    View mViewToAddress;

    public TransferHistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TransferHistoryModel model) {
        Transfer transfer = model.getTransfer();

        mTxtFromAddress.setText(transfer.getTransferFromAddress());
        mTxtToAddress.setText(transfer.getTransferToAddress());

        StringBuilder sb = new StringBuilder(Utils.getRealTrxFormat(transfer.getAmount()) + "")
                .append(" ").append(transfer.getTokenName());
        mTxtAmout.setText(sb.toString());

        mViewFromAddress.setOnClickListener(view -> {
            copyToClipboard(transfer.getTransferFromAddress());
            Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.copy_to_address_msg),
                    Toast.LENGTH_SHORT)
                    .show();
        });

        mViewToAddress.setOnClickListener(view -> {
            copyToClipboard(transfer.getTransferFromAddress());
            Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.copy_to_address_msg),
                    Toast.LENGTH_SHORT)
                    .show();
        });
    }

    private void copyToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", content);
        clipboard.setPrimaryClip(clip);
    }
}
