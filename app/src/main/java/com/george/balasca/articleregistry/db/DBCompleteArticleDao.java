package com.george.balasca.articleregistry.db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.model.modelobjects.Headline;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DBCompleteArticleDao {

    @Transaction
    @Query("SELECT * FROM article WHERE isFavourite != 1")
    DataSource.Factory<Integer, DBCompleteArticle> getDBCompleteArticlesWithoutFavourites();

    @Transaction
    @Query("SELECT * FROM article WHERE isFavourite == 1")
    DataSource.Factory<Integer, DBCompleteArticle> getFavouritesDBCompleteArticles();

    @Transaction
    @Query("SELECT * FROM article WHERE id=:article_id LIMIT 1")
    LiveData<DBCompleteArticle> findDBCompleteArticleById(String article_id);

}
