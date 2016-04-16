/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.jsonresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author anthony
 */
public class SuccessFeedInfoResponse {

    private final int id;
    private final String name;
    private final String url;
    private final int newArticles;
    private final boolean refreshError;

    public SuccessFeedInfoResponse(int id, String name, String url, int newArticles, boolean error) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.newArticles = newArticles;
        this.refreshError = error;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("new_articles")
    public int getNewArticles() {
        return newArticles;
    }
    
    @JsonProperty("refresh_error")
    public boolean getRefreshError() {
        return refreshError;
    }
}
