/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.jsonresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author anthony
 */
public class SuccessGetFeedsResponse {

    private final List<SuccessGetFeedWithIdResponse> feeds;
    private final int newArticles;

    public SuccessGetFeedsResponse(List<SuccessGetFeedWithIdResponse> feeds, int newArticles) {
        this.feeds = feeds;
        this.newArticles = newArticles;
    }

    public List<SuccessGetFeedWithIdResponse> getFeeds() {
        return feeds;
    }

    @JsonProperty("new_articles")
    public int getNewArticles() {
        return newArticles;
    }
}
