package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.repository.AppRepository;

public class DBArticleDetailsViewModel extends ViewModel {
    private static final String TAG = DBArticleDetailsViewModel.class.getSimpleName();
    private final AppRepository repository;

    // init a mutable live data to listen for queries
    private final MutableLiveData<String> queryLiveData;

    public final LiveData<DBCompleteArticle> dbCompleteArticleLiveData;


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

    // Update article fav/not favourite
    public final void setArticleFavouriteState(@NonNull Boolean isFavourite, String articleId) {
        repository.setArticleFavouriteState(isFavourite, articleId);
    }

}
