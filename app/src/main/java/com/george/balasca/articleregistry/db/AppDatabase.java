package com.george.balasca.articleregistry.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.db.helpers.Converters;
import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private final static String TAG = AppDatabase.class.getSimpleName();
    private static AppDatabase dbInstance;

    public abstract ArticleDao getArticleDao();


    public static AppDatabase getDatabase(final Context context) {
        if (dbInstance == null) {
            synchronized (AppDatabase.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);

                    new PopulateDbAsync(dbInstance).execute();
                }
            };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ArticleDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.getArticleDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            // mDao.insertArticles(getInitialDummyArticles());

            return null;
        }
    }

    public static List<Article> getInitialDummyArticles() {
        ArrayList<Article> articleArrayList = new ArrayList<>();

        for (int i=0; i < 75; i++){
            Article article = new Article();
            article.setId(String.valueOf(i));
            article.setWebUrl("google.com " + i);
            articleArrayList.add(article);
        }

        return articleArrayList;
    }
}
