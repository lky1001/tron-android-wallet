package com.devband.tronwalletforandroid.ui.token;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.token.holder.HolderFragment;
import com.devband.tronwalletforandroid.ui.token.overview.OverviewFragment;
import com.devband.tronwalletforandroid.ui.token.transaction.TransactionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenDetailActivity extends CommonActivity {

    private static final int FRAGMENT_OVERVIEW = 0;
    private static final int FRAGMENT_TRANSACTION = 1;
    private static final int FRAGMENT_HOLDER = 2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;

    private List<BaseFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);

        ButterKnife.bind(this);
    }

    private void initUi() {
        mFragments.add(OverviewFragment.newInstance());
        mFragments.add(TransactionFragment.newInstance());
        mFragments.add(HolderFragment.newInstance());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragments.get(FRAGMENT_OVERVIEW))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSACTION))
                .add(R.id.content, mFragments.get(FRAGMENT_HOLDER))
                .commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigationView.setSelectedItemId(FRAGMENT_OVERVIEW);
    }

    private void changeFragment(int num) {

        int titleResId = -1;

        switch (num) {
            case FRAGMENT_OVERVIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_HOLDER))
                        .commit();
                titleResId = R.string.bottom_navigation_menu_overview;
                break;
            case FRAGMENT_TRANSACTION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .show(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_HOLDER))
                        .commit();
                titleResId = R.string.bottom_navigation_menu_transaction;
                break;
            case FRAGMENT_HOLDER:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .show(mFragments.get(FRAGMENT_HOLDER))
                        .commit();
                titleResId = R.string.bottom_navigation_menu_holder;
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(titleResId);
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
            case R.id.bottom_navigation_holder:
                changeFragment(FRAGMENT_HOLDER);
                return true;
        }
        return false;
    };
}
