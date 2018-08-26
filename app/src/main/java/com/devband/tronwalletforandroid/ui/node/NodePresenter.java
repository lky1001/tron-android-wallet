package com.devband.tronwalletforandroid.ui.node;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.node.adapter.AdapterImmutableDataModel;

import org.tron.api.GrpcAPI;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NodePresenter extends BasePresenter<NodeView> {

    private AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel;
    private Tron mTron;
    private RxJavaSchedulers mRxJavaSchedulers;

    public NodePresenter(NodeView view, Tron tron, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void setAdapterDataModel(AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel) {
        this.adapterDataModel = adapterDataModel;
    }


    public void getTronNodeList(){
        mTron.getNodeList()
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<GrpcAPI.NodeList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(GrpcAPI.NodeList nodeList) {
                adapterDataModel.setModelList(nodeList);
                mView.displayNodeList(nodeList.getNodesCount());
            }

            @Override
            public void onError(Throwable e) {
                mView.errorNodeList();
                adapterDataModel.clear();
            }
        });
    }
}
