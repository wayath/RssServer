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
public class SuccessGetUsersResponse {
    private final List<SuccessGetUserResponseWithId> users;

    public SuccessGetUsersResponse(List<SuccessGetUserResponseWithId> users) {
        this.users = users;
    }

    public List<SuccessGetUserResponseWithId> getUsers() {
        return users;
    }
    
}