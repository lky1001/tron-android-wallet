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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronlib.dto.Token;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.thefinestartist.finestwebview.FinestWebView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> implements AdapterDataModel<Token>, AdapterView {

    private List<Token> mList;

    private Context mContext;

    private DecimalFormat df = new DecimalFormat("#,##0");
    private DecimalFormat percentDf = new DecimalFormat("#,##0.00");

    public TokenAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.mContext = context;
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_token, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
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
        holder.tokenSaleText.setText(df.format(item.getIssued()));
        holder.tokenSupplyText.setText(df.format(item.getTotalSupply()));
        holder.tokenSalePercentText.setText(percentDf.format(item.getPercentage()) + "%");
        holder.tokenSaleProgress.setMax(100f);
        holder.tokenSaleProgress.setProgress((float) item.getPercentage());
        holder.visitWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title(R.string.visit_external_site_text)
                        .content(item.getUrl() + " " + mContext.getString(R.string.external_website_warning_msg))
                        .titleColorRes(android.R.color.black)
                        .contentColorRes(android.R.color.black)
                        .backgroundColorRes(android.R.color.white)
                        .positiveText(R.string.visit_site_text)
                        .negativeText(R.string.cancen_text)
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            new FinestWebView.Builder(mContext).show(item.getUrl());
                        }).show();
            }
        });
        if (item.getIssued() == item.getTotalSupply()) {
            holder.participateButton.setBackgroundResource(R.color.token_finish_button_color);
            holder.participateButton.setText(R.string.finish_btn_text);
            holder.participateButton.setEnabled(false);
        } else {
            holder.participateButton.setBackgroundResource(R.color.token_participate_button_color);
            holder.participateButton.setText(R.string.participate_btn_text);
            holder.participateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Coming soon", Toast.LENGTH_SHORT).show();
                }
            });
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
            if (t.getId().equalsIgnoreCase(token.getId())) {
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
