package com.george.balasca.articleregistry.repository.networkdatasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.george.balasca.articleregistry.api.Api;
import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.model.modelobjects.ApiResponse;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemPositionalDataSource extends PositionalDataSource<Article> {

    private static final String TAG = ItemPositionalDataSource.class.getSimpleName();

    private Service service;

    private LoadRangeParams afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;
    private LoadInitialParams initialParams;

    public ItemPositionalDataSource(Executor retryExecutor) {
        // service = Api.createService(context);
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.retryExecutor = retryExecutor;
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Article> callback) {
        Log.d(TAG, "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);


        initialParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        service.getQueriedArticles(params.requestedStartPosition, "trump").enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<Article> articleList = new ArrayList();
                    articleList.addAll( response.body().getResponseBody().getArticleList() );

                    callback.onResult(articleList, 0, 10);

                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    initialParams = null;
                } else {
                    Log.e("API call failed", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "Unknown error received!";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Article> callback) {
        Log.d(TAG, "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize);

        afterParams = params;
        networkState.postValue(NetworkState.LOADING);


        service.getQueriedArticles(params.startPosition/10, "trump").enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    ArrayList<Article> articleList = new ArrayList();
                    articleList.addAll( response.body().getResponseBody().getArticleList() );

                    callback.onResult(articleList);

                    networkState.postValue(NetworkState.LOADED);
                    initialParams = null;
                } else {
                    Log.e("API call failed", "REASON: " + response.message());
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String postError = null;
                        postError = jObjError.optString("message");
                        if(postError.isEmpty())
                            postError = jObjError.optString("errors");

                        networkState.postValue(new NetworkState(Status.FAILED, postError));
                    } catch (Exception e) {
                        Log.d(TAG, "Response status errorBody RAISED Exception: " + e.getMessage());
                    }


                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "Unknown error received!";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });
    }
}
