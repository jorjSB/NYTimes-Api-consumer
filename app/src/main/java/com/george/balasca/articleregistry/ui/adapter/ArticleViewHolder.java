package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.modelobjects.Article;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.content)  TextView articleItemView;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(DBCompleteArticle dbCompleteArticle) {
        if(dbCompleteArticle.article.getPubDate()!= null && dbCompleteArticle.headline.get(0).getMain() != null) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String text = formatter.format(dbCompleteArticle.article.getPubDate());
            String parsedDate = text;

            articleItemView.setText(dbCompleteArticle.headline.get(0).getMain() + " /n Published at:  " + parsedDate + "  " + dbCompleteArticle.article.getPubDate());
        }else
            articleItemView.setText( dbCompleteArticle.headline.get(0).getMain() );

    }
}
