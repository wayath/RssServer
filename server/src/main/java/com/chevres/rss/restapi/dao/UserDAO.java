/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.User;
import java.util.List;

/**
 *
 * @author anthony
 */
public interface UserDAO extends GenericDAO {
    
    public User findById(int id);

    public User findByUsername(String username);

    public User findByUsernameAndPassword(String username, String password);
    
    public List<User> findEveryone();
    
    public void updateUser(User oldUser, User newUser, boolean isUpdaterAdmin);
    
    public boolean doesExist(String username);
    
    public boolean isAdmin(int idUser);
    
}
