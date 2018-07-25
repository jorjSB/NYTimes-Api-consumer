package com.george.balasca.articleregistry.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.api.helpers.ErrorPojoClass;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.modelobjects.ApiResponse;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class ArticleBoundaryCallback extends PagedList.BoundaryCallback<DBCompleteArticle>{
    private final String TAG = ArticleBoundaryCallback.class.getSimpleName();

    private int lastRequestedPage = 0;

    private SearchQueryPOJO searchQueryPOJO;
    private Service service;
    private LocalCache cache;

    private MutableLiveData networkState;
    private MutableLiveData networkErrors;


    public ArticleBoundaryCallback(SearchQueryPOJO _searchQueryPOJO, Service service, LocalCache cache) {
        this.searchQueryPOJO = _searchQueryPOJO;
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
        requestAndSaveData(searchQueryPOJO);
//        Log.d(TAG , "onZeroItemsLoaded");
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    @Override
    public void onItemAtEndLoaded(DBCompleteArticle article){
        // fetch data from service
        requestAndSaveData(searchQueryPOJO);
//        Log.d(TAG , "onItemAtEndLoaded ");
    }

    /**
     * Request next page from the server and save it into the DB
     */
    private void requestAndSaveData(SearchQueryPOJO searchQueryPOJO){

        networkState.postValue(NetworkState.LOADING);

        service.getQueriedFilteredArticles(lastRequestedPage,
                searchQueryPOJO.getQuery(),
                searchQueryPOJO.getCategory(),
                searchQueryPOJO.getSort(),
                searchQueryPOJO.getBegin_date(),
                searchQueryPOJO.getEnd_date()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {

                    Log.d(TAG, "API call success " + response.body().getResponseBody().getArticleList().size() + " items loaded page: " + lastRequestedPage);
                    lastRequestedPage++;

                    List<Article> articleList = new ArrayList();
                    articleList.addAll( response.body().getResponseBody().getArticleList() );

                    // INSERT COMPLETE ARTICLES INTO THE DB
                     cache.insertAllArticles(articleList);

                    networkState.postValue(NetworkState.LOADED);
                } else if (!response.isSuccessful()){ // response.code() != 200
                    Log.e("API call failed", "response.message() " + response.message() );
                    networkState.postValue(NetworkState.FAILED);

                    // get the exact error from the API response
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError=new ErrorPojoClass();
                    ArrayList<String> errorsArray = new ArrayList<String>();

                    try {
                        mError= gson.fromJson(response.errorBody().string(),ErrorPojoClass.class);
                    } catch (IOException e) {
                        mError.setMessage("Unknown error");
                    }

                    // in case it's the "message" error returned
                    if(mError.getMessage() != null) {
                        errorsArray.add(mError.getMessage());
                        mError.setErrors(errorsArray);
                    }

                    if(mError.getErrors() != null && mError.getErrors().size() > 0)
                        networkErrors.postValue(mError.getErrors());
                }else{
                    Log.e("API call failed", "response.message() " + response.message() );
                    networkState.postValue(NetworkState.FAILED);

                    ArrayList<String> errors = new ArrayList<String>();
                    errors.add(response.message());
                    networkErrors.postValue(errors);
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

                ArrayList<String> errors = new ArrayList<String>();
                errors.add(errorMessage);
                networkErrors.postValue(errors);
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
