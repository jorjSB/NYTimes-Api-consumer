package com.george.balasca.articleregistry.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.networkdatasource.ArticleDataSourceFactory;
import com.george.balasca.articleregistry.repository.networkdatasource.ItemPositionalDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class APIArticlesViewModel extends ViewModel {
    public LiveData<PagedList<Article>> articleList;
    public LiveData<NetworkState> networkState;
    Executor executor;
    LiveData<ItemPositionalDataSource> tDataSource;

    public APIArticlesViewModel(){
        executor = Executors.newFixedThreadPool(5);
        ArticleDataSourceFactory articleDataSourceFactory = new ArticleDataSourceFactory(executor);

        tDataSource = articleDataSourceFactory.getMutableLiveData();

        networkState = Transformations.switchMap(articleDataSourceFactory.getMutableLiveData(), dataSource -> {
            return dataSource.getNetworkState();
        });

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(30)
                        .setPrefetchDistance(10)
                        .setInitialLoadSizeHint(30)
                        .build();

        articleList = (new LivePagedListBuilder(articleDataSourceFactory, pagedListConfig))
                .build();
    }

}
