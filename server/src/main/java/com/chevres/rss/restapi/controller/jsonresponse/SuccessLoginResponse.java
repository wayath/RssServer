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
public class SuccessLoginResponse {

    private final String token;

    public SuccessLoginResponse(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }

}
