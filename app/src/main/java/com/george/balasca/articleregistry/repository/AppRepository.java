package com.george.balasca.articleregistry.repository;

import android.arch.paging.DataSource;
import android.util.Log;

import com.george.balasca.articleregistry.api.Service;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {

    private static final String TAG = AppRepository.class.getSimpleName();
    private Service service;
    private LocalCache localCache;


    public AppRepository(Service service, LocalCache localCache) {
        this.service = service;
        this.localCache = localCache;
    }

    /**
     * Search - match the query.
     */
    public ArrayList<Article> search(String q){
        Log.d(TAG, "New query: " + q);

        return null;
    }


    public DataSource.Factory getAllArticles() {
        return localCache.getAllArticles();
    }

    public void insertAllArticles(ArrayList<Article> articleList) {
        localCache.insertAllArticles(articleList);
    }
}
