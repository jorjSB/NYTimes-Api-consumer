package com.george.balasca.articleregistry.model;

import java.io.Serializable;

public class SearchQueryPOJO implements Serializable{
    private String query;
    private String sort;
    private String category;
    private String begin_date;
    private String end_date;

    public SearchQueryPOJO() {
    }

    public SearchQueryPOJO(String query, String sort, String category, String begin_date, String end_date) {
        this.query = query;
        this.sort = sort;
        this.category = category;
        this.begin_date = begin_date;
        this.end_date = end_date;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
