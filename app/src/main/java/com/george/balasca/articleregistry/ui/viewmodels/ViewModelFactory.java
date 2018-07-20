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
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}






/**
 * Factory for ViewModels

class ViewModelFactory(private val repository: GithubRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
@Suppress("UNCHECKED_CAST")
            return SearchRepositoriesViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                    }
                    }
 */

