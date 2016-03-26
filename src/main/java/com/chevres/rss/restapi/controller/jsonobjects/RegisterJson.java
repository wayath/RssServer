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
public class RegisterJson {
    private String username;
    private String password;
    private int client;

    public RegisterJson() {
    }

    public RegisterJson(String username, String password, int client) {
        this.username = username;
        this.password = password;
        this.client = client;
    }
    
    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
