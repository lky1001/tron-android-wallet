package com.devband.tronwalletforandroid.ui.token;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.token.holder.HolderFragment;
import com.devband.tronwalletforandroid.ui.token.overview.OverviewFragment;
import com.devband.tronwalletforandroid.ui.token.transfer.TransferFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenDetailActivity extends CommonActivity {

    public static final String EXTRA_TOKEN_NAME = "extra_token_name";

    private static final int FRAGMENT_OVERVIEW = 0;
    private static final int FRAGMENT_TRANSFER = 1;
    private static final int FRAGMENT_HOLDER = 2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;

    private String mTokenName;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);

        ButterKnife.bind(this);
        removeShiftMode(mBottomNavigationView);

        setSupportActionBar(mToolbar);

        mTokenName = getIntent().getStringExtra(EXTRA_TOKEN_NAME);

        if (TextUtils.isEmpty(mTokenName)) {
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTokenName);
        }

        initUi();
    }

    private void initUi() {
        mFragments.add(OverviewFragment.newInstance(mTokenName));
        mFragments.add(TransferFragment.newInstance(mTokenName));
        mFragments.add(HolderFragment.newInstance(mTokenName));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragments.get(FRAGMENT_OVERVIEW))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSFER))
                .add(R.id.content, mFragments.get(FRAGMENT_HOLDER))
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
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .hide(mFragments.get(FRAGMENT_HOLDER))
                        .commit();
                break;
            case FRAGMENT_TRANSFER:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .show(mFragments.get(FRAGMENT_TRANSFER))
                        .hide(mFragments.get(FRAGMENT_HOLDER))
                        .commit();
                break;
            case FRAGMENT_HOLDER:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .show(mFragments.get(FRAGMENT_HOLDER))
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
            case R.id.bottom_navigation_transfer:
                changeFragment(FRAGMENT_TRANSFER);
                return true;
            case R.id.bottom_navigation_holder:
                changeFragment(FRAGMENT_HOLDER);
                return true;
        }
        return false;
    };
}
