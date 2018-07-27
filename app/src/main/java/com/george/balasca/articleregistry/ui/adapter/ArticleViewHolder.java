package com.george.balasca.articleregistry.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.modelobjects.Multimedium;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.george.balasca.articleregistry.api.Api.IMAGES_BASE_URL;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ArticleViewHolder.class.getSimpleName();
    @BindView(R.id.art_title)  TextView artTitle;
    @BindView(R.id.art_date)  TextView artDate;
    @BindView(R.id.art_main_image) ImageView artMainImage;


    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTo(DBCompleteArticle article) {


        artTitle.setText(  article.headline.get(0).getMain());
        if(article.article != null && article.article.getPubDate() != null)
            artDate.setText( "Published at:  " + getParsedDate(article.article.getPubDate()));

        setMainImage(article);

    }

    private String getParsedDate(Date pubDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String parsedDate = pubDate == null ? "n/a" : formatter.format(pubDate);
        return parsedDate;
    }


    private void setMainImage(DBCompleteArticle article) {
        Multimedium myImage = new Multimedium();
        myImage.setWidth(1);

        // get the largest image(no time to TODO: find a way to get them already sorted from ROOM)
        for (Multimedium multimedium : article.multimediaList) {
            if(myImage.getWidth() < multimedium.getWidth())
                myImage = multimedium;
        }

        // set image
        if(myImage.getWidth() != 1)
            Picasso.get()
                    .load( IMAGES_BASE_URL + myImage.getUrl() )
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.no_image_available)
                    .into(artMainImage);
        // avoid making useless calls
        artMainImage.setImageResource(R.drawable.no_image_available);

    }
}
