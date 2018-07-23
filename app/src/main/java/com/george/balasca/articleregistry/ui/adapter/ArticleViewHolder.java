package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.apiresponse.Article;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView articleItemView;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        articleItemView = itemView.findViewById(R.id.content);
    }

    public void bindTo(Article article) {
        articleItemView.setText(article.getWebUrl());
    }
}
