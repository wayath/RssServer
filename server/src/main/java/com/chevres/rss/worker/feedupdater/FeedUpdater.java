/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.feedupdater;

import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.Feed;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author zanchi_r
 */
public class FeedUpdater {
        
    public FeedUpdater() {
    }

    public Boolean updateFeed(Feed feed) {
        if (feed == null) {
            return (false);
        }
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        ArticleDAO articleDAO = context.getBean(ArticleDAO.class);
        List<Article> articles;
        
        RssParser parser = new RssParser();
        try {
            articles = parser.parseFeed(feed);
            for (Article article : articles) {
                if (article.prepare() && !articleDAO.doesExist(article.getFeed(), article.getLink())) {
                    articleDAO.create(article);
                }
            }
            feedDAO.updateRefreshError(feed, false);
        }
        catch (Exception e) {
            System.out.println("FEED UPDATE ERROR");
            System.out.println(e.getMessage());
            feedDAO.updateRefreshError(feed, true);
            return (false);
        }
        finally {
           context.close(); 
        }
        return (true);
    }
    
    public Boolean updateFeedById(int feedId) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);
        
        Feed feed = feedDAO.findById(feedId);
        if (feed == null) {
            context.close();
            return (false);
        }
        context.close();
        return (this.updateFeed(feed));
    }
    
    public Boolean updateAll() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        FeedDAO feedDAO = context.getBean(FeedDAO.class);

        List<Feed> feeds = feedDAO.findAll();
        for (Feed feed : feeds) {
            this.updateFeed(feed);
        }
        context.close();
        return (true);
    }
}

