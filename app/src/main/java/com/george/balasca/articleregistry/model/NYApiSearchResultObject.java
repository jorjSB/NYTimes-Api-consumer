package com.george.balasca.articleregistry.model;


import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.repository.NetworkState;

import java.util.ArrayList;

/**
 * RepoSearchResult from a search, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
public class NYApiSearchResultObject {
    private LiveData<PagedList<DBCompleteArticle>> articles;
    private LiveData<NetworkState> loadingState;
    private LiveData<ArrayList<String>> networkStatus;



    public LiveData<PagedList<DBCompleteArticle>> getArticles() {
        return articles;
    }

    public void setArticles(LiveData<PagedList<DBCompleteArticle>> articles) {
        this.articles = articles;
    }

    public LiveData<NetworkState> getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(LiveData<NetworkState> loadingState) {
        this.loadingState = loadingState;
    }

    public LiveData<ArrayList<String>> getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(LiveData<ArrayList<String>> networkStatus) {
        this.networkStatus = networkStatus;
    }
}
