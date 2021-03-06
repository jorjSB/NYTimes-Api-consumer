package com.george.balasca.articleregistry.api.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.george.balasca.articleregistry.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

        private final Context mContext;

        public ConnectivityInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (!isOnline(mContext)) {
                throw new NoConnectivityException();
            }

            Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());
        }



    private static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return mContext.getResources().getString(R.string.no_internet_connection);
        }
    }
}