package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.NYApiSearchResultObject;
import com.george.balasca.articleregistry.repository.AppRepository;
import com.george.balasca.articleregistry.repository.NetworkState;

public class DBArticleDetailsViewModel extends ViewModel {
    private static final String TAG = DBArticleDetailsViewModel.class.getSimpleName();
    private AppRepository repository;

    // init a mutable live data to listen for queries
    private MutableLiveData<String> queryLiveData;

    public LiveData<DBCompleteArticle> dbCompleteArticleLiveData;


    // constructor, init repo
    public DBArticleDetailsViewModel(@NonNull AppRepository repository) {
        this.repository = repository;

        queryLiveData = new MutableLiveData();


        dbCompleteArticleLiveData = Transformations.switchMap(queryLiveData, object ->
                repository.findDBCompleteArticleById(queryLiveData.getValue()));

    }

//    public DBCompleteArticle findDBCompleteArticleById(String id){
//        return repository.findDBCompleteArticleById(id);
//    }

    // Search Articles
    public final void searchArticle(@NonNull String queryString) {
        this.queryLiveData.postValue(queryString);
    }

}
