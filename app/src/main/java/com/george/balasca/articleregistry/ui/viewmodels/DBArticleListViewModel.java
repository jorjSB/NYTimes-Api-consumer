package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.NYApiSearchResultObject;
import com.george.balasca.articleregistry.model.SearchQueryPOJO;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.AppRepository;
import com.george.balasca.articleregistry.repository.NetworkState;

import java.util.ArrayList;

public class DBArticleListViewModel extends ViewModel {
    private static final String TAG = DBArticleListViewModel.class.getSimpleName();
    private AppRepository repository;

    // init a mutable live data to listen for queries
    private MutableLiveData<SearchQueryPOJO> queryLiveData;

    // make the search after each new search item is posted with (searchArticle) using "map"
    private LiveData<NYApiSearchResultObject> repositoryResult;

    // get my Articles!!
    public LiveData<PagedList<DBCompleteArticle>> articlesLiveData;

    // get my fav. Articles!!
    public LiveData<PagedList<DBCompleteArticle>> favouriteArticlesLiveData;

    // get teh Network errors!
    public LiveData<NetworkState> networkLoadingStateLiveData;

    // get teh Network errors!
    public LiveData<ArrayList<String>> networkErrorsLiveData;

    // constructor, init repo
    public DBArticleListViewModel(@NonNull AppRepository repository) {
        this.repository = repository;

        queryLiveData = new MutableLiveData();

        repositoryResult =  Transformations.map(queryLiveData, queryObject -> {
            NYApiSearchResultObject result = repository.search(queryObject);
            return result;
        });

        articlesLiveData = Transformations.switchMap(repositoryResult, object ->
                object.getArticles());

        networkLoadingStateLiveData = Transformations.switchMap(repositoryResult, object ->
                object.getLoadingState());

        networkErrorsLiveData = Transformations.switchMap(repositoryResult, object ->
                object.getNetworkStatus());

    }

    // Search Article(s)
    public final void searchArticle( @NonNull SearchQueryPOJO searchQueryPOJO) {
        this.queryLiveData.postValue(searchQueryPOJO);
    }

    // LAST Query string used
    public final SearchQueryPOJO lastQueryValue() {
        return this.queryLiveData.getValue();
    }

    public final LiveData<PagedList<DBCompleteArticle>> getfavouritesDBCompleteArticle(){
        return repository.getfavouritesDBCompleteArticle();
    }
}
