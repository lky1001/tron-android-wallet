package com.devband.tronwalletforandroid.ui.accountdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.overview.OverviewFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.RepresentativeFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.transaction.TransactionFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.transfer.TransferFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDetailActivity extends CommonActivity {

    public static final String EXTRA_ADDRESS = "extra_address";
    public static final String EXTRA_BLOCK = "extra_block";

    private static final int FRAGMENT_OVERVIEW = 0;
    private static final int FRAGMENT_TRANSACTION = 1;
    private static final int FRAGMENT_TRANSFER = 2;
    private static final int FRAGMENT_REPRESENTATIVE = 3;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;

    private String mAddress;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        ButterKnife.bind(this);
        removeShiftMode(mBottomNavigationView);

        setSupportActionBar(mToolbar);

        mAddress = getIntent().getStringExtra(EXTRA_ADDRESS);

        if (TextUtils.isEmpty(mAddress)) {
            finishActivity();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.account_title) + "(" + mAddress + ")");
        }

        initUi();
    }

    private void initUi() {
        mFragments.add(OverviewFragment.newInstance(mAddress));
        mFragments.add(TransactionFragment.newInstance(mAddress));
        mFragments.add(TransferFragment.newInstance(mAddress));
        mFragments.add(RepresentativeFragment.newInstance(mAddress));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragments.get(FRAGMENT_OVERVIEW))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSACTION))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSFER))
                .add(R.id.content, mFragments.get(FRAGMENT_REPRESENTATIVE))
                .commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigationView.setSelectedItemId(FRAGMENT_OVERVIEW);

        changeFragment(FRAGMENT_OVERVIEW);
    }

    private void changeFragment(int num) {
        switch (num) {
            case FRAGMENT_OVERVIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .hide(mFragments.get(FRAGMENT_REPRESENTATIVE))
                        .commit();
                break;
            case FRAGMENT_TRANSACTION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .show(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .hide(mFragments.get(FRAGMENT_REPRESENTATIVE))
                        .commit();
                break;
            case FRAGMENT_TRANSFER:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .show(mFragments.get(FRAGMENT_TRANSFER))
                        .hide(mFragments.get(FRAGMENT_REPRESENTATIVE))
                        .commit();
                break;
            case FRAGMENT_REPRESENTATIVE:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .show(mFragments.get(FRAGMENT_REPRESENTATIVE))
                        .commit();
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = (item) -> {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_overview:
                changeFragment(FRAGMENT_OVERVIEW);
                return true;
            case R.id.bottom_navigation_transaction:
                changeFragment(FRAGMENT_TRANSACTION);
                return true;
            case R.id.bottom_navigation_transfer:
                changeFragment(FRAGMENT_TRANSFER);
                return true;
            case R.id.bottom_navigation_representative:
                changeFragment(FRAGMENT_REPRESENTATIVE);
                return true;
        }
        return false;
    };

    public void removeRepresentativeMenu() {
        mBottomNavigationView.getMenu().removeItem(R.id.bottom_navigation_representative);
        removeShiftMode(mBottomNavigationView);
    }
}
