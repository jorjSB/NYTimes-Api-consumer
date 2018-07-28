package com.george.balasca.articleregistry.ui;

import android.util.Log;
import android.widget.ImageView;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.model.modelobjects.Multimedium;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static com.george.balasca.articleregistry.api.Api.IMAGES_BASE_URL;

public class Helpers {
    private static final String TAG = Helpers.class.getSimpleName();

    public static void setMainImage(DBCompleteArticle article, ImageView imageView, Boolean replaceHolderIfNoImageFound) {

        // sort images based on width (ASC)
        Collections.sort(article.multimediaList);

        // new MMD object
        Multimedium myImage = new Multimedium();
        myImage.setWidth(1);

        // get the an image between 300<>550 width, if any
        for (Multimedium multimedium : article.multimediaList) {
            if(multimedium.getWidth() > 300 && multimedium.getWidth() <= 550 ){
                myImage = multimedium;
                break;
            }
        }

        // if no "medium" image found, get the largest
        if(myImage.getWidth() == 1 && article.multimediaList.size() > 0)
            myImage = article.multimediaList.get(article.multimediaList.size() - 1);

        // set image
        if(myImage.getWidth() != 1){
            Log.d(TAG, "image url: " + IMAGES_BASE_URL + myImage.getUrl());
            Picasso.get()
                    .load( IMAGES_BASE_URL + myImage.getUrl() )
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.no_image_available)
                    .into(imageView);
        }else if(replaceHolderIfNoImageFound)
            imageView.setImageResource(R.drawable.no_image_available);
    }


    public static String getParsedDate(Date pubDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String parsedDate = pubDate == null ? "n/a" : formatter.format(pubDate);
        return parsedDate;
    }

}
