package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.modelobjects.Article;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView articleItemView;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        articleItemView = itemView.findViewById(R.id.content);
    }

    public void bindTo(Article article) {
        if(article.getPubDate()!= null) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String text = formatter.format(article.getPubDate());
            String parsedDate = text;

            articleItemView.setText(article.getWebUrl() + " /n Published at:  " + parsedDate);
        }else
            articleItemView.setText(article.getWebUrl() );

    }
}
