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
public class SuccessGetArticleWithIdResponse {
    private final int id;
    private final int FeedId;
    private final String status;
    private final String link;
    private final String title;
    private final String preview;
    private final String full;

    public SuccessGetArticleWithIdResponse(int id, int FeedId, String status, String link, String title, String preview, String full) {
        this.id = id;
        this.FeedId = FeedId;
        this.status = status;
        this.link = link;
        this.title = title;
        this.preview = preview;
        this.full = full;
    }

    public int getId() {
        return id;
    }

    @JsonProperty("feed_id")
    public int getFeedId() {
        return FeedId;
    }

    public String getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getFull() {
        return full;
    }
    
    
}
