package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.util.Log;

import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.model.modelobjects.Headline;
import com.george.balasca.articleregistry.model.modelobjects.Multimedium;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
public class LocalCache {
    private static final String TAG = LocalCache.class.getSimpleName();
    private Executor ioExecutor;
    private AppDatabase appDatabase;
    private ArticleDao articleDao;
    private HeadlineDao headlineDao;
    private MultimediaDao multimediaDao;
    private DBCompleteArticleDao dbCompleteArticleDao;

    public LocalCache(AppDatabase appDatabase, Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
        this.appDatabase = appDatabase;
        this.articleDao = appDatabase.getArticleDao();
        this.headlineDao = appDatabase.getHeadlineDao();
        this.multimediaDao = appDatabase.getMultimediaDao();
        this.dbCompleteArticleDao = appDatabase.getDBCompleteArticleDao();
    }

    public void insertAllArticles(List<Article> articleArrayList){
        ArrayList<Headline> headlineArrayList = new ArrayList<>();
        ArrayList<Multimedium> multimediaArrayList = new ArrayList<>();

        // parse here the lists from my complete list!!!
        for (Article article : articleArrayList){
            // set Headline and add to list of headlines
            article.getHeadline().setArticle_original_id(article.getId());
            headlineArrayList.add(article.getHeadline());

            for (Multimedium multimedia : article.getMultimedia()) {
                multimedia.setArticle_original_id(article.getId());
                multimediaArrayList.add(multimedia);
            }
        }

        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Inserting " + articleArrayList.size() + " articles");
                        articleDao.insert(articleArrayList);
                        Log.d(TAG, "Inserting " + multimediaArrayList.size() + " multimedia");
                        multimediaDao.insert(multimediaArrayList);
                        Log.d(TAG, "Inserting " + headlineArrayList.size() + " headliness");
                        headlineDao.insert(headlineArrayList);
                    }
                });
            }
        });

    }

    public DataSource.Factory<Integer, Article> getAllArticles() {
        DataSource.Factory<Integer, Article> articleFactory = articleDao.getAllArticles();
        return articleFactory;
    }

    public DataSource.Factory<Integer, DBCompleteArticle> getDBCompleteArticleDao() {
        DataSource.Factory<Integer, DBCompleteArticle> dbCompleteArticleFactory = dbCompleteArticleDao.getDBCompleteArticles();
        return dbCompleteArticleFactory;
    }
}
