package com.devband.tronwalletforandroid.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends CommonActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_layout)
    public LinearLayout mViewLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tron);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_about_tron);
        }

        TronNetwork.getInstance().getCoinInfo(Constants.TRON_COINMARKET_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps != null && !coinMarketCaps.isEmpty()) {
                            CoinMarketCap coinMarketCap = coinMarketCaps.get(0);
                            DecimalFormat df = new DecimalFormat("#,##0.000");
                            DecimalFormat df2 = new DecimalFormat("#,##0");

                            Date updated = new Date(Long.parseLong(coinMarketCap.getLastUpdated()) * 1_000);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Market Price : ")
                                    .append(df.format(Double.parseDouble(coinMarketCap.getPriceUsd())))
                                    .append(" USD (")
                                    .append("-".equals(coinMarketCap.getPercentChange24h().substring(0, 1)) ?
                                            coinMarketCap.getPercentChange24h() :
                                            "+" + coinMarketCap.getPercentChange24h()
                                    ).append(")");

                            View aboutPage = new AboutPage(AboutActivity.this)
                                    .isRTL(false)
                                    .setDescription("TRON is an ambitious project dedicated to building the infrastructure for a truly decentralized Internet. The Tron Protocol, one of the largest blockchain based operating systems in the world, offers scalable, high-availability and high-throughput support that underlies all the decentralized applications in the TRON ecosystem.\n" +
                                            "\n" +
                                            "TRON Protocol and the TVM allow anyone to develop DAPPs for themselves or their communities, with smart contracts making decentralized crowdfunding and token issuance easier than ever. Tron DAPP projects already include Peiwo, Obike, Uplive, game.com, Kitty live and Mico, with 100M+ active users from more than 100 countries and regions around the world.")
                                    .addGroup("Tron info (" + sdf.format(updated) + ")")
                                    .addItem(new Element().setTitle("Name : TRON"))
                                    .addItem(new Element().setTitle("Symbol : TRX"))
                                    .addItem(new Element().setTitle("Total Supply : " + df2.format(Double.parseDouble(coinMarketCap.getTotalSupply())) + " TRX"))
                                    .addItem(new Element().setTitle("Supply : " + df2.format(Double.parseDouble(coinMarketCap.getAvailableSupply())) + " TRX"))
                                    .addItem(new Element().setTitle(sb.toString()))
                                    .addItem(new Element().setTitle("Market Rank : " + coinMarketCap.getRank()))
                                    .addItem(new Element().setTitle("Market Cap : " + df.format(Double.parseDouble(coinMarketCap.getMarketCapUsd())) + " USD"))
                                    .addItem(new Element().setTitle("24H Volume : " + df.format(Double.parseDouble(coinMarketCap.get_24hVolumeUsd())) + " USD"))
                                    .addGroup("Connect with tron team")
                                    .addWebsite("https://tron.network")
                                    .addFacebook("tronfoundation")
                                    .addTwitter("Tronfoundation")
                                    .addGitHub("tronprotocol")
                                    .create();

                            mViewLayout.addView(aboutPage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
