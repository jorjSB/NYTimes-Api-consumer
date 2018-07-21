package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Article... repos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticles(List<Article> articles);


    @Delete
    void delete(Article... articles);

    @Query("DELETE FROM article")
    void deleteAll();

    @Query("SELECT * FROM article")
    List<Article> getArticles();

    @Query("SELECT * FROM article")
    DataSource.Factory<Integer, Article> getAllArticles();

}
