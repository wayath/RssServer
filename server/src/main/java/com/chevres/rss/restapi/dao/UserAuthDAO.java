/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.List;

/**
 *
 * @author anthony
 */
public interface UserAuthDAO extends GenericDAO {
    
    public List<UserAuth> findByUsername(String username);

    public UserAuth findByToken(String token);
    
    public void removeAllForUser(User user);
}
