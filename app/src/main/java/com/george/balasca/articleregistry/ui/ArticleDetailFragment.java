package com.george.balasca.articleregistry.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.george.balasca.articleregistry.Injection;
import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.ui.viewmodels.DBArticleDetailsViewModel;
import com.george.balasca.articleregistry.ui.widget.AppWidgetProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.george.balasca.articleregistry.ui.Helpers.getParsedDate;
import static com.george.balasca.articleregistry.ui.Helpers.setMainImage;

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

    @BindView(R.id.article_title) TextView article_title;
    @BindView(R.id.article_detail) TextView article_detail;
    @BindView(R.id.article_source) TextView article_source;
    @BindView(R.id.article_date) TextView article_date;
    @BindView(R.id.article_main_image_twopane) ImageView article_main_image_twopane;
    @BindView(R.id.adView) AdView mAdView;

    // from the Parent activity!
    FloatingActionButton fab;
    @Nullable
    ImageView article_main_image;
    private boolean mTwoPane = false;
    private DBCompleteArticle article;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getActivity() instanceof ArticleDetailActivity )
            mTwoPane = false;
        else
            mTwoPane = true;

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
        article_main_image = getActivity().findViewById(R.id.article_main_image);

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
        article = dbCompleteArticle;
        if (appBarLayout != null)
            appBarLayout.setTitle(article.headline.get(0).getMain());

        article_title.setText(article.headline.get(0).getMain());
        article_detail.setText(article.article.getSnippet());
        article_source.setText(article.article.getSource());
        article_date.setText(getParsedDate(article.article.getPubDate()));

        // handle viusl indicator - FAB
        handleFabVisualIndicator(article);

        // show snack only if we removed/added to favourites
        if(favouriteStateChanged) {
            handleSnackIfFavouriteStatusChanged(getView(), article);
            // this will send the broadcast to update the appwidget
            AppWidgetProvider.sendRefreshBroadcast(getActivity().getApplicationContext());
        }

        // set image for activity (single pane)
        if(mTwoPane) {
            article_main_image_twopane.setVisibility(View.VISIBLE);
            setMainImage(article, article_main_image_twopane, false);
        }else
            setMainImage(article, article_main_image, false);



        if (fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteStateChanged = true;
                currentFavouriteState = article.article.getFavourite();
                boolean newState = currentFavouriteState == true ? false : true;
                dbArticleDetailsViewModel.setArticleFavouriteState(newState, articleId);
            }
        });
    }

    private void handleFabVisualIndicator(DBCompleteArticle dbCompleteArticle) {
        if(fab != null)
            if(dbCompleteArticle.article.getFavourite()) {
                fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.theme_dark_gray)));
                fab.setImageResource(R.drawable.ic_bookmark_white_24dp);
            }else {
                fab.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.theme_accent)));
                fab.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
            }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "menuX fragment onCreateOptionsMenu,  mTwoPane: " + mTwoPane );
        if(!mTwoPane){
            menu.clear();
            inflater.inflate(R.menu.article_details_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "menuX fragment onPrepareOptionsMenu,  mTwoPane: " + mTwoPane );
        if(mTwoPane) {
            menu.findItem(R.id.app_bar_share).setVisible(true);
            menu.findItem(R.id.app_bar_filter).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_share:
                shareArticle();
                return true;
            default:
                break;
        }
        return false;
    }

    private void shareArticle() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.menu_title_share) + article.headline.get(0).getMain() );
        i.putExtra(Intent.EXTRA_TEXT, article.article.getWebUrl());
        startActivity(Intent.createChooser(i, getActivity().getResources().getString(R.string.share_url)));

        Snackbar.make(getView(), getActivity().getResources().getString(R.string.menu_title_share) + article.headline.get(0).getMain(), Snackbar.LENGTH_LONG).show();
    }

}
