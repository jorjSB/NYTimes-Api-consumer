package com.george.balasca.articleregistry.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.NYApiSearchResultObject;
import com.george.balasca.articleregistry.model.ArticleBoundaryCallback;
import com.george.balasca.articleregistry.model.apiresponse.Article;

public class AppRepository {

    private static final String TAG = AppRepository.class.getSimpleName();
    private static final int DATABASE_PAGE_SIZE = 20;
    private Service service;
    private LocalCache cache;
    private LiveData<PagedList<Article>> mPagedListLiveData;

    public AppRepository(Service service, LocalCache cache) {
        this.service = service;
        this.cache = cache;
    }

    /**
     * Search - match the query.
     */
    public NYApiSearchResultObject search(String q){
        Log.d(TAG, "New query: " + q);

        // Get data source factory from the local cache
        DataSource.Factory dataSourceFactory = cache.getAllArticles();

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        ArticleBoundaryCallback boundaryCallback = new ArticleBoundaryCallback(q, service, cache);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(20)
                        .setPrefetchDistance(10)
                        .build();

        // Get the paged list
        LiveData data = new LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                .setBoundaryCallback(boundaryCallback)
                .build();

        mPagedListLiveData = data;

        NYApiSearchResultObject NYApiSearchResultObject = new NYApiSearchResultObject();
        NYApiSearchResultObject.setArticles(data);
//        NYApiSearchResultObject.setNetworkErrors(new ArrayList<String>());

        return NYApiSearchResultObject;
    }

    public DataSource.Factory<Integer, Article> getAllArticles(){
        return cache.getAllArticles();
    }

}
