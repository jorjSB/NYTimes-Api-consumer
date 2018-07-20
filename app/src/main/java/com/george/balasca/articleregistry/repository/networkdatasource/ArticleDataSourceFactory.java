package com.george.balasca.articleregistry.repository.networkdatasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import java.util.concurrent.Executor;

public class ArticleDataSourceFactory extends DataSource.Factory  {

    MutableLiveData<ItemPositionalDataSource> mutableLiveData;
    ItemPositionalDataSource itemPositionalDataSource;

    Executor executor;

    public ArticleDataSourceFactory(Executor executor) {
        this.mutableLiveData = new MutableLiveData<ItemPositionalDataSource>();
        this.executor = executor;
    }

    @Override
    public DataSource create() {
        itemPositionalDataSource = new ItemPositionalDataSource(executor);
        mutableLiveData.postValue(itemPositionalDataSource);
        return itemPositionalDataSource;
    }

    public MutableLiveData<ItemPositionalDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
