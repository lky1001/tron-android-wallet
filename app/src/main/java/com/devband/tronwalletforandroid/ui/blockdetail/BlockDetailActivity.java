package com.devband.tronwalletforandroid.ui.blockdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.accountdetail.transaction.TransactionFragment;
import com.devband.tronwalletforandroid.ui.blockdetail.fragment.BlockInfoFragment;
import com.devband.tronwalletforandroid.ui.blockexplorer.transfer.TransferFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 28..
 */

public class BlockDetailActivity extends CommonActivity {

    public static final String EXTRA_BLOCK_NUMBER = "extra_block_number";

    private static final int FRAGMENT_OVERVIEW = 0;
    private static final int FRAGMENT_TRANSACTION = 1;
    private static final int FRAGMENT_TRANSFER = 2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation)
    BottomNavigationView mBottomNavigationView;

    private long mBlockNumber;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_block);

        ButterKnife.bind(this);
        removeShiftMode(mBottomNavigationView);

        setSupportActionBar(mToolbar);

        mBlockNumber = getIntent().getLongExtra(EXTRA_BLOCK_NUMBER, 0L);

        if (mBlockNumber == 0L) {
            finishActivity();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.block_title) + " #" + Utils.getCommaNumber(mBlockNumber));
        }

        initUi();
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
        }
        return false;
    };

    private void initUi() {
        mFragments.add(BlockInfoFragment.newInstance(mBlockNumber));
        mFragments.add(TransactionFragment.newInstance(mBlockNumber));
        mFragments.add(TransferFragment.newInstance(mBlockNumber));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragments.get(FRAGMENT_OVERVIEW))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSACTION))
                .add(R.id.content, mFragments.get(FRAGMENT_TRANSFER))
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
                        .commit();
                break;
            case FRAGMENT_TRANSACTION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .show(mFragments.get(FRAGMENT_TRANSACTION))
                        .hide(mFragments.get(FRAGMENT_TRANSFER))
                        .commit();
                break;
            case FRAGMENT_TRANSFER:
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mFragments.get(FRAGMENT_OVERVIEW))
                        .hide(mFragments.get(FRAGMENT_TRANSACTION))
                        .show(mFragments.get(FRAGMENT_TRANSFER))
                        .commit();
                break;
        }
    }
}
