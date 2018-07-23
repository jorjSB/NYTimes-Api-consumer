package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.util.Log;

import com.george.balasca.articleregistry.model.modelobjects.Article;

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
    }

    public void insertAllArticles(List<Article> articleArrayList){
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Inserting " + articleArrayList.size() + " articles");
                articleDao.insert(articleArrayList);
            }
        });
    }

    public DataSource.Factory<Integer, Article> getAllArticles() {
        DataSource.Factory<Integer, Article> articleFactory = articleDao.getAllArticles();
        return articleFactory;
    }

}
