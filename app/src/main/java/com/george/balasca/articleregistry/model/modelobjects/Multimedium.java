
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

@Entity(tableName = "multimedia",
        foreignKeys = @ForeignKey(entity = Article.class,
                parentColumns = "id",
                childColumns = "article_original_id",
                onDelete = CASCADE),
        indices = {@Index("article_original_id")})
public class Multimedium implements Comparable<Multimedium>{

    @Expose(deserialize = false)
    @NonNull
    @ColumnInfo(name = "article_original_id")
    private String article_original_id;

    @Expose(deserialize = false)
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;

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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Method used to sort the multimedia images based on their width
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NonNull Multimedium o) {
        if (this.getWidth() < o.getWidth() ){
            return -1;
        }else{
            return 1;
        }
    }
}
