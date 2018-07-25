package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.repository.AppRepository;


// so we can pass the AppRepository in the constructor
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AppRepository repository;

    public ViewModelFactory(@NonNull AppRepository repository) {
        super();
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DBArticleListViewModel.class)) {
            return (T) new DBArticleListViewModel(this.repository);
        } else if(modelClass.isAssignableFrom(DBArticleDetailsViewModel.class)){
            return (T) new DBArticleDetailsViewModel(this.repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}