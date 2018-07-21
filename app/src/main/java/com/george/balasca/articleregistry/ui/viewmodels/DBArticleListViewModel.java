package com.george.balasca.articleregistry.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.george.balasca.articleregistry.model.ApiSearchResultObject;
import com.george.balasca.articleregistry.model.apiresponse.Article;
import com.george.balasca.articleregistry.repository.AppRepository;

public class DBArticleListViewModel extends ViewModel {

    private AppRepository repository;

    // init a mutable live data to listen for queries
    private MutableLiveData<String> queryLiveData = new MutableLiveData();

    // make the search after each new search item is posted with (searchRepo) using "map"
    private LiveData<ApiSearchResultObject> repositoryResult =  Transformations.map(queryLiveData, queryString -> {
        return repository.search(queryString);
    });

    // constructor, init repo
    public DBArticleListViewModel(@NonNull AppRepository repository) {
        this.repository = repository;
    }

    // get my Articles!!
    public LiveData<PagedList<Article>> articlesLiveData = Transformations.switchMap(repositoryResult, object ->
            object.getArticles());

    // get teh Network errors!
    public LiveData<String> errorsLiveData = Transformations.switchMap(repositoryResult, object ->
            object.getNetworkErrors());


    // Search REPO
    public final void searchRepo(@NonNull String queryString) {
        this.queryLiveData.postValue(queryString);
    }

    // LAST Query string used
    public final String lastQueryValue() {
        return (String)this.queryLiveData.getValue();
    }


//    @NonNull
//    public final LiveData getRepos() {
//        return this.repos;
//    }
//    @NonNull
//    public final LiveData getNetworkErrors() {
//        return this.networkErrors;
//    }

//    private AppRepository appRepository;
//
//    public LiveData<PagedList<Article>> pagedListLiveData;
//    private MutableLiveData<List<String>> queryLiveData = new MutableLiveData<>();
//
//    LiveData<Article> searchResult = Transformations.map(pagedListLiveData, articles -> {
//        return appRepository.search(??); // Returns String
//    });
//
//
//
//
//
//    public DBArticleListViewModel(AppRepository appRepository) {
//        this.appRepository = appRepository;
//
//        pagedListLiveData = Transformations.switchMap(searchResult) { abc -> abc.data }
//
//
////        PagedList.Config pagedListConfig =
////            (new PagedList.Config.Builder())
////                    .setEnablePlaceholders(false)
////                    .setPageSize(20)
////                    .build();
////
////        pagedListLiveData = new LivePagedListBuilder(
////                appRepository.getAllArticles(), pagedListConfig
////        ).build();
//
//        ? pagedListLiveData = appRepository.search("trump");
//    }
//
//    /**
//     * Search a repository based on a query string.
//     */
//    public void searchRepo(String queryString) {
//        queryLiveData.postValue(queryString);
//    }
//
//    /**
//     * Get the last query value.
//     */
//    public String lastQueryValue(){
//        return  queryLiveData.value != null ? queryLiveData.value  : "";
//    }
//
//
//
//    //***************    Obsolete: Used for testing/first steps  //***************
//
//    public void insertDummyArticlesIntoDB(){
//        appRepository.insertAllArticles(getInitialDummyArticles());
//    }
//
//    public static ArrayList<Article> getInitialDummyArticles() {
//        ArrayList<Article> articleArrayList = new ArrayList<>();
//        int time = (int) (System.currentTimeMillis());
//
//        for (int i=1; i < 75; i++){
//            Article article = new Article();
//            article.setId(String.valueOf(time + i));
//            article.setWebUrl("ceva.com " + String.valueOf(time + i));
//            articleArrayList.add(article);
//        }
//
//        return articleArrayList;
//    }
}
