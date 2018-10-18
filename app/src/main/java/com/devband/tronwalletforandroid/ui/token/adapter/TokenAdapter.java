package com.devband.tronwalletforandroid.ui.token.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronlib.dto.Token;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> implements AdapterDataModel<Token>, AdapterView {

    private List<Token> mList;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    private View.OnClickListener mOnParticipateClickListener;

    public TokenAdapter(Context context, View.OnClickListener onItemClickListener, View.OnClickListener onParticipateClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnParticipateClickListener = onParticipateClickListener;
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_token, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TokenViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {
        Token item = mList.get(position);

        if (position == 0) {
            holder.marginView.setVisibility(View.GONE);
        } else {
            holder.marginView.setVisibility(View.VISIBLE);
        }

        holder.tokenSymbolImage.setVisibility(View.GONE);
        holder.tokenNameText.setText(item.getName());
        holder.tokenDescText.setText(item.getDescription());
        holder.tokenSaleText.setText(Constants.numberFormat.format(item.getIssued()));
        holder.tokenSupplyText.setText(Constants.numberFormat.format(item.getTotalSupply()));
        holder.tokenSalePercentText.setText(Constants.percentFormat.format(item.getIssuedPercentage()) + "%");
        holder.tokenSaleProgress.setMax(100f);
        holder.tokenSaleProgress.setProgress((float) item.getIssuedPercentage());
        holder.tokenEndText.setText(mContext.getString(R.string.ends_text) + " " + Utils.getDateTimeWithTimezone(item.getEndTime()));
        holder.visitWebsiteButton.setOnClickListener(view -> {
            new MaterialDialog.Builder(mContext)
                    .title(R.string.visit_external_site_text)
                    .content(item.getUrl() + " " + mContext.getString(R.string.external_website_warning_msg))
                    .titleColorRes(android.R.color.black)
                    .contentColorRes(android.R.color.black)
                    .backgroundColorRes(android.R.color.white)
                    .positiveText(R.string.visit_site_text)
                    .negativeText(R.string.cancel_text)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                        new FinestWebView.Builder(mContext).show(item.getUrl());
                    }).show();
        });

        holder.participateButton.setEnabled(false);
        holder.participateButton.setTag(null);

        if (item.getIssued() == item.getTotalSupply()
                || Calendar.getInstance().getTimeInMillis() > item.getEndTime()) {
            holder.participateButton.setBackgroundResource(R.color.token_finished_button_color);
            holder.participateButton.setText(R.string.finished_btn_text);
        } else if (item.getStartTime() > Calendar.getInstance().getTimeInMillis()) {
            holder.participateButton.setBackgroundResource(R.color.token_preparing_button_color);
            holder.participateButton.setText(R.string.preparing_btn_text);
        } else {
            holder.participateButton.setBackgroundResource(R.color.token_participate_button_color);
            holder.participateButton.setText(R.string.participate_btn_text);
            holder.participateButton.setEnabled(true);
            holder.participateButton.setTag(item);
            holder.participateButton.setOnClickListener(mOnParticipateClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(Token model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<Token> list) {
        for (Token token : list) {
            if (!isContain(token)) {
                mList.add(token);
            }
        }
    }

    private boolean isContain(Token token) {
        for (Token t : mList) {
            if (t.getOwnerAddress().equalsIgnoreCase(token.getOwnerAddress())
                    && t.getName().equalsIgnoreCase(token.getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public Token getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    class TokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.margin_view)
        View marginView;

        @BindView(R.id.token_symbol_image)
        ImageView tokenSymbolImage;

        @BindView(R.id.token_name_text)
        TextView tokenNameText;

        @BindView(R.id.visit_website_button)
        ImageView visitWebsiteButton;

        @BindView(R.id.token_desc_text)
        TextView tokenDescText;

        @BindView(R.id.token_sale_text)
        TextView tokenSaleText;

        @BindView(R.id.token_supply_text)
        TextView tokenSupplyText;

        @BindView(R.id.token_sale_percent_text)
        TextView tokenSalePercentText;

        @BindView(R.id.token_end_text)
        TextView tokenEndText;

        @BindView(R.id.progress_token_sales)
        RoundCornerProgressBar tokenSaleProgress;

        @BindView(R.id.btn_participate)
        Button participateButton;

        public TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
