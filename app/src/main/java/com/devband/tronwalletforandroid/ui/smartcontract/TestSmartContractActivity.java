package com.devband.tronwalletforandroid.ui.smartcontract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.devband.tronlib.dto.TriggerRequest;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;

import org.spongycastle.util.encoders.Hex;
import org.tron.common.utils.AbiUtil;
import org.tron.common.utils.ByteArray;
import org.tron.core.exception.EncodingException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestSmartContractActivity extends CommonActivity {

    @Inject
    Tron mTron;

    @BindView(R.id.contract_address_edit)
    EditText contractAddressEdit;

    @BindView(R.id.abi_text)
    TextView abiText;

    @BindView(R.id.method_edit)
    EditText contractMethod;

    @BindView(R.id.params_edit)
    EditText contractParams;

    @BindView(R.id.fee_limit_edit)
    EditText feeLimit;

    @BindView(R.id.call_value_edit)
    EditText callValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_contract);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_abi_button)
    public void onGetAbiClick() {
        this.mTron.getSmartContract(contractAddressEdit.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contract -> abiText.setText(contract.getAbi().toString()), e -> e.printStackTrace());
    }

    @OnClick(R.id.send_transaction_button)
    public void onSendTransactionClick() {
        Single.fromCallable(() -> {
            String transferMethod = contractMethod.getText().toString();
            //String transferParams = contractParams.getText().toString();

            String transferParams = "\"address\",1000000";

            String contractTrigger = "";

            try {
                contractTrigger = AbiUtil.parseMethod(transferMethod, transferParams);
            } catch (EncodingException e ) {
                e.printStackTrace();
            }

            byte[] input = Hex.decode(contractTrigger);
            byte[] contractAddress = AccountManager.decodeFromBase58Check(contractAddressEdit.getText().toString());

            TriggerRequest triggerRequest = TriggerRequest.builder()
                    .contractAddress(ByteArray.toHexString(contractAddress))
                    .ownerAddress(ByteArray.toHexString(AccountManager.decodeFromBase58Check(mTron.getLoginAddress())))
                    .functionSelector(transferMethod)
                    .parameter(contractTrigger)
                    .callValue(Long.parseLong(callValue.getText().toString()))
                    .feeLimit(Long.parseLong(feeLimit.getText().toString()))
                    .build();

            //TriggerResult result = mTronGridService.triggerSmartContract(triggerRequest).blockingGet();
            return mTron.callQueryContract(mTron.getLoginAddress(), contractAddress, Long.parseLong(callValue.getText().toString()), input, Long.parseLong(feeLimit.getText().toString()), 0L, null);
        })
        .flatMap(result -> result)
        .subscribe(result -> {
            Timber.d("result : " + result);
        }, e -> e.printStackTrace());
    }
}
