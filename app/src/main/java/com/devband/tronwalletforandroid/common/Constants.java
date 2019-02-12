package com.devband.tronwalletforandroid.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);

    public static final DecimalFormat tokenBalanceFormat = new DecimalFormat("#,##0.########");
    public static final DecimalFormat numberFormat = new DecimalFormat("#,##0");
    public static final DecimalFormat usdFormat = new DecimalFormat("#,##0.000");
    public static final  DecimalFormat percentFormat = new DecimalFormat("#,##0.00");

    public static final String TRON_SYMBOL = "TRX";
    public static final double ONE_TRX = 1_000_000;
    public static final int TRX_PRECISION = 6;
    public static final String PREFIX_ACCOUNT_NAME = "Account";
    public static final long FREEZE_DURATION = 3;

    public static final float VOTE_MAX_PROGRESS = 10_000f;
    public static final int SUPER_REPRESENTATIVE_COUNT= 27;
    public static final String TRON_COINMARKET_NAME = "tronix";

    public static final String SUPER_REPRESENTATIVE_TEAM_PAGE_URL = "https://tronscan.org/#/representative/";
    public static final int CONNECTION_RETRY = 10;
    public static final long GRPC_TIME_OUT_IN_MS = 2 * 1000;

    public static final String DB_NAME = "tron_wallet_db";

    public static final int SALT_LOG_ROUND = 10;
    public static final String ALIAS_SALT = "alias_salt";
    public static final String ALIAS_ACCOUNT_KEY = "alias_account_key";
    public static final String ALIAS_PASSWORD_KEY = "alias_password_key";
    public static final String ALIAS_ADDRESS_KEY = "alias_address_key";

    public static final int TOKEN_TRC_10 = 10;
    public static final int TOKEN_TRC_20 = 20;
}
