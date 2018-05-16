package com.devband.tronwalletforandroid.network;

import android.util.Log;

import com.devband.tronwalletforandroid.network.model.response.BaseModel;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;

class BaseApiCall {
//    private static final String TAG = "##ApiError" + ApiException.class.getSimpleName();

    private Transform transform;

    protected BaseApiCall() {
        transform = new Transform();
    }

    private static OkHttpClient buildHttpClient() {
        TokenMigration.getInstance().waitForMigrate();
        return HttpClientFactory.INSTANCE.get();
    }

    public static ApiService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(buildHttpClient())
                .baseUrl(Environments.B612_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }


    protected  <T extends BaseModel> Single<T> call(Single<Response<T>> apiService) {
        return Single.just(apiService)
                .flatMap(api -> {
//                    if (!NetworkUtils.isNetworkAvailable()){
//                        return Single.error(new ApiException(ApiError.NETWORK));
//                    }
                    return api.compose(transform.transform());
                });
    }

    private class Transform {
        public <T extends BaseModel> SingleTransformer<Response<T>, T> transform() {
            StringBuilder requestUrlSb = new StringBuilder();
            return upstream ->
                    upstream
                            .doOnSuccess(response -> {
                                String requestUrl = response.raw().request().url().toString();
                                requestUrlSb.append(requestUrl);
                                Log.d("##Api", requestUrl + " onSuccess");
                            })
                            .flatMap(checkErrorCode())
                            .doOnError(error -> {
//                                if (!(error instanceof ApiException)) {
//                                    Log.e(TAG, requestUrlSb.toString() + " error - Exception : " + error.getMessage());
//                                    return;
//                                }
//
//                                ApiException apiException = (ApiException) error;
//                                StringBuilder sb = new StringBuilder(requestUrlSb.toString() + " ApiException error").append("\n");
//                                sb.append("ApiException message : ").append(apiException.apiError.errorMessage).append("\n");
//                                sb.append("ApiException errorType : ").append(apiException.apiError.errorType.name()).append("\n");
//                                Log.e(TAG, sb.toString());
                            });
        }

        private <T extends BaseModel> Function<Response<T>, Single<? extends T>> checkErrorCode() {
            return response -> {
//                if (!NetworkUtils.isNetworkAvailable()) {
//                    return Single.error(new ApiException(ApiError.NETWORK));
//                }
//                if (response == null || !response.isSuccessful()) {
//                    return Single.error(new ApiException(ApiError.UNKNOWN));
//                }
//
//                if (!response.body().isSuccess()) {
//                    ApiError apiError = ApiError.parse(response.body());
//                    return Single.error(new ApiException(apiError));
//                }

                T body = response.body();
//                setEtag(response.headers().get("ETag"), body);

                return Single.just(body);
            };
        }

//        private <T extends BaseModel> void setEtag(String etag, T body) {
//            if (TextUtils.isEmpty(etag) || body == null) {
//                return;
//            }
//
//            if (body instanceof BaseResponse) {
//                ((BaseResponse) body).etag = etag;
//            } else if (body instanceof RawResponse) {
//                ((RawResponse) body).etag = etag;
//            }
//        }
    }
}
