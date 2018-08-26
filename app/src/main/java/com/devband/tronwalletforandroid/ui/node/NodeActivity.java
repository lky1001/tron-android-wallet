package com.devband.tronwalletforandroid.ui.node;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.node.adapter.NodeListAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NodeActivity extends CommonActivity implements NodeView {

    @Inject
    NodePresenter mNodePresenter;
    
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tronnode_listview)
    RecyclerView tronnodeListview;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.nodelist_title_text)
    TextView nodelistTitleText;
    @BindView(R.id.count_node_text)
    TextView countNodeText;

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
            getSupportActionBar().setTitle("");
        }


        mLayoutManager = new LinearLayoutManager(NodeActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tronnodeListview.setLayoutManager(mLayoutManager);
        tronnodeListview.setNestedScrollingEnabled(false);

        nodeListAdapter = new NodeListAdapter(NodeActivity.this);
        tronnodeListview.setAdapter(nodeListAdapter);


        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setTitle(getString(R.string.title_node_list));
                    nodelistTitleText.setVisibility(View.GONE);
                    countNodeText.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    toolbarLayout.setTitle("");
                    nodelistTitleText.setVisibility(View.VISIBLE);
                    countNodeText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });


        mNodePresenter.setAdapterDataModel(nodeListAdapter);

        initNodeList();
    }

    private void initNodeList() {
        showProgressDialog(null, getString(R.string.loading_msg));
        mNodePresenter.getTronNodeList();
    }

    @Override
    public void displayNodeList(int count) {
        hideDialog();
        tronnodeListview.setVisibility(View.VISIBLE);
        countNodeText.setText("Found "+count+" node(s).");
    }

    @Override
    public void errorNodeList() {
        hideDialog();
        tronnodeListview.setVisibility(View.GONE);
        Toast.makeText(NodeActivity.this, "Failed to get node list.", Toast.LENGTH_SHORT).show();
    }
}
