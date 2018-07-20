package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.util.Log;

import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;
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

    public void insertAllArticles(ArrayList<Article> articleArrayList){
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
            Log.d(TAG, "inserting " + articleArrayList.size() + " repos");
            articleDao.insertArticles(articleArrayList);
            }
        });
    }

    public void otherFunction(ArrayList<Article> articleArrayList){
        // TODO
    }

    public DataSource.Factory getAllArticles() {
        return articleDao.getAllArticles();
    }


//
//    /**
//     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
//     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
//     * any characters between the words.
//     * @param name repository name
//     */
//    fun reposByName(name: String): DataSource.Factory<Int, Repo> {
//        // appending '%' so we can allow other characters to be before and after the query string
//        val query = "%${name.replace(' ', '%')}%"
//        return repoDao.reposByName(query)
//    }

}
