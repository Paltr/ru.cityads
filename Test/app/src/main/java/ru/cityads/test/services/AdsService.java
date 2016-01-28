package ru.cityads.test.services;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import ru.cityads.test.BuildConfig;
import rx.Observable;

/**
 * Service used to request ads.
 *
 * @see AdResponse
 */
public final class AdsService
{
    private final RemoteInterface remoteInterface;

    public AdsService()
    {
        final OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(BuildConfig.HTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(BuildConfig.HTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClient.setReadTimeout(BuildConfig.HTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS);

        final Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .build();

        remoteInterface = retrofit.create(RemoteInterface.class);
    }

    public Observable<AdResponse> requestAd(String requesterId)
    {
        return remoteInterface.requestAd(requesterId);
    }

    //region Interface that represents remote ad service. Is implemented by Retrofit.

    private interface RemoteInterface
    {
        @FormUrlEncoded
        @POST("/adviator/index.php")
        Observable<AdResponse> requestAd(@Field("id") String id);
    }

    //endregion
}
