/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.feedupdater;

import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.Feed;
import java.util.List;

/**
 *
 * @author zanchi_r
 */
public class FeedUpdater {

    private final FeedDAO feedDAO;
    private final ArticleDAO articleDAO;
    private final ArticleStateDAO articleStateDAO;

    public FeedUpdater(FeedDAO feedDAO, ArticleDAO articleDAO, ArticleStateDAO articleStateDAO) {
        this.feedDAO = feedDAO;
        this.articleDAO = articleDAO;
        this.articleStateDAO = articleStateDAO;
    }

    public Boolean updateFeed(Feed feed) {
        if (feed == null) {
            return (false);
        }
        List<Article> articles;

        RssParser parser = new RssParser(articleStateDAO);
        try {
            articles = parser.parseFeed(feed);
            for (Article article : articles) {
                if (article.prepare() && !articleDAO.doesExist(article.getFeed(), article.getLink())) {
                    articleDAO.create(article);
                }
            }
            feedDAO.updateRefreshError(feed, false);
        } catch (Exception e) {
            feedDAO.updateRefreshError(feed, true);
            return (false);
        }
        return (true);
    }

    public Boolean updateFeedById(int feedId) {
        Feed feed = feedDAO.findById(feedId);

        if (feed == null) {
            return (false);
        }
        return (this.updateFeed(feed));
    }

    public Boolean updateAll() {
        List<Feed> feeds = feedDAO.findAll();
        for (Feed feed : feeds) {
            this.updateFeed(feed);
        }
        return (true);
    }
}
