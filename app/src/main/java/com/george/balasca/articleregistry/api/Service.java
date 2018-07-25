package com.george.balasca.articleregistry.api;

import com.george.balasca.articleregistry.model.modelobjects.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("articlesearch.json")
    Call<ApiResponse> getArticles(
            @Query("page") int page);


    @GET("articlesearch.json")
    Call<ApiResponse> getQueriedArticles(
            @Query("page") int page,
            @Query("q") String q);

    @GET("articlesearch.json")
    Call<ApiResponse> getQueriedFilteredArticles(
            @Query("page") int page,
            @Query("q") String query,
            @Query("fq") String category,
            @Query("sort") String sort,
            @Query("begin_date") String begin_date,
            @Query("end_date") String end_date);
}

