package com.george.balasca.articleregistry.model;

import android.arch.paging.PagedList;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.apiresponse.ApiResponse;
import com.george.balasca.articleregistry.model.apiresponse.Article;

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

    public ArticleBoundaryCallback(String query, Service service, LocalCache cache) {
        this.query = query;
        this.service = service;
        this.cache = cache;
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @Override
    public void onZeroItemsLoaded(){
        // fetch data from service
         requestAndSaveData(query);
        Log.d(TAG , "onZeroItemsLoaded");
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    @Override
    public void onItemAtEndLoaded(Article article){
        // fetch data from service
         requestAndSaveData(query);
        Log.d(TAG , "onItemAtEndLoaded ");
    }

    /**
     * Request next page from the server and save it into the DB
     */
    private void requestAndSaveData(String query){

        service.getQueriedArticles(lastRequestedPage, query).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    Log.d(TAG, "API call success " + response.body().getResponseBody().getArticleList().size() + " items loaded page: " + lastRequestedPage);
                    lastRequestedPage++;

                    List<Article> articleList = new ArrayList();
                    articleList.addAll( response.body().getResponseBody().getArticleList() );

                     cache.insertAllArticles(articleList);
//                    callback.onResponseResult(articleList, 0, 10);

//                    initialLoading.postValue(NetworkState.LOADED);
//                    networkState.postValue(NetworkState.LOADED);
                } else {
                    Log.e("API call failed", response.message());
//                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
//                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "Unknown error received!";
                }
//                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }


}
