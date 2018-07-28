package com.george.balasca.articleregistry.repository.networkdatasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import java.util.concurrent.Executor;

public class ArticleDataSourceFactory extends DataSource.Factory  {

    private final MutableLiveData<ItemPositionalDataSource> mutableLiveData;

    private final Executor executor;

    public ArticleDataSourceFactory(Executor executor) {
        this.mutableLiveData = new MutableLiveData<>();
        this.executor = executor;
    }

    @Override
    public DataSource create() {
        ItemPositionalDataSource itemPositionalDataSource = new ItemPositionalDataSource(executor);
        mutableLiveData.postValue(itemPositionalDataSource);
        return itemPositionalDataSource;
    }

    public MutableLiveData<ItemPositionalDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
