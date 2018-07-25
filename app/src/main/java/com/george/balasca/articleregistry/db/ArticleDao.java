package com.george.balasca.articleregistry.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.george.balasca.articleregistry.model.modelobjects.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Article article);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Article> articles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Article... repos);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticles(List<Article> articles);

    @Delete
    void delete(Article... articles);

    @Query("DELETE FROM article")
    void deleteAll();

    @Query("DELETE FROM article WHERE isFavourite == 0")
    int deleteAllExceptFavourites();

    @Query("SELECT * FROM article")
    List<Article> getArticles();

    @Query("SELECT * FROM article")
    DataSource.Factory<Integer, Article> getAllArticles();

    // update individual flag for an article(is/is not Favourite)
    @Query("UPDATE article SET isFavourite = :favourite WHERE id = :articleId")
    int setArticleFavouriteState(Boolean favourite, String articleId);


//    @RawQuery(observedEntities = Article.class)
//    DataSource.Factory<Integer, Article> getAllArticlesOrdered(SupportSQLiteQuery query);

}
