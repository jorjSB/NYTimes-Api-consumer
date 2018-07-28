package com.george.balasca.articleregistry.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.george.balasca.articleregistry.model.modelobjects.Multimedium;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MultimediaDao {

    @Insert(onConflict = REPLACE)
    void insert(Multimedium multimedia);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Multimedium> multimediumList);

    @Insert(onConflict = REPLACE)
    void update(Multimedium... multimedia);

    @Delete
    void delete(Multimedium... multimedia);

    @Query("SELECT * FROM multimedia ORDER BY width DESC")
    List<Multimedium> getAllMultimedia();

    @Query("SELECT * FROM multimedia WHERE article_original_id=:article_original_id ORDER BY width DESC")
    List<Multimedium> findMultimediaByArticleUid(final String article_original_id);

}
