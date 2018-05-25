package com.devband.tronwalletforandroid.common;

import java.text.DecimalFormat;

public class Constants {

    public static final DecimalFormat tronBalanceFormat = new DecimalFormat("#,##0.000000");
    public static final DecimalFormat numberFormat = new DecimalFormat("#,##0");
    public static final DecimalFormat usdFormat = new DecimalFormat("#,##0.000");
    public static final  DecimalFormat percentFormat = new DecimalFormat("#,##0.00");

    public static final String TRON_SYMBOL = "TRX";
    public static final double ONE_TRX = 1_000_000;
    public static final int TRX_DECIMALS = 6;
    public static final String PREFIX_ACCOUNT_NAME = "Account";
    public static final long FREEZE_DURATION = 3;

    public static final float VOTE_MAX_PROGRESS = 10_000f;
    public static final int SUPER_REPRESENTATIVE_COUNT= 27;
    public static final String TRON_COINMARKET_NAME = "tronix";

    public static final String SUPER_REPRESENTATIVE_TEAM_PAGE_URL = "https://tronscan.org/#/representative/";
}
