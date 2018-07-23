package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.model.NYApiSearchResultObject;
import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.repository.AppRepository;

public class DBArticleListViewModel extends ViewModel {
    private static final String TAG = DBArticleListViewModel.class.getSimpleName();
    private AppRepository repository;

    // init a mutable live data to listen for queries
    private MutableLiveData<String> queryLiveData;

    // make the search after each new search item is posted with (searchRepo) using "map"
    private LiveData<NYApiSearchResultObject> repositoryResult;

    // get my Articles!!
    public LiveData<PagedList<Article>> articlesLiveData;

    // get teh Network errors!
    public LiveData<String> errorsLiveData;

    // constructor, init repo
    public DBArticleListViewModel(@NonNull AppRepository repository) {
        this.repository = repository;

        queryLiveData = new MutableLiveData();

        repositoryResult =  Transformations.map(queryLiveData, queryString -> {
            NYApiSearchResultObject result = repository.search(queryString);
            return result;
        });

        articlesLiveData = Transformations.switchMap(repositoryResult, object ->
                object.getArticles());

        errorsLiveData = Transformations.switchMap(repositoryResult, object ->
                object.getNetworkErrors());

    }


    // Search REPO
    public final void searchRepo(@NonNull String queryString) {
        this.queryLiveData.postValue(queryString);
    }

    // LAST Query string used
    public final String lastQueryValue() {
        return this.queryLiveData.getValue();
    }
}
