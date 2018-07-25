package com.george.balasca.articleregistry.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.george.balasca.articleregistry.Injection;
import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.DBCompleteArticle;
import com.george.balasca.articleregistry.ui.viewmodels.DBArticleDetailsViewModel;

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
    public static final String ARG_ITEM_ID = "item_id";

    private DBArticleDetailsViewModel dbArticleDetailsViewModel;
    private static String articleId;
    private DBCompleteArticle article;

    private Activity activity;
    private CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.article_detail) TextView article_detail;
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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
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

        // observe the networkState (loading/loaded/error)
        dbArticleDetailsViewModel.dbCompleteArticleLiveData.observe(this, article ->{
            if(article != null) {
                inflateArticleDetails(article);
            }
        });

        return rootView;
    }


    private void inflateArticleDetails(DBCompleteArticle dbCompleteArticle) {

        if (appBarLayout != null) {
            appBarLayout.setTitle(dbCompleteArticle.headline.get(0).getMain());
        }

        article_detail.setText(dbCompleteArticle.article.getSnippet());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
