package com.devband.tronwalletforandroid.common;

import android.content.Context;

import com.devband.tronwalletforandroid.R;

import org.tron.protos.Protocol;

public class Utils {

    public static String getContractTypeString(Context context, int contractType) {

        String[] contractTypes = context.getResources().getStringArray(R.array.contract_types);

        try {
            if (contractType == Protocol.Transaction.Contract.ContractType.UNRECOGNIZED.getNumber()) {
                return context.getString(R.string.unrecognized_text);
            }
            return contractTypes[contractType];
        } catch (Exception e) {
            return context.getString(R.string.unrecognized_text);
        }
    }

    public static String getCommaNumber(int number) {
        return Constants.numberFormat.format(number);
    }

    public static String getCommaNumber(long number) {
        return Constants.numberFormat.format(number);
    }
}