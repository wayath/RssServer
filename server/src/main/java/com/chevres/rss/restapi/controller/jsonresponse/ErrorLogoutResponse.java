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
public class ErrorLogoutResponse {
    private final String message;

    public ErrorLogoutResponse(String status) {
        this.message = status;
    }
    
    public ErrorLogoutResponse(String status, String token) {
        this.message = status;
    }

    public String getMessage() {
        return message;
    }
}
