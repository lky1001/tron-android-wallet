package com.devband.tronwalletforandroid.ui.node;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.node.adapter.AdapterImmutableDataModel;

import org.tron.api.GrpcAPI;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NodePresenter extends BasePresenter<NodeView> {

    private AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel;
    private Tron mTron;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public NodePresenter(NodeView view, Tron tron, Scheduler processScheduler, Scheduler observerScheduler) {
        super(view);
        this.mTron = tron;
        this.mProcessScheduler = processScheduler;
        this.mObserverScheduler = observerScheduler;
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
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
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
