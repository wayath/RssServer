/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.controller.jsonobjects;

/**
 *
 * @author anthony
 */
public class LoginJsonStatus {

    private final String status;
    private final String token;

    public LoginJsonStatus() {
        this.status = "error";
        this.token = null;
    }

    public LoginJsonStatus(String status) {
        this.token = null;
        this.status = status;
    }
    
    public LoginJsonStatus(String status, String token) {
        this.token = token;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

}
