package com.george.balasca.articleregistry.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.balasca.articleregistry.Injection;
import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.ui.viewmodels.DBArticleDetailsViewModel;
import com.george.balasca.articleregistry.ui.widget.AppWidgetProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a {@link ArticleListActivity}
 * in two-pane mode (on tablets) or a {@link ArticleDetailActivity}
 * on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String TAG = ArticleDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";

    private DBArticleDetailsViewModel dbArticleDetailsViewModel;
    private static String articleId;

    private Activity activity;
    private CollapsingToolbarLayout appBarLayout;
    private boolean currentFavouriteState;
    private boolean favouriteStateChanged = false;

    @BindView(R.id.article_detail) TextView article_detail;
    @BindView(R.id.adView) AdView mAdView;
    FloatingActionButton fab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // init variables
            activity = this.getActivity();
            appBarLayout = activity.findViewById(R.id.toolbar_layout);

            // Load the content specified by the fragment arguments.
            articleId = getArguments().getString(ARG_ITEM_ID);
            dbArticleDetailsViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(getActivity())).get(DBArticleDetailsViewModel.class);

            dbArticleDetailsViewModel.searchArticle(articleId);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail, container, false);
        ButterKnife.bind(this, rootView);

        // need to set it here (no good Butterknife) fab instance from main activity
        fab = getActivity().findViewById(R.id.fab);

        // init Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // observe the networkState (loading/loaded/error)
        dbArticleDetailsViewModel.dbCompleteArticleLiveData.observe(this, article ->{
            if(article != null) {
                inflateArticleDetails(article);
            }
        });

        return rootView;
    }


    private void handleSnackIfFavouriteStatusChanged(View view, DBCompleteArticle dbCompleteArticle) {

        if(currentFavouriteState != dbCompleteArticle.article.getFavourite())
            if(dbCompleteArticle.article.getFavourite())
                Snackbar.make(view, activity.getResources().getString(R.string.added_to_fav_prefix) +
                        dbCompleteArticle.headline.get(0).getMain() +
                        activity.getResources().getString(R.string.added_to_fav_suffix) , Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(view, activity.getResources().getString(R.string.removed_from_fav_prefix) +
                        dbCompleteArticle.headline.get(0).getMain() +
                        activity.getResources().getString(R.string.removed_from_fav_suffix) , Snackbar.LENGTH_LONG).show();
        favouriteStateChanged = false;
    }


    private void inflateArticleDetails(DBCompleteArticle dbCompleteArticle) {

        if (appBarLayout != null) {
            appBarLayout.setTitle(dbCompleteArticle.headline.get(0).getMain());
        }

        article_detail.setText(dbCompleteArticle.article.getSnippet());

        // handle viusl indicator - FAB
        handleFabVisualIndicator(dbCompleteArticle);

        // show snack only if we removed/added to favourites
        if(favouriteStateChanged) {
            handleSnackIfFavouriteStatusChanged(getView(), dbCompleteArticle);

            // this will send the broadcast to update the appwidget
            AppWidgetProvider.sendRefreshBroadcast(getActivity().getApplicationContext());
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteStateChanged = true;
                currentFavouriteState = dbCompleteArticle.article.getFavourite();
                boolean newState = currentFavouriteState == true ? false : true;
                dbArticleDetailsViewModel.setArticleFavouriteState(newState, articleId);
            }
        });
    }

    private void handleFabVisualIndicator(DBCompleteArticle dbCompleteArticle) {
        if(dbCompleteArticle.article.getFavourite()) {
            fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.theme_dark_gray)));
            fab.setImageResource(R.drawable.ic_bookmark_white_24dp);
        }else {
            fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.theme_accent)));
            fab.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
        }
    }
}
