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

