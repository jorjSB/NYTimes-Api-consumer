package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.ui.ArticleListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.george.balasca.articleregistry.ui.Helpers.getParsedDate;
import static com.george.balasca.articleregistry.ui.Helpers.setMainImage;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ArticleViewHolder.class.getSimpleName();
    @BindView(R.id.art_title)  TextView artTitle;
    @BindView(R.id.art_date)  TextView artDate;
    @BindView(R.id.art_main_image) ImageView artMainImage;


    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(DBCompleteArticle article, ArticleListActivity mParentActivity) {


        artTitle.setText(  article.headline.get(0).getMain());
        if(article.article != null && article.article.getPubDate() != null)
            artDate.setText( mParentActivity.getResources().getString(R.string.list_date_prefix) + getParsedDate(article.article.getPubDate()));

        setMainImage(article, artMainImage, true);

    }
}
