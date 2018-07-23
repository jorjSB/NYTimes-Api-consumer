package com.george.balasca.articleregistry.model;


import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.model.apiresponse.Article;

/**
 * RepoSearchResult from a search, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
public class NYApiSearchResultObject {
    private LiveData<PagedList<Article>> articles;
    private LiveData<String> networkErrors;

    public LiveData<PagedList<Article>> getArticles() {
        return articles;
    }

    public void setArticles(LiveData<PagedList<Article>> articles) {
        this.articles = articles;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    public void setNetworkErrors(LiveData<String> networkErrors) {
        this.networkErrors = networkErrors;
    }
}
