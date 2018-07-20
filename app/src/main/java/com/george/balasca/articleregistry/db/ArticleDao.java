package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.george.balasca.articleregistry.model.apiresponse.Article;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ArticleDao {

    @Insert(onConflict = REPLACE)
    long insert(Article article);

    @Insert(onConflict = REPLACE)
    void update(Article... repos);

    @Insert(onConflict = IGNORE)
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
