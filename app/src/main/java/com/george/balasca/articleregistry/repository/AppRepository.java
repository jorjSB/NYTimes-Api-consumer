package com.george.balasca.articleregistry.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.NYApiSearchResultObject;
import com.george.balasca.articleregistry.model.ArticleBoundaryCallback;
import com.george.balasca.articleregistry.model.SearchQueryPOJO;
import com.george.balasca.articleregistry.model.modelobjects.Article;

public class AppRepository {

    private static final String TAG = AppRepository.class.getSimpleName();
    private static final int DATABASE_PAGE_SIZE = 20;
    private Service service;
    private LocalCache cache;
    private LiveData<PagedList<Article>> mPagedListLiveData;
    private LiveData<PagedList<DBCompleteArticle>> liveFavouriteArticlesList;

    public AppRepository(Service service, LocalCache cache) {
        this.service = service;
        this.cache = cache;

        DataSource.Factory dataSourceFactory = cache.getfavouritesDBCompleteArticle();
        LiveData data = new LivePagedListBuilder(dataSourceFactory, 20)
                .build();
        liveFavouriteArticlesList = data;
    }

    /**
     * Search - match the query.
     * @param q
     */
    public NYApiSearchResultObject search(SearchQueryPOJO q){
        Log.d(TAG, "New query: " + q);

        // delete old articles
        cache.deleteObsoleteArticles();

        // Get data source factory from the local cache
        DataSource.Factory dataSourceFactory = cache.getDBCompleteArticles();

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        ArticleBoundaryCallback boundaryCallback = new ArticleBoundaryCallback(q, service, cache);

        // Get the paged list
        LiveData data = new LivePagedListBuilder(dataSourceFactory, 20)
                .setBoundaryCallback(boundaryCallback)
                .build();

        mPagedListLiveData = data;

        // new object with results - observables
        NYApiSearchResultObject NYApiSearchResultObject = new NYApiSearchResultObject();

        NYApiSearchResultObject.setArticles(data);
        NYApiSearchResultObject.setLoadingState(boundaryCallback.getNetworkState());
        NYApiSearchResultObject.setNetworkStatus(boundaryCallback.getNetworkErrors());

        return NYApiSearchResultObject;
    }

    public DataSource.Factory<Integer, Article> getAllArticles(){
        return cache.getAllArticles();
    }

    public LiveData<DBCompleteArticle> findDBCompleteArticleById(String id){
        return cache.findDBCompleteArticleById(id);
    }

    // get live paged list with favourite articles from the DB
    public LiveData<PagedList<DBCompleteArticle>> getfavouritesDBCompleteArticle(){
        return liveFavouriteArticlesList;
    }

    public void setArticleFavouriteState(Boolean isFavourite, String articleId) {
        cache.setArticleFavouriteState(isFavourite, articleId);
    }
}
