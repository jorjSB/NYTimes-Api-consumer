package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.apiresponse.Article;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameTV;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        nameTV = (TextView) itemView.findViewById(R.id.content);
    }

    public void bindTo(Article article) {
        nameTV.setText(article.getWebUrl());
    }

    void clear() {
        itemView.invalidate();
        nameTV.invalidate();
    }
}