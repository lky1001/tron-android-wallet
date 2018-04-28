package com.devband.tronwalletforandroid.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.about.AboutActivity;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.tronwalletforandroid.ui.importwallet.ImportWalletActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.sendcoin.SendCoinActivity;

import org.tron.protos.Protocol;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CommonActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static final String CREATE_WALLET = "";
    private static final String IMPORT_WALLET = "";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mSideMenu;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.main_before_login_layout)
    LinearLayout mBeforeLoginLayout;

    @BindView(R.id.main_after_login_layout)
    LinearLayout mAfterLoginLayout;

    @BindView(R.id.tv_balance)
    TextView mBalanceText;

    Spinner mWalletSpinner;

    TextView mNavHeaderText;

    private MenuItem mMenuAddressItem;

    private MenuItem mMenuTronPayItem;

    private ArrayAdapter<String> mWalletAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupDrawerLayout();

        mPresenter = new MainPresenter(this);
        mPresenter.onCreate();
    }

    private void setupDrawerLayout() {
        //좌측 메뉴 초기 메뉴 리스트 정의
        mSideMenu.inflateMenu(R.menu.navigation_menu);

        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, mSideMenu);

        mNavHeaderText = (TextView) header.findViewById(R.id.headerTitleText);
        mWalletSpinner = header.findViewById(R.id.wallet_spinner);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
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

            // show address ic_qrcode_white_24dp, send actionbar item
            mBeforeLoginLayout.setVisibility(View.GONE);
            mAfterLoginLayout.setVisibility(View.VISIBLE);

            String loginWalletName = ((MainPresenter) mPresenter).getLoginWalletName();

            if (loginWalletName == null) {
                mNavHeaderText.setText(R.string.navigation_header_title);
            } else {
                mNavHeaderText.setText(loginWalletName);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(loginWalletName);
                }

                mWalletAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,
                        new String[] {
                        loginWalletName,
                        CREATE_WALLET,
                        IMPORT_WALLET
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
//            if (mMenuAddressItem != null) {
//                mMenuAddressItem.setVisible(false);
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        mMenuAddressItem = menu.findItem(R.id.action_address);
        mMenuTronPayItem = menu.findItem(R.id.action_tron_pay);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_address:
                startActivity(AddressActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_item_create_account:
                startActivity(CreateWalletActivity.class);
                break;
            case R.id.drawer_item_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.drawer_item_send_tron:
                startActivity(SendCoinActivity.class);
                break;
            case R.id.drawer_item_import_wallet:
                startActivity(ImportWalletActivity.class);
                break;
            case R.id.drawer_item_export_private_key:
                sharePrivateKey();
                break;
            case R.id.drawer_item_about_tron:
                startActivity(AboutActivity.class);
                break;
            case R.id.drawer_item_logout:
                logout();
                break;
        }
        return false;
    }

    public void logout() {
        ((MainPresenter) mPresenter).logout();
        checkLoginState();
    }

    @OnClick(R.id.btn_create_account)
    public void onCreateAccountClick() {
        startActivity(CreateWalletActivity.class);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        startActivity(LoginActivity.class);
    }

    @Override
    public void displayAccountInfo(@NonNull Protocol.Account account) {
        if (mMenuAddressItem != null) {
            mMenuAddressItem.setVisible(true);
        }

        Log.i(MainActivity.class.getSimpleName(), "address : " + account.getAddress().toStringUtf8());
        Log.i(MainActivity.class.getSimpleName(), "balance : " + account.getBalance() + "trx");
        double balance = ((double) account.getBalance()) / Constants.REAL_TRX_AMOUNT;
        DecimalFormat df = new DecimalFormat("#,##0.00000000");

        mBalanceText.setText(df.format(balance));
    }

    private void sharePrivateKey() {
        String privateKey = Tron.getInstance(MainActivity.this).getPrivateKey();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, privateKey);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_private_key)));
    }

    private AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            String selectedAccount = mWalletAdapter.getItem(pos);

            if (CREATE_WALLET.equals(selectedAccount)) {
                // todo - create new wallet
            } else if (IMPORT_WALLET.equals(selectedAccount)) {
                // todo - import wallet
            } else {

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}
