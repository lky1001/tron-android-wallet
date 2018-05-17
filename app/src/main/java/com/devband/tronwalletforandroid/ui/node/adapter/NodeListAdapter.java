package com.devband.tronwalletforandroid.ui.node.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;

import org.tron.api.GrpcAPI;


import butterknife.BindView;
import butterknife.ButterKnife;

public class NodeListAdapter extends RecyclerView.Adapter<NodeListAdapter.MyNodeViewHolder> implements AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node>, AdapterView {

    private GrpcAPI.NodeList nodeList;
    private Context context;


    public NodeListAdapter(Context context) {
        this.context = context;
        this.nodeList=null;
    }

    @NonNull
    @Override
    public NodeListAdapter.MyNodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_node, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new NodeListAdapter.MyNodeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeListAdapter.MyNodeViewHolder holder, int position) {
        GrpcAPI.Node node = nodeList.getNodes(position);

        holder.tronNodeIp.setText(node.getAddress().getHost().toStringUtf8()+":"+String.valueOf(node.getAddress().getPort()));

    }

    @Override
    public int getItemCount() {
        return nodeList.getNodesCount();
    }


    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public GrpcAPI.Node getModel(int position) {
        return nodeList.getNodes(position);
    }

    @Override
    public int getSize() {
        return getItemCount();
    }

    @Override
    public void setModelList(GrpcAPI.NodeList nodeList) {
        this.nodeList = nodeList;
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.nodeList = null;
        notifyDataSetChanged();
    }

    public class MyNodeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tron_node_ip_text)
        TextView tronNodeIp;

        public MyNodeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
