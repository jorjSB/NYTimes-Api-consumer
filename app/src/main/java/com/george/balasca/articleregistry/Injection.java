package com.george.balasca.articleregistry;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.george.balasca.articleregistry.api.Api;
import com.george.balasca.articleregistry.db.AppDatabase;
import com.george.balasca.articleregistry.db.LocalCache;
import com.george.balasca.articleregistry.repository.AppRepository;
import com.george.balasca.articleregistry.ui.viewmodels.ViewModelFactory;

import java.util.concurrent.Executors;

// super object used as provider.. objects can be passed as parameters in the constructors
public class Injection {
    /**
     * Creates an instance of the LocalCache based on the DB
     */
    private static LocalCache provideCache(Context context){
        AppDatabase database = AppDatabase.getDatabase(context);
        return new LocalCache(database, Executors.newSingleThreadExecutor());
    }

    /**
     * Creates an instance of the AppRepository baseed on the API.createService and LocalCache
     */
    private static AppRepository provideAppRepository(Context context){
        return new AppRepository(Api.createService(), provideCache(context));
    }

    /**
     * Provides the ViewModelProvider.Factory that is then used to get a reference to
     * [ViewModel] objects.
     */

    public static ViewModelProvider.Factory provideViewModelFactory(Context context){
        return new ViewModelFactory(provideAppRepository(context));
    }
}