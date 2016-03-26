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
public class RegisterJsonStatus {

    private String status;
    private String token;

    public RegisterJsonStatus() {
        this.status = "error";
        this.token = null;
    }

    public RegisterJsonStatus(String status) {
        this.token = null;
        this.status = status;
    }
    
    public RegisterJsonStatus(String status, String token) {
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
