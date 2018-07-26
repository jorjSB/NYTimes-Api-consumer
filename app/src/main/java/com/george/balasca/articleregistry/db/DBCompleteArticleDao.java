package com.george.balasca.articleregistry.db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.george.balasca.articleregistry.model.DBCompleteArticle;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DBCompleteArticleDao {

    @Transaction
    @Query("SELECT * FROM article WHERE isFavourite != 1")
    DataSource.Factory<Integer, DBCompleteArticle> getDBCompleteArticlesWithoutFavourites();

    @Transaction
    @Query("SELECT * FROM article WHERE isFavourite == 1")
    DataSource.Factory<Integer, DBCompleteArticle> getFavouritesDBCompleteArticles();

    // NOT PAGED -> WiDGET
    @Query("SELECT * FROM article WHERE isFavourite == 1")
    List<DBCompleteArticle> getFavouritesDBCompleteArticlesList();

    @Transaction
    @Query("SELECT * FROM article WHERE id=:article_id LIMIT 1")
    LiveData<DBCompleteArticle> findDBCompleteArticleById(String article_id);

}
