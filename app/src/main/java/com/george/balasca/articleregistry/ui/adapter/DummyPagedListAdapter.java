package com.george.balasca.articleregistry.ui.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.ui.ArticleListActivity;

public class DummyPagedListAdapter extends PagedListAdapter<Article, ArticleViewHolder> {

    private final ArticleListActivity mParentActivity;

    public DummyPagedListAdapter(ArticleListActivity parentActivity) {
        super(Article.DIFF_CALLBACK);
        mParentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mParentActivity).inflate(R.layout.article_list_content, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = getItem(position);
//
        if (article != null) {
            holder.bindTo(article);
        } else {
            holder.clear();
        }
    }
}
