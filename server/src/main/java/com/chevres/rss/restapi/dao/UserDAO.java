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
public interface UserDAO extends GenericDAO {
    
    public User findById(int id);
    
    public void updatePassword(int id, String password);
    
    public boolean doesExist(String username);
    
    public User findByUsernameAndPassword(String username, String password);
}
