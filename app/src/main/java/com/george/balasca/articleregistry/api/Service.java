package com.george.balasca.articleregistry.api;

import com.george.balasca.articleregistry.model.apiresponse.ApiResponse;

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
    Call<ApiResponse> getQueriedStartEndArticles(
            @Query("page") int page,
            @Query("q") String q,
            @Query("end_date") int end_date,
            @Query("begin_date") int begin_date);
}
