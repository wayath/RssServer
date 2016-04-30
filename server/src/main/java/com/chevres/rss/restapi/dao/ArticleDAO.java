/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.List;

/**
 *
 * @author anthony
 */
public interface ArticleDAO extends GenericDAO {
    
    public Boolean doesExist(Feed feed, String link);

    public Article findById(UserAuth userAuth, int id);
    
    public List<Article> findArticlesByPageId(List<Feed> feeds, int pageNumber);
    
    public List<Article> findArticlesByFeedAndPageId(Feed feed, int pageNumber);
    
    public void markAsRead(Article article, ArticleState newState);
    
    public void markAllArticlesInFeedAsRead(Feed feed, ArticleState newState);
}
