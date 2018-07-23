package com.george.balasca.articleregistry.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.util.Log;
import android.widget.Toast;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.api.helpers.ErrorPojoClass;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.modelobjects.ApiResponse;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This boundary callback gets notified when user reaches to the edges of the list for example when
 * the database cannot provide any more data.
 **/
public class ArticleBoundaryCallback extends PagedList.BoundaryCallback<Article>{
    private final String TAG = ArticleBoundaryCallback.class.getSimpleName();

    private int lastRequestedPage = 0;

    private String query;
    private Service service;
    private LocalCache cache;

    private MutableLiveData networkState;
    private MutableLiveData networkErrors;


    public ArticleBoundaryCallback(String query, Service service, LocalCache cache) {
        this.query = query;
        this.service = service;
        this.cache = cache;

        networkState = new MutableLiveData();
        networkErrors = new MutableLiveData();
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @Override
    public void onZeroItemsLoaded(){
        // fetch data from service
        requestAndSaveData(query);
//        Log.d(TAG , "onZeroItemsLoaded");
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    @Override
    public void onItemAtEndLoaded(Article article){
        // fetch data from service
        requestAndSaveData(query);
//        Log.d(TAG , "onItemAtEndLoaded ");
    }

    /**
     * Request next page from the server and save it into the DB
     */
    private void requestAndSaveData(String query){


        networkState.postValue(NetworkState.LOADING);

        service.getQueriedArticles(lastRequestedPage, query).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    Log.d(TAG, "API call success " + response.body().getResponseBody().getArticleList().size() + " items loaded page: " + lastRequestedPage);
                    lastRequestedPage++;

                    List<Article> articleList = new ArrayList();
                    articleList.addAll( response.body().getResponseBody().getArticleList() );

                    cache.insertAllArticles(articleList);

                    networkState.postValue(NetworkState.LOADED);
                    // networkErrors.postValue("All GOOD");
                } else if (!response.isSuccessful()){ // response.code() != 200
                    Log.e("API call failed", "response.message() " + response.message() );
                    networkState.postValue(NetworkState.FAILED);

                    // get the exact error from the API response
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError=new ErrorPojoClass();
                    try {
                        mError= gson.fromJson(response.errorBody().string(),ErrorPojoClass .class);
                    } catch (IOException e) {
                        mError.setMessage("Unknown error");
                    }
                    networkErrors.postValue(mError.getErrors() != null ? mError.getErrors() : mError.getMessage() );
                }else{
                    Log.e("API call failed", "response.message() " + response.message() );
                    networkState.postValue(NetworkState.FAILED);
                    networkErrors.postValue(response.message());
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "Unknown error received!";
                }
                networkState.postValue(NetworkState.FAILED);
                networkErrors.postValue(errorMessage);
                Log.e("API call failed", "errorMessage: " + errorMessage );
            }
        });

    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getNetworkErrors() {
        return networkErrors;
    }
}
