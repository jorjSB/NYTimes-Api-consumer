package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.george.balasca.articleregistry.db.ArticleDao;
import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class DBArticleListViewModel extends ViewModel {

    private AppRepository appRepository;
    public LiveData<PagedList> pagedListLiveData;


    public DBArticleListViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;

        PagedList.Config pagedListConfig =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(20)
                    .build();

        pagedListLiveData = new LivePagedListBuilder(
                appRepository.getAllArticles(), pagedListConfig
        ).build();
    }

    public void insertDummyArticlesIntoDB(){
        appRepository.insertAllArticles(getInitialDummyArticles());
    }

    public static ArrayList<Article> getInitialDummyArticles() {
        ArrayList<Article> articleArrayList = new ArrayList<>();
        int time = (int) (System.currentTimeMillis());

        for (int i=1; i < 75; i++){
            Article article = new Article();
            article.setId(String.valueOf(time + i));
            article.setWebUrl("ceva.com " + String.valueOf(time + i));
            articleArrayList.add(article);
        }

        return articleArrayList;
    }
}
