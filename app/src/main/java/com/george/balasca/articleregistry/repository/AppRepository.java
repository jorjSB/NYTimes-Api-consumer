package com.george.balasca.articleregistry.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.ApiSearchResultObject;
import com.george.balasca.articleregistry.model.ArticleBoundaryCallback;
import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;

public class AppRepository {

    private static final String TAG = AppRepository.class.getSimpleName();
    private static final int DATABASE_PAGE_SIZE = 20;
    private Service service;
    private LocalCache localCache;
    private LiveData<PagedList<Article>> mPagedListLiveData;

    public AppRepository(Service service, LocalCache localCache) {
        this.service = service;
        this.localCache = localCache;
    }

    /**
     * Search - match the query.
     */
    public ApiSearchResultObject search(String q){
        Log.d(TAG, "New query: " + q);

        // Get data source factory from the local cache
        DataSource.Factory dataSourceFactory = localCache.getAllArticles();

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        ArticleBoundaryCallback boundaryCallback = new ArticleBoundaryCallback(q, service, localCache);

        // Get the paged list
        LiveData data = new LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build();

        mPagedListLiveData = data;

        ApiSearchResultObject apiSearchResultObject = new ApiSearchResultObject();
        apiSearchResultObject.setArticles(data);
//        apiSearchResultObject.setNetworkErrors(new ArrayList<String>());

        return apiSearchResultObject;
    }


    /**

     /**
     * Search repositories whose names match the query.
     *
    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.reposByName(query)

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = RepoBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return RepoSearchResult(data, networkErrors)
    }
     
     
     */
    
    

    public DataSource.Factory getAllArticles() {
        return localCache.getAllArticles();
    }

    public void insertAllArticles(ArrayList<Article> articleList) {
        localCache.insertAllArticles(articleList);
    }
}
