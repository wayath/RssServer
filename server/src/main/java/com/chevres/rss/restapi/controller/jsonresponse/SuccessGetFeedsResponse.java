/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.jsonresponse;

import java.util.List;

/**
 *
 * @author anthony
 */
public class SuccessGetFeedsResponse {

    private final List<SuccessGetFeedWithIdResponse> feeds;

    public SuccessGetFeedsResponse(List<SuccessGetFeedWithIdResponse> feeds) {
        this.feeds = feeds;
    }

    public List<SuccessGetFeedWithIdResponse> getFeeds() {
        return feeds;
    }
}
