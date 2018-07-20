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
import android.widget.Toast;

import com.george.balasca.articleregistry.R;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.Status;

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
    private boolean mTwoPane;
    private ArticlesViewModel viewModel;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.article_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.article_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        ArticleListAdapter articleListAdapter = new ArticleListAdapter(this, mTwoPane);

        viewModel.articleList.observe(this, pagedList -> {
            articleListAdapter.submitList(pagedList);
        });

        viewModel.networkState.observe(this, networkState -> {
             articleListAdapter.setNetworkState(networkState);
             if(networkState.getNetworkStatus() == Status.FAILED)
                showSnack(networkState);
        });

        recyclerView.setAdapter(articleListAdapter);
    }

    private void showSnack(NetworkState networkState) {
        Snackbar.make(
                findViewById(R.id.fab)
                , networkState.getMessage(), Snackbar.LENGTH_LONG).show();
    }


}
