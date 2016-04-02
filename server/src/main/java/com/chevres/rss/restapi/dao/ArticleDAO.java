/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.Feed;
import java.util.List;

/**
 *
 * @author anthony
 */
public interface ArticleDAO extends GenericDAO {

    public List<Article> findArticlesByPageId(List<Feed> feeds, int pageNumber);
    
    public List<Article> findArticlesByFeedAndPageId(Feed feed, int pageNumber);
}
