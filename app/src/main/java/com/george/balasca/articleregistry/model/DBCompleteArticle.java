package com.george.balasca.articleregistry.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.model.modelobjects.Headline;
import com.george.balasca.articleregistry.model.modelobjects.Multimedium;

import java.util.List;

public class DBCompleteArticle {
    @Embedded
    public Article article;

    @Relation(parentColumn = "id",
            entityColumn = "article_original_id") public List<Headline> headline;

    @Relation(parentColumn = "id",
            entityColumn = "article_original_id", entity = Multimedium.class) public List<Multimedium> multimediaList;


    public static final DiffUtil.ItemCallback<DBCompleteArticle> DIFF_CALLBACK = new DiffUtil.ItemCallback<DBCompleteArticle>() {
        @Override
        public boolean areItemsTheSame(@NonNull DBCompleteArticle oldItem, @NonNull DBCompleteArticle newItem) {
            return oldItem.article.getId().equals(newItem.article.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DBCompleteArticle oldItem, @NonNull DBCompleteArticle newItem) {
            return oldItem.article.getId().equals(newItem.article.getId()) && oldItem.article.getWebUrl().equals(newItem.article.getWebUrl());
        }
    };
}
