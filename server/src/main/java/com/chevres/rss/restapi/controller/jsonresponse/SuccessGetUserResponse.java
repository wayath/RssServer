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
public class SuccessGetUserResponse {

    private final String username;
    private final String type;

    public SuccessGetUserResponse(String username, String type) {
        this.username = username;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

}
