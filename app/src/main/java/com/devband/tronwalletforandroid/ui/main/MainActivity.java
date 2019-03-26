package com.devband.tronwalletforandroid.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.BuildConfig;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.blockexplorer.BlockExplorerActivity;
import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.main.adapter.MyTokenListAdapter;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.myaccount.MyAccountActivity;
import com.devband.tronwalletforandroid.ui.mytransfer.TransferActivity;
import com.devband.tronwalletforandroid.ui.previewwallet.PreviewWalletActivity;
import com.devband.tronwalletforandroid.ui.sendtrc10.SendTrc10Activity;
import com.devband.tronwalletforandroid.ui.sendtrc20.SendTrc20Activity;
import com.devband.tronwalletforandroid.ui.smartcontract.TestSmartContractActivity;
import com.devband.tronwalletforandroid.ui.token.TokenActivity;
import com.devband.tronwalletforandroid.ui.vote.VoteActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends CommonActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainPresenter mMainPresenter;

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

    @BindView(R.id.no_token_layout)
    LinearLayout mNoTokenLayout;

    @BindView(R.id.my_token_listview)
    RecyclerView mMyTokenListView;

    @BindView(R.id.check_favorite_tokens)
    CheckBox mShowOnlyFavoritesCheckBox;

    @BindView(R.id.trc10_button)
    Button mTrc10Button;

    @BindView(R.id.trc20_button)
    Button mTrc20Button;

    @BindView(R.id.fab_add_trc20)
    FloatingActionButton fabAdd;

    @BindView(R.id.trc20_sync_msg_text)
    TextView trc20SyncMsgText;

    @BindView(R.id.favorites_text)
    TextView mFavoritesText;

    Spinner mAccountSpinner;

    TextView mNavHeaderText;

    String mLoginAccountName;

    private TronAccount mLoginTronAccount;

    private CoinMarketCap mCoinMarketCapPriceInfo;

    private ArrayAdapter<AccountModel> mAccountAdapter;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private MyTokenListAdapter mMyTokenListAdapter;

    private boolean mLoadingAccountInfo;

    private boolean mDoubleBackToExitPressedOnce;

    private int mSelectedToken = Constants.TOKEN_TRC_10;

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

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyTokenListView.setLayoutManager(mLayoutManager);
        mMyTokenListView.addItemDecoration(new DividerItemDecoration(0));
        mMyTokenListView.setNestedScrollingEnabled(false);

        mMyTokenListAdapter = new MyTokenListAdapter();
        mMyTokenListView.setAdapter(mMyTokenListAdapter);
        mAdapterView = mMyTokenListAdapter;

        mMainPresenter.setAdapterDataModel(mMyTokenListAdapter);
        mMainPresenter.onCreate();

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

        mShowOnlyFavoritesCheckBox.setOnCheckedChangeListener((view, isChecked) -> {
            if (mSelectedToken == Constants.TOKEN_TRC_10) {
                mMainPresenter.setOnlyFavorites(isChecked);
            }

            mShowOnlyFavoritesCheckBox.setEnabled(false);
            checkLoginState();
        });

        initAccountList(false);
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

    private void initAccountList(boolean isCreateOrImport) {
        mMainPresenter.getAccountList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<List<AccountModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<AccountModel> accountModelList) {
                initAccountList(accountModelList);
                if (isCreateOrImport) {
                    mAccountSpinner.setSelection(mAccountAdapter.getCount() - 1);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                initAccountList(new ArrayList<>());
            }
        });
    }

    private void initAccountList(List<AccountModel> accountModelList) {
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

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.trc10_button)
    public void onTrc10Click() {
        mSelectedToken = Constants.TOKEN_TRC_10;
        mFavoritesText.setText(R.string.favorites_text);
        mShowOnlyFavoritesCheckBox.setChecked(mMainPresenter.getIsFavoritesTokens());
        mTrc10Button.setBackgroundResource(R.drawable.ic_trc10_selected);
        mTrc10Button.setTextColor(getResources().getColor(android.R.color.white));
        mTrc20Button.setBackgroundResource(R.drawable.ic_trc20_unselected);
        mTrc20Button.setTextColor(getResources().getColor(R.color.trc20_color));
        fabAdd.setVisibility(View.GONE);
        checkLoginState();
    }

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.trc20_button)
    public void onTrc20Click() {
        mSelectedToken = Constants.TOKEN_TRC_20;
        mFavoritesText.setText(R.string.hide_no_balance_text);
        mShowOnlyFavoritesCheckBox.setChecked(true);
        mTrc10Button.setBackgroundResource(R.drawable.ic_trc10_unselected);
        mTrc10Button.setTextColor(getResources().getColor(R.color.trc10_color));
        mTrc20Button.setBackgroundResource(R.drawable.ic_trc20_selected);
        mTrc20Button.setTextColor(getResources().getColor(android.R.color.white));
        fabAdd.setVisibility(View.VISIBLE);
        checkLoginState();
    }

    private void checkLoginState() {
        if (mLoadingAccountInfo) {
            return;
        }

        if (mMainPresenter.isLogin()) {
            mLoadingAccountInfo = true;
            // get account info
            if (mSelectedToken == Constants.TOKEN_TRC_10) {
                mMainPresenter.getMyAccountTrc10Info();
            } else if (mSelectedToken == Constants.TOKEN_TRC_20) {
                mMainPresenter.getMyAccountTrc20Info(mShowOnlyFavoritesCheckBox.isChecked());
            }

            Single.fromCallable(() -> mMainPresenter.getLoginAccount())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<AccountModel>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(AccountModel loginAccount) {
                    if (loginAccount == null) {
                        mNavHeaderText.setText(R.string.navigation_header_title);
                    } else {
                        mLoginAccountName = loginAccount.getName();
                        mNavHeaderText.setText(mLoginAccountName);
                        mMainTitleText.setText(mLoginAccountName);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });

            mMainPresenter.getAccountList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((accountModelList) -> {
                long id = mMainPresenter.getLoginAccountIndex();

                int size = accountModelList.size();

                for (int i = 0; i < size; i++) {
                    if (id == accountModelList.get(i).getId()) {
                        if (mAccountSpinner.getSelectedItemPosition() != i) {
                            mAccountSpinner.setSelection(i);
                            return;
                        }
                        break;
                    }
                }

                mAccountAdapter.notifyDataSetChanged();
            }, (e) -> {});

            if (mSelectedToken == Constants.TOKEN_TRC_10) {
                mShowOnlyFavoritesCheckBox.setChecked(mMainPresenter.getIsFavoritesTokens());
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
                startActivity(TransferActivity.class);
                break;
            case R.id.action_preview_wallet:
                startActivity(PreviewWalletActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
        } else {
            if (mDoubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.mDoubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.activity_main_back_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> mDoubleBackToExitPressedOnce = false, 2000);
        }
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
            case R.id.drawer_item_change_password:
                changePassword();
                break;
            case R.id.drawer_item_send_trx10_token:
                startActivity(SendTrc10Activity.class);
                break;
            case R.id.drawer_item_send_trx20_token:
                startActivity(SendTrc20Activity.class);
                break;
            case R.id.drawer_item_vote:
                startActivity(VoteActivity.class);
                break;
            case R.id.drawer_item_tokens:
                startActivity(TokenActivity.class);
                break;
            case R.id.drawer_item_block_explorer:
                startActivity(BlockExplorerActivity.class);
                break;
//            case R.id.drawer_item_exchange:
//                startActivity(ExchangeActivity.class);
//                break;
            case R.id.drawer_item_more:
                 startActivity(MoreActivity.class);
                break;
            case R.id.drawer_item_logout:
                logout();
                break;
        }
        return false;
    }

    private void changePassword() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.change_password)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .customView(R.layout.dialog_change_password, true)
                .positiveText(R.string.confirm_text)
                .negativeText(R.string.cancel_text)
                .onPositive((dialog, which) -> {
                    String currentPassword = ((EditText) dialog.getCustomView().findViewById(R.id.current_password))
                            .getText().toString();

                    if (!mMainPresenter.matchPassword(currentPassword)) {
                        Toast.makeText(dialog.getContext(), R.string.unmatched_current_password, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String newPassword = ((EditText) dialog.getCustomView().findViewById(R.id.new_password))
                            .getText().toString();
                    String confirmNewPassword = ((EditText) dialog.getCustomView().findViewById(R.id.confirm_new_password))
                            .getText().toString();

                    if (!TextUtils.equals(newPassword, confirmNewPassword)) {
                        Toast.makeText(dialog.getContext(), R.string.not_equal_new_password, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!WalletAppManager.passwordValid(newPassword)) {
                        Toast.makeText(dialog.getContext(), R.string.invalid_new_password, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dialog.dismiss();
                    mMainPresenter.changePassword(currentPassword, confirmNewPassword);
                })
                .onNegative((dialog, which) -> Log.d("hanseon--", "negative"));

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public void logout() {
        mMainPresenter.logout();
        finishActivity();
        startActivity(LoginActivity.class);
    }

    private void createAccount() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_create_account)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.create_account_password_hint), "", (dialog, input) -> {
                    showProgressDialog(null, getString(R.string.loading_msg));
                    mMainPresenter.createAccount(Constants.PREFIX_ACCOUNT_NAME, input.toString());
                }).show();
    }

    private void importAccount() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_import_account)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_import_private_key, false);

        MaterialDialog dialog = builder.build();

        EditText inputPrivateKey = (EditText) dialog.getCustomView().findViewById(R.id.input_private_key);
        EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);
        Button importButton = (Button) dialog.getCustomView().findViewById(R.id.btn_import_private_key);

        importButton.setOnClickListener(view -> {
            String privateKey = inputPrivateKey.getText().toString();
            String password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(privateKey)) {
                Toast.makeText(this, R.string.required_private_key, Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, R.string.required_password, Toast.LENGTH_SHORT).show();
                return;
            }

            mMainPresenter.importAccount(Constants.PREFIX_ACCOUNT_NAME, privateKey, password);

            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void displayAccountInfo(@NonNull TronAccount account) {
        hideDialog();
        mLoginTronAccount = account;
        mShowOnlyFavoritesCheckBox.setEnabled(true);

        if (mLoginTronAccount.getAssetList().isEmpty()) {
            mNoTokenLayout.setVisibility(View.VISIBLE);
            mMyTokenListView.setVisibility(View.GONE);
        } else {
            mNoTokenLayout.setVisibility(View.GONE);
            mMyTokenListView.setVisibility(View.VISIBLE);
        }

        double balance = ((double) account.getBalance()) / Constants.ONE_TRX;
        long frozenBalance = 0;

        for (int i = 0; i < account.getFrozenList().size(); i++) {
            Frozen frozen = account.getFrozenList().get(i);

            frozenBalance += frozen.getFrozenBalance();
        }

        double fz = frozenBalance / Constants.ONE_TRX;
        double bandwidth = account.getBandwidth();

        mLoginAccountBalanceText.setText(Constants.tokenBalanceFormat.format(balance) + " " + getString(R.string.currency_text));
        mLoginFrozenBalanceText.setText(Constants.numberFormat.format(fz));
        mLoginBandwidthText.setText(bandwidth == 0 ? "-" : Constants.numberFormat.format(bandwidth));

        mLoadingAccountInfo = false;

        mMainPresenter.getTronMarketInfo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setTronMarketInfo(CoinMarketCap coinMarketCap) {
        double balance = ((double) mLoginTronAccount.getBalance()) / Constants.ONE_TRX;

        mLoginAccountPriceText.setText("(" + Constants.usdFormat.format(balance * Double.parseDouble(coinMarketCap.getPriceUsd()))
                + " " + getString(R.string.price_text) + ")");

        mCoinMarketCapPriceInfo = coinMarketCap;

        mPriceHelpImage.setVisibility(View.VISIBLE);
        mLoginAccountPriceText.setVisibility(View.VISIBLE);
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
        new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.backup_title))
                .content(getString(R.string.create_account_backup_msg))
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(android.R.color.black)
                .backgroundColorRes(android.R.color.white)
                .positiveText(R.string.close_text)
                .autoDismiss(true)
                .build()
                .show();

        initAccountList(true);
    }

    @Override
    public void successImportAccount() {
        hideDialog();
        initAccountList(true);
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
        hideDialog();
        mLoadingAccountInfo = false;
        mShowOnlyFavoritesCheckBox.setEnabled(true);
        Toast.makeText(MainActivity.this, getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_add_trc20)
    public void onTrc20AddClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_add_trc_20_contract)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_add_trc20_contract, false);

        MaterialDialog dialog = builder.build();

        EditText inputName = (EditText) dialog.getCustomView().findViewById(R.id.input_trc_20_name);
        EditText inputSymbol = (EditText) dialog.getCustomView().findViewById(R.id.input_trc_20_symbol);
        EditText inputContractAddress = (EditText) dialog.getCustomView().findViewById(R.id.input_trc_20_contract_address);
        EditText inputPrecision = (EditText) dialog.getCustomView().findViewById(R.id.input_trc_20_contract_precision);
        Button importButton = (Button) dialog.getCustomView().findViewById(R.id.btn_add_trc_20_contract);

        importButton.setOnClickListener(view -> {
            String name = inputName.getText().toString();
            String symbol = inputSymbol.getText().toString();
            String contractAddress = inputContractAddress.getText().toString();
            String precision = inputPrecision.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, R.string.required_trc20_name, Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(symbol)) {
                Toast.makeText(this, R.string.required_trc20_symbol, Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(contractAddress)) {
                Toast.makeText(this, R.string.required_contract_address, Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(precision)) {
                Toast.makeText(this, R.string.requried_precision, Toast.LENGTH_SHORT).show();
                return;
            }

            mMainPresenter.addTrc20Contract(name, symbol, contractAddress, Integer.parseInt(precision));

            dialog.dismiss();
        });

        dialog.show();
    }

    @OnClick(R.id.fab_refresh)
    public void onHistoryClick() {
        if (BuildConfig.DEBUG) {
            startActivity(TestSmartContractActivity.class);
        }

        checkLoginState();
    }

    @OnClick({ R.id.login_account_price_layout })
    public void onPriceHelpImageClick() {
        StringBuilder sb = new StringBuilder();

        if (mCoinMarketCapPriceInfo == null) {
            return;
        }

        Date updated = new Date(Long.parseLong(mCoinMarketCapPriceInfo.getLastUpdated()) * 1_000);

        sb.append("Price : ")
                .append(mCoinMarketCapPriceInfo.getPriceUsd())
                .append(" USD (")
                .append("-".equals(mCoinMarketCapPriceInfo.getPercentChange24h().substring(0, 1)) ?
                        mCoinMarketCapPriceInfo.getPercentChange24h() :
                        "+" + mCoinMarketCapPriceInfo.getPercentChange24h()
                )
                .append("%)\nLast updated : ")
                .append(Constants.sdf.format(updated))
                .append("\nFrom CoinMarketCap");

        hideDialog();

        new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.tron_price_title))
                .content(sb.toString())
                .titleColorRes(android.R.color.black)
                .contentColorRes(android.R.color.black)
                .backgroundColorRes(android.R.color.white)
                .positiveText(R.string.close_text)
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
                            mMainPresenter.changeLoginAccountName(accountName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        checkLoginState();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }
                    }
                }).show();
    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            AccountModel accountModel = mAccountAdapter.getItem(pos);

            mMainPresenter.changeLoginAccount(accountModel);
            mMainTitleText.setText(accountModel.getName());

            mDrawer.closeDrawers();

            checkLoginState();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };

    @OnClick(R.id.get_token_button)
    public void onGetTokenClick() {
        startActivity(TokenActivity.class);
    }

    @Override
    public void changePasswordResult(boolean result) {
        hideDialog();

        if (result) {
            Toast.makeText(MainActivity.this, getString(R.string.change_password_success_msg), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.change_password_fail_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showChangePasswordDialog() {
        showProgressDialog(getString(R.string.change_password), getString(R.string.change_password_loading_msg));
    }

    @Override
    public void goToIntroActivity() {
        finish();
        startActivity(IntroActivity.class);
    }

    @Override
    public void resultAddTrc20(int result) {
        if (result == Tron.SUCCESS) {
            checkLoginState();
        } else if (result ==  Tron.ERROR_INVALID_ADDRESS) {

        } else if (result == Tron.ERROR_INVALID_TRC20_CONTRACT) {

        }
    }

    @Override
    public void finishSyncTrc20() {
        trc20SyncMsgText.setVisibility(View.GONE);
    }

    @Override
    public void showSyncTrc20Loading() {
        trc20SyncMsgText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDialog() {
        if (!isFinishing()) {
            showProgressDialog(null, getString(R.string.loading_msg));
        }
    }
}
