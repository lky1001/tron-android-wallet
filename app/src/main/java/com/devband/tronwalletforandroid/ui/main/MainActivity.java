package com.devband.tronwalletforandroid.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterView;
import com.devband.tronwalletforandroid.ui.main.adapter.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.main.adapter.MyTokenListAdapter;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;
import com.devband.tronwalletforandroid.ui.requestcoin.RequestCoinActivity;
import com.devband.tronwalletforandroid.ui.sendcoin.SendCoinActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.tron.protos.Protocol;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CommonActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_FROM_DONATIONS = "from_donations";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.nav_view)
    NavigationView mSideMenu;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.login_wallet_name_text)
    TextView mMainTitleText;

    @BindView(R.id.login_wallet_balance_text)
    TextView mLoginWalletBalanceText;

    @BindView(R.id.login_wallet_price_text)
    TextView mLoginWalletPriceText;

    @BindView(R.id.price_help_image)
    ImageView mPriceHelpImage;

    @BindView(R.id.edit_wallet_name_image)
    ImageView mEditWalletNameImage;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @BindView(R.id.no_token_layout)
    LinearLayout mNoTokenLayout;

    @BindView(R.id.my_token_listview)
    RecyclerView mMyTokenListView;

    FloatingActionButton mFloatingActionMenuRequestCoin;

    FloatingActionButton mFloatingActionMenuSendCoin;

    Spinner mWalletSpinner;

    TextView mNavHeaderText;

    String mLoginWalletName;

    private Protocol.Account mLoginTronAccount;

    private CoinMarketCap mCoinMarketCapPriceInfo;

    private ArrayAdapter<String> mWalletAdapter;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private MyTokenListAdapter mMyTokenListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupDrawerLayout();

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        mFloatingActionMenuRequestCoin = (FloatingActionButton) mFloatingActionMenu.findViewById(R.id.fab_menu_request_coin);
        mFloatingActionMenuSendCoin = (FloatingActionButton) mFloatingActionMenu.findViewById(R.id.fab_menu_send_coin);

        mFloatingActionMenuRequestCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RequestCoinActivity.class);
            }
        });

        mFloatingActionMenuSendCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QrScanActivity.class);
                intent.putExtra(QrScanActivity.EXTRA_FROM_TRON_PAY_MENU, true);
                startActivity(intent);
            }
        });

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyTokenListView.setLayoutManager(mLayoutManager);
        mMyTokenListView.addItemDecoration(new DividerItemDecoration(0));
        mMyTokenListView.setNestedScrollingEnabled(false);

        mMyTokenListAdapter = new MyTokenListAdapter(MainActivity.this);
        mMyTokenListView.setAdapter(mMyTokenListAdapter);
        mAdapterView = mMyTokenListAdapter;

        mPresenter = new MainPresenter(this);
        ((MainPresenter) mPresenter).setAdapterDataModel(mMyTokenListAdapter);
        mPresenter.onCreate();

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(mLoginWalletName);
                    mMainTitleText.setVisibility(View.GONE);
                    mLoginWalletBalanceText.setVisibility(View.GONE);
                    mLoginWalletPriceText.setVisibility(View.GONE);
                    mPriceHelpImage.setVisibility(View.GONE);
                    mEditWalletNameImage.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mMainTitleText.setVisibility(View.VISIBLE);
                    mLoginWalletBalanceText.setVisibility(View.VISIBLE);
                    mLoginWalletPriceText.setVisibility(View.VISIBLE);
                    mPriceHelpImage.setVisibility(View.VISIBLE);
                    mEditWalletNameImage.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    private void setupDrawerLayout() {
        mSideMenu.inflateMenu(R.menu.navigation_menu);

        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, mSideMenu);

        mNavHeaderText = (TextView) header.findViewById(R.id.headerTitleText);
        mWalletSpinner = header.findViewById(R.id.wallet_spinner);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawer.addDrawerListener(toggle);

        toggle.syncState();

        mSideMenu.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoginState();
    }

    private void checkLoginState() {
        if (((MainPresenter) mPresenter).isLogin()) {
            if(mSideMenu.getMenu() != null) {
                mSideMenu.getMenu().clear();
                mSideMenu.inflateMenu(R.menu.navigation_logged_in_menu);
            }

            // get account info
            ((MainPresenter) mPresenter).getMyAccountInfo();

            String loginWalletName = ((MainPresenter) mPresenter).getLoginWalletName();

            if (loginWalletName == null) {
                mNavHeaderText.setText(R.string.navigation_header_title);
            } else {
                mLoginWalletName = loginWalletName;
                mNavHeaderText.setText(mLoginWalletName);
                mMainTitleText.setText(mLoginWalletName);

                mWalletAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,
                    new String[] {
                        loginWalletName
                });

                mWalletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mWalletSpinner.setAdapter(mWalletAdapter);
                mWalletSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
            }
        } else {
            finishActivity();
            startActivity(LoginActivity.class);
//            if(mSideMenu.getMenu() != null) {
//                mSideMenu.getMenu().clear();
//                mSideMenu.inflateMenu(R.menu.navigation_menu);
//            }
//
//            // hide address ic_qrcode_white_24dp, send actionbar item
//            mBeforeLoginLayout.setVisibility(View.VISIBLE);
//            mAfterLoginLayout.setVisibility(View.GONE);
//
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_wallet:
                break;
            case R.id.action_import_wallet:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_item_my_address:
                startActivity(AddressActivity.class);
                break;
            case R.id.drawer_item_send_tron:
                startActivity(SendCoinActivity.class);
                break;
            case R.id.drawer_item_export_private_key:
                sharePrivateKey();
                break;
            case R.id.drawer_item_donations:
                Intent intent = new Intent(MainActivity.this, SendCoinActivity.class);
                intent.putExtra(EXTRA_FROM_DONATIONS, true);
                startActivity(intent);
                break;
            case R.id.drawer_item_more:
                 startActivity(MoreActivity.class);
                break;
        }
        return false;
    }

    public void logout() {
        ((MainPresenter) mPresenter).logout();
        checkLoginState();
    }

    @Override
    public void displayAccountInfo(@NonNull Protocol.Account account) {
        mLoginTronAccount = account;

        if (mLoginTronAccount.getAssetMap().isEmpty()) {
            mNoTokenLayout.setVisibility(View.VISIBLE);
            mMyTokenListView.setVisibility(View.GONE);
        } else {
            mNoTokenLayout.setVisibility(View.GONE);
            mMyTokenListView.setVisibility(View.VISIBLE);
        }

        Log.i(MainActivity.class.getSimpleName(), "address : " + account.getAddress().toStringUtf8());
        Log.i(MainActivity.class.getSimpleName(), "balance : " + account.getBalance() + "trx");
        double balance = ((double) account.getBalance()) / Constants.REAL_TRX_AMOUNT;
        DecimalFormat df = new DecimalFormat("#,##0.00000000");

        mLoginWalletBalanceText.setText(df.format(balance) + " " + getString(R.string.currency_text));

        ((MainPresenter) mPresenter).getTronMarketInfo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setTronMarketInfo(CoinMarketCap coinMarketCap) {
        if (mLoginTronAccount != null) {
            double balance = ((double) mLoginTronAccount.getBalance()) / Constants.REAL_TRX_AMOUNT;
            DecimalFormat df = new DecimalFormat("#,##0.000");

            mLoginWalletPriceText.setText(df.format(balance * Double.parseDouble(coinMarketCap.getPriceUsd()))
                    + " " + getString(R.string.price_text));

            mCoinMarketCapPriceInfo = coinMarketCap;

            mPriceHelpImage.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({ R.id.login_wallet_price_layout })
    public void onPriceHelpImageClick() {
        StringBuilder sb = new StringBuilder();

        Date updated = new Date(Long.parseLong(mCoinMarketCapPriceInfo.getLastUpdated()) * 1_000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);

        sb.append("Price : ")
                .append(mCoinMarketCapPriceInfo.getPriceUsd())
                .append(" USD (")
                .append("-".equals(mCoinMarketCapPriceInfo.getPercentChange24h().substring(0, 1)) ?
                        mCoinMarketCapPriceInfo.getPercentChange24h() :
                        "+" + mCoinMarketCapPriceInfo.getPercentChange24h()
                )
                .append("%)\nLast updated : ")
                .append(sdf.format(updated))
                .append("(UTC)\nFrom CoinMarketCap");

        hideDialog();

        new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.tron_price_title))
                .content(sb.toString())
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .autoDismiss(true)
                .build()
                .show();
    }

    @OnClick(R.id.edit_wallet_name_image)
    public void onEditWalletNameImageClick() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_rename_wallet)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputRangeRes(2, 20, android.R.color.white)
                .input(getString(R.string.rename_wallet_hint), mLoginWalletName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String walletName = input.toString();

                        if (!TextUtils.isEmpty(walletName)) {
                            if (((MainPresenter) mPresenter).renameWallet(walletName)) {
                                checkLoginState();
                            }
                        }
                    }
                }).show();
    }

    private void sharePrivateKey() {
        String privateKey = Tron.getInstance(MainActivity.this).getPrivateKey();

        Log.d(MainActivity.class.getSimpleName(), privateKey);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, privateKey);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_private_key)));
    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            String selectedAccount = mWalletAdapter.getItem(pos);
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
