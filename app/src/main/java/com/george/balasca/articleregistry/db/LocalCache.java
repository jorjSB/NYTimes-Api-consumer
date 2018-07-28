package com.george.balasca.articleregistry.db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.os.AsyncTask;
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
    private final Executor ioExecutor;
    private final AppDatabase appDatabase;
    private final ArticleDao articleDao;
    private final HeadlineDao headlineDao;
    private final MultimediaDao multimediaDao;
    private final DBCompleteArticleDao dbCompleteArticleDao;

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
            // hack to set the isfavourite field to 0, else Room doesn't know how to delete it... wtf..
            article.setFavourite(false);
            // set Headline and add to list of headlines
            article.getHeadline().setArticle_original_id(article.getId());
            headlineArrayList.add(article.getHeadline());

            for (Multimedium multimedia : article.getMultimedia()) {
                multimedia.setArticle_original_id(article.getId());
                multimediaArrayList.add(multimedia);
            }
        }

        new PopulateDbAsync(articleArrayList, headlineArrayList, multimediaArrayList).execute();

//        ioExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase.runInTransaction(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Log.d(TAG, "Inserting " + articleArrayList.size() + " articles");
//                        articleDao.insert(articleArrayList);
//                        // Log.d(TAG, "Inserting " + multimediaArrayList.size() + " multimedia");
//                        multimediaDao.insert(multimediaArrayList);
//                        // Log.d(TAG, "Inserting " + headlineArrayList.size() + " headliness");
//                        headlineDao.insert(headlineArrayList);
//                    }
//                });
//            }
//        });
    }


    // not needed actually. I replaced a ioExecutor runnable with this AsyncTask to comply with the project requirements
    private class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        final List<Article> articleArrayList;
        final ArrayList<Headline> headlineArrayList;
        final ArrayList<Multimedium> multimediaArrayList;

        PopulateDbAsync(List<Article> _articleArrayList,  ArrayList<Headline> _headlineArrayList,  ArrayList<Multimedium> _multimediaArrayList) {
            articleArrayList = _articleArrayList;
            headlineArrayList = _headlineArrayList;
            multimediaArrayList = _multimediaArrayList;
        }

        @Override
        protected Void doInBackground(final Void... params) {

            appDatabase.runInTransaction(() -> {
                // Log.d(TAG, "Inserting " + articleArrayList.size() + " articles");
                articleDao.insert(articleArrayList);
                // Log.d(TAG, "Inserting " + multimediaArrayList.size() + " multimedia");
                multimediaDao.insert(multimediaArrayList);
                // Log.d(TAG, "Inserting " + headlineArrayList.size() + " headliness");
                headlineDao.insert(headlineArrayList);
            });

            return null;
        }
    }



    public DataSource.Factory<Integer, Article> getAllArticles() {
        return articleDao.getAllArticles();
    }

    public DataSource.Factory<Integer, DBCompleteArticle> getDBCompleteArticles() {
        return dbCompleteArticleDao.getDBCompleteArticlesWithoutFavourites();
    }

    public DataSource.Factory<Integer, DBCompleteArticle> getfavouritesDBCompleteArticle() {
        return dbCompleteArticleDao.getFavouritesDBCompleteArticles();
    }

    public List<DBCompleteArticle> getfavouritesDBCompleteArticleList() {
        return dbCompleteArticleDao.getFavouritesDBCompleteArticlesList();
    }

    public LiveData<DBCompleteArticle> findDBCompleteArticleById(String id) {
        return dbCompleteArticleDao.findDBCompleteArticleById(id);
    }

    public void deleteObsoleteArticles(){
        ioExecutor.execute(() -> {
            int noOfDeletedArticles =  articleDao.deleteAllExceptFavourites();
            Log.d(TAG, "Deleted: " + noOfDeletedArticles + " articles");
        });
    }

    public void setArticleFavouriteState(Boolean isFavourite, String articleId) {
        ioExecutor.execute(() -> {
            int setFavourite = articleDao.setArticleFavouriteState(isFavourite, articleId);
            Log.d(TAG, "Switched fav. state to " + isFavourite + ": " + setFavourite);
        });
    }
}
