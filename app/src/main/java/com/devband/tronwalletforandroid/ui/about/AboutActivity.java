package com.devband.tronwalletforandroid.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CoinMarketCapService coinMarketCapService = ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
        coinMarketCapService.getPrice("tronix")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps != null && !coinMarketCaps.isEmpty()) {
                            Element price = new Element()
                                    .setTitle("Price : " + coinMarketCaps.get(0).getPriceUsd());

                            View aboutPage = new AboutPage(AboutActivity.this)
                                    .isRTL(false)
                                    .setDescription("TRON is an ambitious project dedicated to building the infrastructure for a truly decentralized Internet. The Tron Protocol, one of the largest blockchain based operating systems in the world, offers scalable, high-availability and high-throughput support that underlies all the decentralized applications in the TRON ecosystem.\n" +
                                            "\n" +
                                            "TRON Protocol and the TVM allow anyone to develop DAPPs for themselves or their communities, with smart contracts making decentralized crowdfunding and token issuance easier than ever. Tron DAPP projects already include Peiwo, Obike, Uplive, game.com, Kitty live and Mico, with 100M+ active users from more than 100 countries and regions around the world.")
                                    .setImage(R.drawable.ic_tron_red)
                                    .addGroup("Tron info")
                                    .addItem(price)
                                    .addGroup("Connect with tron team")
                                    .addWebsite("https://tron.network")
                                    .addFacebook("tronfoundation")
                                    .addTwitter("Tronfoundation")
                                    .addGitHub("tronprotocol")
                                    .create();

                            setContentView(aboutPage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
