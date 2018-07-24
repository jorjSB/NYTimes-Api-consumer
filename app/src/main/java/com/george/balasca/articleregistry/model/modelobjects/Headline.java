
package com.george.balasca.articleregistry.model.modelobjects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "headline",
        foreignKeys = @ForeignKey(entity = Article.class,
                parentColumns = "id",
                childColumns = "article_original_id",
                onDelete = CASCADE),
        indices = {@Index("article_original_id")})
public class Headline {

    @Expose(deserialize = false)
    @NonNull
    @ColumnInfo(name = "article_original_id")
    private String article_original_id;

    @Expose(deserialize = false)
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("kicker")
    @Expose
    private String kicker;


    // ************************************************************************************************************************


    @NonNull
    public String getArticle_original_id() {
        return article_original_id;
    }

    public void setArticle_original_id(@NonNull String article_original_id) {
        this.article_original_id = article_original_id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getKicker() {
        return kicker;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

}
