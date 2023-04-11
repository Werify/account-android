package net.werify.id.internal;

import android.content.Context;

import net.werify.id.NetworkDataSource;
import net.werify.id.common.WConstants;
import net.werify.id.retrofit.NetworkApi;
import net.werify.id.retrofit.RetrofitWerifyNetwork;
import net.werify.id.retrofit.WerifyConfigure;
import net.werify.id.utils.Utils;
import net.werify.official.BuildConfig;

import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternalNetworking {

    private InternalNetworking() {

    }

    public static OkHttpClient sHttpClient;
    public static String sUserAgent = null;
    public static NetworkDataSource sNetworkDataSource;

    public static OkHttpClient getClient() {
        return sHttpClient;
    }

    public static OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static void setClientWithCache(Context context, WerifyConfigure configure) {

        String url = BuildConfig.BACKEND_URL;
        if (configure != null) {
            sHttpClient = new OkHttpClient().newBuilder()
                    .cache(Utils.INSTANCE.getCache(context, WConstants.MAX_CACHE_SIZE, WConstants.CACHE_DIR_NAME))
                    .connectTimeout(configure.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(configure.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(configure.getWriteTimeout(), TimeUnit.SECONDS)
                    .build();
            url = configure.getUrl();
        } else {
            sHttpClient = getDefaultClient();
        }


    }


    public static void setUserAgent(String userAgent) {
        sUserAgent = userAgent;
    }

    public static void setClient(OkHttpClient okHttpClient) {
        sHttpClient = okHttpClient;
    }

    public static void enableLogging(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(level);
        sHttpClient = sHttpClient
                .newBuilder()
                .addInterceptor(logging)
                .build();
    }

}
