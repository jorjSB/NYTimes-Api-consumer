package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.model.modelobjects.Article;
import com.george.balasca.articleregistry.repository.NetworkState;
import com.george.balasca.articleregistry.repository.networkdatasource.ArticleDataSourceFactory;
import com.george.balasca.articleregistry.repository.networkdatasource.ItemPositionalDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class _APIArticlesViewModel extends ViewModel {

    public _APIArticlesViewModel(){
        Executor executor = Executors.newFixedThreadPool(5);
        ArticleDataSourceFactory articleDataSourceFactory = new ArticleDataSourceFactory(executor);

        LiveData<ItemPositionalDataSource> tDataSource = articleDataSourceFactory.getMutableLiveData();

        LiveData<NetworkState> networkState = Transformations.switchMap(articleDataSourceFactory.getMutableLiveData(), dataSource -> {
            return dataSource.getNetworkState();
        });

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(30)
                        .setPrefetchDistance(10)
                        .setInitialLoadSizeHint(30)
                        .build();

        LiveData<PagedList<Article>> articleList = (new LivePagedListBuilder(articleDataSourceFactory, pagedListConfig))
                .build();
    }

}
