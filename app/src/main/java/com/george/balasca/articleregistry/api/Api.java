package com.george.balasca.articleregistry.api;

import android.content.Context;
import android.os.SystemClock;

import com.george.balasca.articleregistry.api.helpers.ConnectivityInterceptor;

import java.io.IOException;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface Api {
    String TAG = Api.class.getSimpleName();
    String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    public  final static String IMAGES_BASE_URL = "https://www.nytimes.com/";
    String API_KEY = "3baa17f100794f77a741c475c8916700";

    public static Service createService(Context context) {
        // Logger
        HttpLoggingInterceptor loggerInterceptor = new HttpLoggingInterceptor();
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

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
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("api-key", API_KEY)
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
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
