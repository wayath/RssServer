/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.jsonresponse;

/**
 *
 * @author anthony
 */
public class SuccessFeedInfoResponse {
    private final int id;
    private final String name;
    private final String url;

    public SuccessFeedInfoResponse(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
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
    
    
}
