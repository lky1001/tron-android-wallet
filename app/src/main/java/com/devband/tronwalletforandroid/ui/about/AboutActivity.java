package com.devband.tronwalletforandroid.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import mehdi.sakout.aboutpage.AboutPage;

public class AboutActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("TRON is an ambitious project dedicated to building the infrastructure for a truly decentralized Internet. The Tron Protocol, one of the largest blockchain based operating systems in the world, offers scalable, high-availability and high-throughput support that underlies all the decentralized applications in the TRON ecosystem.\n" +
                        "\n" +
                        "TRON Protocol and the TVM allow anyone to develop DAPPs for themselves or their communities, with smart contracts making decentralized crowdfunding and token issuance easier than ever. Tron DAPP projects already include Peiwo, Obike, Uplive, game.com, Kitty live and Mico, with 100M+ active users from more than 100 countries and regions around the world.")
                .setImage(R.drawable.ic_tron_red)
                .addGroup("Connect with us")
                .addWebsite("https://tron.network")
                .addFacebook("tronfoundation")
                .addTwitter("Tronfoundation")
                .addGitHub("tronprotocol")
                .create();

        setContentView(aboutPage);
    }
}
