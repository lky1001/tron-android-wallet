package com.devband.tronwalletforandroid.network;

import com.devband.tronwalletforandroid.network.model.reqeust.TransactionsReqModel;
import com.devband.tronwalletforandroid.network.model.response.TransactionsModel;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiCall extends BaseApiCall {

    private static ApiCall instance;

    public static synchronized ApiCall getInstance() {
        if (instance == null) {
            instance = new ApiCall();
        }
        return instance;
    }

    private final ApiService api;

    private ApiCall() {
        super();



//        public static ApiService getServiceForRx() {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(buildHttpClient())
                    .baseUrl(Environments.B612_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
            return retrofit.create(ApiService.class);
//        }

        api = ApiClient.getServiceForRx();
    }


    public Single<TransactionsModel> getTransactions(String address) {
        TransactionsReqModel model = new TransactionsReqModel(address);
        return call(api.device(m));
    }
}
