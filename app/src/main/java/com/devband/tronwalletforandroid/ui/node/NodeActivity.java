package com.devband.tronwalletforandroid.ui.node;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.node.adapter.NodeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NodeActivity extends CommonActivity implements NodeView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.tronnode_listview)
    RecyclerView tronnodeListview;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    private NodeListAdapter nodeListAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_node_list);
        }


        mLayoutManager = new LinearLayoutManager(NodeActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tronnodeListview.setLayoutManager(mLayoutManager);
        tronnodeListview.setNestedScrollingEnabled(false);

        nodeListAdapter = new NodeListAdapter(NodeActivity.this);
        tronnodeListview.setAdapter(nodeListAdapter);

        mPresenter = new NodePresenter(this);
        ((NodePresenter)mPresenter).setAdapterDataModel(nodeListAdapter);



        initNodeList();
    }

    private void initNodeList() {
        ((NodePresenter)mPresenter).getTronNodeList();
    }

    @Override
    public void displayNodeList() {
        tronnodeListview.setVisibility(View.VISIBLE);
    }

    @Override
    public void errorNodeList() {
        tronnodeListview.setVisibility(View.GONE);
        Toast.makeText(NodeActivity.this,"Failed to get node list.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
