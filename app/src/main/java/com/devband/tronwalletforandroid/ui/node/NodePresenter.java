package com.devband.tronwalletforandroid.ui.node;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.node.adapter.AdapterImmutableDataModel;

import org.tron.api.GrpcAPI;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NodePresenter extends BasePresenter<NodeView> {

    private AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel;

    public NodePresenter(NodeView view) {
        super(view);
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
        Tron.getInstance(mContext).getNodeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
