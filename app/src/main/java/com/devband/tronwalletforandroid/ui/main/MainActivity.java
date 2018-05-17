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
import android.text.InputType;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.main.adapter.MyTokenListAdapter;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.myaccount.MyAccountActivity;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;
import com.devband.tronwalletforandroid.ui.requestcoin.RequestCoinActivity;
import com.devband.tronwalletforandroid.ui.sendtoken.SendTokenActivity;
import com.devband.tronwalletforandroid.ui.representative.RepresentativeActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.tron.protos.Protocol;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @BindView(R.id.login_account_name_text)
    TextView mMainTitleText;

    @BindView(R.id.login_account_balance_text)
    TextView mLoginAccountBalanceText;

    @BindView(R.id.login_account_price_text)
    TextView mLoginAccountPriceText;

    @BindView(R.id.login_frozen_balance_text)
    TextView mLoginFrozenBalanceText;

    @BindView(R.id.login_bandwidth_text)
    TextView mLoginBandwidthText;

    @BindView(R.id.price_help_image)
    ImageView mPriceHelpImage;

    @BindView(R.id.edit_account_name_image)
    ImageView mEditAccountNameImage;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @BindView(R.id.no_token_layout)
    LinearLayout mNoTokenLayout;

    @BindView(R.id.my_token_listview)
    RecyclerView mMyTokenListView;

    FloatingActionButton mFloatingActionMenuRequestCoin;

    FloatingActionButton mFloatingActionMenuSendCoin;

    Spinner mAccountSpinner;

    TextView mNavHeaderText;

    String mLoginAccountName;

    private Protocol.Account mLoginTronAccount;

    private CoinMarketCap mCoinMarketCapPriceInfo;

    private ArrayAdapter<AccountModel> mAccountAdapter;

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
                    mToolbarLayout.setTitle(mLoginAccountName);
                    mMainTitleText.setVisibility(View.GONE);
                    mLoginAccountBalanceText.setVisibility(View.GONE);
                    mLoginAccountPriceText.setVisibility(View.GONE);
                    mPriceHelpImage.setVisibility(View.GONE);
                    mEditAccountNameImage.setVisibility(View.GONE);
                    mLoginFrozenBalanceText.setVisibility(View.GONE);
                    mLoginBandwidthText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mMainTitleText.setVisibility(View.VISIBLE);
                    mLoginAccountBalanceText.setVisibility(View.VISIBLE);
                    mLoginAccountPriceText.setVisibility(View.VISIBLE);
                    mPriceHelpImage.setVisibility(View.VISIBLE);
                    mEditAccountNameImage.setVisibility(View.VISIBLE);
                    mLoginFrozenBalanceText.setVisibility(View.VISIBLE);
                    mLoginBandwidthText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        initAccountList();
    }

    private void setupDrawerLayout() {
        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, mSideMenu);

        mNavHeaderText = (TextView) header.findViewById(R.id.headerTitleText);
        mAccountSpinner = header.findViewById(R.id.account_spinner);

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
        mSideMenu.inflateMenu(R.menu.navigation_logged_in_menu);
    }

    private void initAccountList() {
        List<AccountModel> accountModelList = ((MainPresenter) mPresenter).getAccountList();

        mAccountAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,
                accountModelList);

        mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccountSpinner.setAdapter(mAccountAdapter);
        mAccountSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoginState();
    }

    private void checkLoginState() {
        if (((MainPresenter) mPresenter).isLogin()) {
            // get account info
            ((MainPresenter) mPresenter).getMyAccountInfo();

            AccountModel loginAccount = ((MainPresenter) mPresenter).getLoginAccount();

            if (loginAccount == null) {
                mNavHeaderText.setText(R.string.navigation_header_title);
            } else {
                mLoginAccountName = loginAccount.getName();
                mNavHeaderText.setText(mLoginAccountName);
                mMainTitleText.setText(mLoginAccountName);
            }
        } else {
            finishActivity();
            startActivity(LoginActivity.class);
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
            case R.id.action_create_account:
                createAccount();
                break;
            case R.id.action_import_account:
                importAccount();
                break;
            case R.id.action_refresh_account:
                checkLoginState();
                break;
            case R.id.action_transaction_history:
                Toast.makeText(MainActivity.this, "Transaction history coming soon",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_item_my_account:
                startActivity(MyAccountActivity.class);
                break;
            case R.id.drawer_item_my_address:
                startActivity(AddressActivity.class);
                break;
            case R.id.drawer_item_send_tron:
                startActivity(SendTokenActivity.class);
                break;
            case R.id.drawer_item_export_private_key:
                sharePrivateKey();
                break;
            case R.id.drawer_item_vote:
                startActivity(RepresentativeActivity.class);
                break;
            case R.id.drawer_item_donations:
                Intent intent = new Intent(MainActivity.this, SendTokenActivity.class);
                intent.putExtra(EXTRA_FROM_DONATIONS, true);
                startActivity(intent);
                break;
            case R.id.drawer_item_more:
                 startActivity(MoreActivity.class);
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

    private void createAccount() {
        showProgressDialog(null, getString(R.string.loading_msg));
        ((MainPresenter) mPresenter).createAccount(Constants.PREFIX_ACCOUNT_NAME);
    }

    private void importAccount() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_import_account)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.import_account_hint), "bc82841406e33d12374fb2933ddfafab3769157e999fd92981ab006ce49bcddf", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String privateKey = input.toString();

                        if (!TextUtils.isEmpty(privateKey)) {
                            ((MainPresenter) mPresenter).importAccount(Constants.PREFIX_ACCOUNT_NAME, privateKey);
                        }
                    }
                }).show();
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

        Log.i(MainActivity.class.getSimpleName(), "address : " + AccountManager.encode58Check(account.getAddress().toByteArray()));
        Log.i(MainActivity.class.getSimpleName(), "balance : " + account.getBalance() + Constants.TRON_SYMBOL);
        double balance = ((double) account.getBalance()) / Constants.REAL_TRX_AMOUNT;
        long frozenBalance = 0;

        for (int i = 0; i < account.getFrozenList().size(); i++) {
            Protocol.Account.Frozen frozen = account.getFrozen(i);

            frozenBalance += frozen.getFrozenBalance();
        }

        double fz = frozenBalance / Constants.REAL_TRX_AMOUNT;

        DecimalFormat df = new DecimalFormat("#,##0");

        mLoginAccountBalanceText.setText(df.format(balance) + " " + getString(R.string.currency_text));
        mLoginFrozenBalanceText.setText(df.format(fz) + " " + getString(R.string.frozen_trx));
        mLoginBandwidthText.setText(df.format(account.getBandwidth() / Constants.REAL_TRX_AMOUNT) + " " + getString(R.string.bandwidth_text));

        ((MainPresenter) mPresenter).getTronMarketInfo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setTronMarketInfo(CoinMarketCap coinMarketCap) {
        if (mLoginTronAccount != null && mLoginTronAccount.getBalance() > 0) {
            double balance = ((double) mLoginTronAccount.getBalance()) / Constants.REAL_TRX_AMOUNT;
            DecimalFormat df = new DecimalFormat("#,##0.000");

            mLoginAccountPriceText.setText("(" + df.format(balance * Double.parseDouble(coinMarketCap.getPriceUsd()))
                    + " " + getString(R.string.price_text) + ")");

            mCoinMarketCapPriceInfo = coinMarketCap;

            mPriceHelpImage.setVisibility(View.VISIBLE);
            mLoginAccountPriceText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showInvalidPasswordMsg() {
        hideDialog();
        Toast.makeText(MainActivity.this, getString(R.string.invalid_password),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successCreateAccount() {
        hideDialog();
        initAccountList();
        mAccountSpinner.setSelection(mAccountAdapter.getCount() - 1);
    }

    @Override
    public void successImportAccount() {
        successCreateAccount();
    }

    @Override
    public void failCreateAccount() {
        hideDialog();
        Toast.makeText(MainActivity.this, getString(R.string.invalid_private_key),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void duplicatedAccount() {
        hideDialog();
        Toast.makeText(MainActivity.this, getString(R.string.already_exist_account),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionError() {
        Toast.makeText(MainActivity.this, getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_refresh)
    public void onHistoryClick() {
        checkLoginState();
    }

    @OnClick({ R.id.login_account_price_layout })
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

    @OnClick(R.id.edit_account_name_image)
    public void onEditAccountNameImageClick() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_rename_account)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputRangeRes(2, 20, android.R.color.white)
                .input(getString(R.string.rename_account_hint), mLoginAccountName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String accountName = input.toString();

                        if (!TextUtils.isEmpty(accountName)) {
                            if (((MainPresenter) mPresenter).changeLoginAccountName(accountName)) {
                                checkLoginState();
                            }
                        }
                    }
                }).show();
    }

    private void sharePrivateKey() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_export_private_key)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.input_password_text), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String password = input.toString();

                        if (!TextUtils.isEmpty(password) && ((MainPresenter) mPresenter).matchPassword(password)) {
                            String privateKey = ((MainPresenter) mPresenter).getLoginPrivateKey();

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, privateKey);
                            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_private_key)));
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            AccountModel accountModel = mAccountAdapter.getItem(pos);
            ((MainPresenter) mPresenter).changeLoginAccount(accountModel);
            mDrawer.closeDrawers();
            checkLoginState();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
