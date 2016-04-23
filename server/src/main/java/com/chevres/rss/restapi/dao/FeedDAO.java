/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.List;

/**
 *
 * @author anthony
 */
public interface FeedDAO extends GenericDAO {
    
    public boolean doesExist(UserAuth userAuth, String url);
    
    public void removeAllForUser(User user);
    
    public Feed findById(UserAuth userAuth, int id);
    
    public Feed findById(int id);
    
    public List<Feed> findAll(UserAuth userAuth);
    
    public List<Feed> findAll();
    
    public void updateName(Feed oldFeed, Feed newFeed);
    
    public void updateRefreshError(Feed feed, boolean refreshError);
    
    public int getNewArticles(List<Feed> feeds, ArticleState newState);
    
    public int getNewArticlesByFeed(Feed feed);
    
    public void deleteArticles(Feed feed);
}
