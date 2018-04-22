package com.devband.tronwalletforandroid.ui.main;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.createaccount.CreateAccountActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;

import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends CommonActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

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

    private MenuItem mMenuAddressItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupDrawerLayout();

        mPresenter = new MainPresenter(this);
        mPresenter.onCreate();

        // test
        Tron tron = Tron.getInstance(this);

        tron.queryAccount("27fXgQ46DcjEsZ444tjZPKULcxiUfDrDjqj")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Protocol.Account>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Protocol.Account account) {
                        if (account == null) {
                            Log.i(MainActivity.class.getSimpleName(), "Get Account failed !!!!");
                        } else {
                            Log.i(MainActivity.class.getSimpleName(), "Address::" + ByteArray.toHexString(account.getAddress().toByteArray()));
                            Log.i(MainActivity.class.getSimpleName(), "Account[" + account + "]");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void setupDrawerLayout() {
        //좌측 메뉴 초기 메뉴 리스트 정의
        mSideMenu.inflateMenu(R.menu.navigation_menu);

        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, mSideMenu);

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
        checkLogin();
    }

    private void checkLogin() {
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
        } else {
            if(mSideMenu.getMenu() != null) {
                mSideMenu.getMenu().clear();
                mSideMenu.inflateMenu(R.menu.navigation_menu);
            }

            // hide address ic_qrcode_white_24dp, send actionbar item
            mBeforeLoginLayout.setVisibility(View.VISIBLE);
            mAfterLoginLayout.setVisibility(View.GONE);

            if (mMenuAddressItem != null) {
                mMenuAddressItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        mMenuAddressItem = menu.findItem(R.id.action_address);
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
                startActivity(CreateAccountActivity.class);
                break;
            case R.id.drawer_item_login:
                startActivity(LoginActivity.class);
                break;
        }
        return false;
    }

    @OnClick(R.id.btn_create_account)
    public void onCreateAccountClick() {
        startActivity(CreateAccountActivity.class);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        startActivity(LoginActivity.class);
    }

    @Override
    public void displayAccountInfo(Protocol.Account account) {
        if (mMenuAddressItem != null) {
            mMenuAddressItem.setVisible(true);
        }

        Log.i(MainActivity.class.getSimpleName(), String.valueOf(account.getBalance()) + "trx");
        double balance = ((double) account.getBalance()) / 1_000_000f;
        DecimalFormat df = new DecimalFormat("#,##0.00000000");

        mBalanceText.setText(df.format(balance));
    }
}
