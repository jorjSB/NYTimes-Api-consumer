
package com.george.balasca.articleregistry.model.modelobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBody {

    @SerializedName("docs")
    @Expose
    private List<Article> articleList = null;

    @SerializedName("meta")
    @Expose
    private Meta meta;


    // ************************************************************************************************************************

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
