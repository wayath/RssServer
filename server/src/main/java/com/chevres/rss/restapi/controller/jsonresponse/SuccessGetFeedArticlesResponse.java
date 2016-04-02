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
public class SuccessGetFeedArticlesResponse {
    private final List<SuccessGetArticleWithIdResponse> articles;

    public SuccessGetFeedArticlesResponse(List<SuccessGetArticleWithIdResponse> articles) {
        this.articles = articles;
    }

    public List<SuccessGetArticleWithIdResponse> getArticles() {
        return articles;
    }
    
    
}
