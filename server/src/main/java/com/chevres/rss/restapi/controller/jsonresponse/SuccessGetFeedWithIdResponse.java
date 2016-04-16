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
public class SuccessGetFeedWithIdResponse {
    private final int id;
    private final String name;
    private final String url;
    private final boolean refreshError;

    public SuccessGetFeedWithIdResponse(int id, String name, String url, boolean error) {
        this.id = id;
        this.name = name;
        this.url = url;
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
    
    @JsonProperty("refresh_error")
    public boolean getRefreshError() {
        return refreshError;
    }
}
