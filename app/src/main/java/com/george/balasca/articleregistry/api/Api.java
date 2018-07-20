package com.george.balasca.articleregistry.api;

import android.appwidget.AppWidgetProviderInfo;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class Api {
    private static final String TAG = Api.class.getSimpleName();
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY = "3baa17f100794f77a741c475c8916700";

    public static Service createService() {
        // Logger
        HttpLoggingInterceptor loggerInterceptor = new HttpLoggingInterceptor();
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Use 1sec between calls to avoid API limit exceeding
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        Interceptor delayInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SystemClock.sleep(1000);
                return chain.proceed(chain.request());
            }
        };

        // Add akp-key to all requests (needed by the API)
        Interceptor apiKeyInterceptor =new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api-key", API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggerInterceptor)
                .addInterceptor(delayInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL);

        return builder.build().create(Service.class);
    }
}
