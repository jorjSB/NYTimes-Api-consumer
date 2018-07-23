package com.george.balasca.articleregistry.model;


import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.NetworkState;

/**
 * RepoSearchResult from a search, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
public class NYApiSearchResultObject {
    private LiveData<PagedList<Article>> articles;
    private LiveData<NetworkState> loadingState;
    private LiveData<String> networkStatus;



    public LiveData<PagedList<Article>> getArticles() {
        return articles;
    }

    public void setArticles(LiveData<PagedList<Article>> articles) {
        this.articles = articles;
    }

    public LiveData<NetworkState> getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(LiveData<NetworkState> loadingState) {
        this.loadingState = loadingState;
    }

    public LiveData<String> getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(LiveData<String> networkStatus) {
        this.networkStatus = networkStatus;
    }
}
