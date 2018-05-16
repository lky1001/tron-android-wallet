package com.devband.tronwalletforandroid.network.model.reqeust;

public class TransactionsReqModel {
    private String hash;
    private int block;
    private String to;
    private String address;
    private String from;
    private String token;

    public TransactionsReqModel(String address) {
        this.address = address;
    }
}
