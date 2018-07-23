package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.util.Log;

import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
public class LocalCache {
    private static final String TAG = LocalCache.class.getSimpleName();
    private ArticleDao articleDao;
    private Executor ioExecutor;

    public LocalCache(AppDatabase appDatabase, Executor ioExecutor) {
        this.articleDao = appDatabase.getArticleDao();
        this.ioExecutor = ioExecutor;

        Log.d(TAG, "New cache created!!");
    }

    public void insertAllArticles(List<Article> articleArrayList){
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "inserting " + articleArrayList.size() + " repos");
                articleDao.insert(articleArrayList);
            }
        });
    }

    public void otherFunction(ArrayList<Article> articleArrayList){
        // TODO
    }

    public DataSource.Factory<Integer, Article> getAllArticles() {
        DataSource.Factory<Integer, Article> test = articleDao.getAllArticles();
        return test;
    }

}
