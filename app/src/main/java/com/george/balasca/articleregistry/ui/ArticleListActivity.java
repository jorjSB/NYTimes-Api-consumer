package com.george.balasca.articleregistry.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.george.balasca.articleregistry.Injection;
import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.ui.adapter.ArticleListAdapter;
import com.george.balasca.articleregistry.ui.viewmodels.DBArticleListViewModel;

/**
 * An activity representing a list of Article. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ArticleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ArticleListActivity extends AppCompatActivity {

    private static final String TAG = ArticleListActivity.class.getSimpleName();
    private final String LAST_SEARCH_QUERY = "last_search_query";
    private final String DEFAULT_QUERY = "Android";
    private String query;
    private boolean mTwoPane;
//    private _APIArticlesViewModel viewModel;
    private DBArticleListViewModel localDBViewModel;
    private View recyclerView;
    private TextView noResultsPlaceholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Inserting mock data into the DB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // localDBViewModel.insertDummyArticlesIntoDB();
            }
        });

        if (findViewById(R.id.article_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.article_list);
        noResultsPlaceholder = findViewById(R.id.empty_list);
        localDBViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(DBArticleListViewModel.class);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        query = savedInstanceState != null && savedInstanceState.getString(LAST_SEARCH_QUERY) != null ? savedInstanceState.getString(LAST_SEARCH_QUERY) : DEFAULT_QUERY;
        localDBViewModel.searchRepo(query);
        initSearch(query);
    }

    private void initSearch(String query) {
        // TODO
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, localDBViewModel.lastQueryValue());
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ArticleListAdapter articleListAdapter = new ArticleListAdapter(this, mTwoPane);

        // observe the List of articles
        localDBViewModel.articlesLiveData.observe(this, pagedListLiveData ->{

            if(pagedListLiveData != null) {
                showListPlaceholder( pagedListLiveData.size() == 0 ? true : false );
                articleListAdapter.submitList(pagedListLiveData);
            }
        });

        // observe the networkState (loading/loaded/error)
        localDBViewModel.networkLoadingStateLiveData.observe(this, networkStateLiveData ->{
            articleListAdapter.setNetworkState(networkStateLiveData);
        });

        // observe the network errors: no internet/error messages from server
        localDBViewModel.networkErrorsLiveData.observe(this, networkErrorsLiveData ->{
            showSnack(networkErrorsLiveData);
        });

        recyclerView.setAdapter(articleListAdapter);
    }

    private void showListPlaceholder(boolean showListPlaceholder) {
        if(showListPlaceholder){
            recyclerView.setVisibility(View.GONE);
            noResultsPlaceholder.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            noResultsPlaceholder.setVisibility(View.GONE);
        }
    }

    private void showSnack(String networkErrorsLiveData) {
        Snackbar.make(
                findViewById(R.id.article_list)
                , networkErrorsLiveData, Snackbar.LENGTH_LONG).show();
    }


}
