/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.User;

/**
 *
 * @author anthony
 */
public interface FeedDAO extends GenericDAO {
    
    public boolean doesExist(String url);
    
    public void removeAllForUser(User user);
}
