package com.george.balasca.articleregistry.ui;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.george.balasca.articleregistry.Injection;
import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.model.SearchQueryPOJO;
import com.george.balasca.articleregistry.ui.adapter.ArticleListAdapter;
import com.george.balasca.articleregistry.ui.viewmodels.DBArticleListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListActivity extends AppCompatActivity implements FilterDialogFragment.OnFiltersSetListener {

    private static final String TAG = ArticleListActivity.class.getSimpleName();
    private final String LAST_SEARCH_QUERY = "last_search_query";
    private final SearchQueryPOJO DEFAULT_QUERY = null;
    private SearchQueryPOJO query;
    private boolean mTwoPane;
    private DBArticleListViewModel localDBViewModel;
    private ArticleListAdapter articleListAdapter;
    @BindView(R.id.article_list)  RecyclerView recyclerView;
    @BindView(R.id.empty_list) LinearLayout noResultsPlaceholder;
    @BindView(R.id.fab_article_list)  FloatingActionButton fab;
    @BindView(R.id.toolbar)  Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.article_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            // using it from this activity only in 2-pane mode

        }

        // fab click opens filters
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialogFragment();
            }
        });

        // get the VM for this activity
        localDBViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this)).get(DBArticleListViewModel.class);
        assert recyclerView != null;


        /** INIT MY OBJECTS: adapter, observables etc.. */

        // setup the List, add observables etc..
        setupRecyclerView((RecyclerView) recyclerView);

        // init search observables
        initSearchObservables();

        // init DB observable
        // TODO: init an observable that doesn't need changes to observe the DB??


        // handle searches/initial data
        query = savedInstanceState != null && savedInstanceState.getSerializable(LAST_SEARCH_QUERY) != null ?
                (SearchQueryPOJO) savedInstanceState.getSerializable(LAST_SEARCH_QUERY)
                : DEFAULT_QUERY;


        // get the NEW search query, INIT SEARCH (delete old data)
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String searchString = getIntent().getStringExtra(SearchManager.QUERY);
            // reset the intent, so it won't be triggered on every orientation change etc.. we need it only once
            getIntent().removeExtra(SearchManager.QUERY);
            getIntent().removeExtra(SearchManager.APP_DATA);

            if(searchString != null) {
                // starting a new activity after a search -> query obj is null
                if(query == null)
                    query = new SearchQueryPOJO();
                query.setQuery(searchString);
                initNewAPISearch(searchString);
            }
        }

        // if no query was made(1'st time entering the app for example, or returning after a while)
        if(query == null){
            initNewAPILatestArticleSearch();
            query = new SearchQueryPOJO();
        }
    }

    // make an api call with a new query term, no filters
    private void initNewAPISearch(String searchString) {
        SearchQueryPOJO newSearchQueryPOJO = new SearchQueryPOJO();
        newSearchQueryPOJO.setQuery(searchString);
        showSnack(getResources().getString(R.string.searching_for_hint) + " " + searchString);
        searchArticles(newSearchQueryPOJO);
    }

    // make an api call that returns the latest articles(no query, no filters)
    private void initNewAPILatestArticleSearch() {
        SearchQueryPOJO newSearchQueryPOJO = new SearchQueryPOJO();
        newSearchQueryPOJO.setSort("newest");
        showSnack(getResources().getString(R.string.latest_articles_hint));
        searchArticles(newSearchQueryPOJO);
    }

    private void showSearchDialogFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        FilterDialogFragment dialog = FilterDialogFragment.newInstance();
        dialog.show(fm, "search_dialog_fragment");
    }

    private void searchArticles(SearchQueryPOJO searchQueryPOJO) {
        // Log.d(TAG, "searchArticles(String query) " + query.getQuery());
        localDBViewModel.searchArticle(searchQueryPOJO);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        articleListAdapter = new ArticleListAdapter(this, mTwoPane);
        recyclerView.setAdapter(articleListAdapter);
    }

    private void initSearchObservables(){
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
            for (String error: networkErrorsLiveData) {
                showSnackWithConfirmation(error);
            }

            // remove the data so it won't show on config changes
            localDBViewModel.networkErrorsLiveData.getValue().clear();
        });

//        if(localDBViewModel.articlesLiveData.getValue() != null)
//            Toast.makeText(this, query + " adapter items: " + localDBViewModel.articlesLiveData.getValue().size() , Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, "localDBViewModel.articlesLiveData EMPTY" , Toast.LENGTH_SHORT).show();
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
        Snackbar.make( findViewById(R.id.article_list), networkErrorsLiveData, Snackbar.LENGTH_LONG).show();
    }

    private void showSnackWithConfirmation(String networkErrorsLiveData) {
        Snackbar snackBar = Snackbar.make(findViewById(R.id.article_list), networkErrorsLiveData, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(R.string.dismiss_snackbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LAST_SEARCH_QUERY, localDBViewModel.lastQueryValue());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_list_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_search:  //handled by the searchView
                return true;
            case R.id.app_bar_newest:
                initNewAPILatestArticleSearch();
                return true;
            case R.id.app_bar_favourites:
                // TODO: favourite list!
                showSnack(getResources().getString(R.string.showing_favourite_articles_hint));
                return true;
            case R.id.app_bar_filter:
                showSearchDialogFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onFiltersSet(String beginDate, String endDate, String sort, String category) {
        SearchQueryPOJO newFilteredSearch = new SearchQueryPOJO();
        newFilteredSearch.setQuery(query.getQuery());
        if(!beginDate.isEmpty()) newFilteredSearch.setBegin_date(beginDate);
        if(!endDate.isEmpty()) newFilteredSearch.setEnd_date(endDate);
        newFilteredSearch.setSort(sort.toLowerCase());
        if(category.compareTo(getResources().getString(R.string.news_desk_0)) != 0) newFilteredSearch.setCategory(category);

        searchArticles(newFilteredSearch);
    }
}
