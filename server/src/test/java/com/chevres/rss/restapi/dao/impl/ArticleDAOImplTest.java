/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 *
 * @author anthony
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:datasets/dataset.xml")
public class ArticleDAOImplTest {

    private static final String TOKEN_USER1 = "upok4dqq2r7udqf9p5u21p0bh3";

    @Autowired
    UserAuthDAO userAuthDAO;
    @Autowired
    FeedDAO feedDAO;
    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    ArticleStateDAO articleStateDAO;

    /**
     * Test of doesExist method, of class ArticleDAOImpl.
     */
    @Test
    public void testDoesExist() {
        String link = "http://article.rss.com";
        String fakeLink = "http://articlefake.rss.com";
        Feed feed = feedDAO.findById(1);
        assertTrue(articleDAO.doesExist(feed, link));
        assertFalse(articleDAO.doesExist(feed, fakeLink));
    }

    /**
     * Test of findById method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindById() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        Article article = articleDAO.findById(userAuth, 1);
        Article articleFake = articleDAO.findById(userAuth, 4);
        assertNotNull(article);
        assertNull(articleFake);
    }

    /**
     * Test of findArticlesByPageId method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindArticlesByPageId() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        List<Feed> feeds = feedDAO.findAll(userAuth);
        List<Article> articles = articleDAO.findArticlesByPageId(feeds, 1);
        assertEquals(articles.size(), 3);
        articles = articleDAO.findArticlesByPageId(feeds, 2);
        assertEquals(articles.size(), 0);
    }

    /**
     * Test of findArticlesByFeedAndPageId method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindArticlesByFeedAndPageId() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        Feed feed = feedDAO.findById(userAuth, 1);
        Feed feed2 = feedDAO.findById(userAuth, 2);
        List<Article> articles = articleDAO.findArticlesByFeedAndPageId(feed, 1);
        List<Article> articles2 = articleDAO.findArticlesByFeedAndPageId(feed2, 1);
        assertEquals(articles.size(), 2);
        assertEquals(articles2.size(), 1);
        articles = articleDAO.findArticlesByFeedAndPageId(feed, 2);
        assertEquals(articles.size(), 0);
    }

    /**
     * Test of markAsRead method, of class ArticleDAOImpl.
     */
    @Test
    public void testMarkAsRead() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        Article article = articleDAO.findById(userAuth, 1);
        assertEquals(article.getStatus().getLabel(), ArticleState.NEW_LABEL);

        ArticleState readState = articleStateDAO.findByLabel(ArticleState.READ_LABEL);
        articleDAO.markAsRead(article, readState);

        article = articleDAO.findById(userAuth, 1);
        assertEquals(article.getStatus().getLabel(), ArticleState.READ_LABEL);
    }

    /**
     * Test of markAllArticlesInFeedAsRead method, of class ArticleDAOImpl.
     */
    @Test
    public void testMarkAllArticlesInFeedAsRead() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        Feed feed = feedDAO.findById(userAuth, 1);

        for (Article article : articleDAO.findArticlesByFeedAndPageId(feed, 1)) {
            assertEquals(article.getStatus().getLabel(), ArticleState.NEW_LABEL);
        }

        ArticleState readState = articleStateDAO.findByLabel(ArticleState.READ_LABEL);
        articleDAO.markAllArticlesInFeedAsRead(feed, readState);

        for (Article article : articleDAO.findArticlesByFeedAndPageId(feed, 1)) {
            assertEquals(article.getStatus().getLabel(), ArticleState.READ_LABEL);
        }
    }

    @Test
    public void testCreate() {
        Feed feed = feedDAO.findById(1);
        ArticleState articleState = articleStateDAO.findByLabel(ArticleState.NEW_LABEL);
        UserAuth userAuth = userAuthDAO.findByToken("upok4dqq2r7udqf9p5u21p0bh3");

        assertNull(articleDAO.findById(userAuth, 4));

        Article article = new Article();
        article.setFeed(feed);
        article.setLink("http://url.com/rss");
        article.setPreviewContent("preview");
        article.setFullContent("full");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        article.setPubDate(timestamp);
        article.setStatus(articleState);
        article.setTitle("My Article for test");
        articleDAO.create(article);

        assertNotNull(articleDAO.findById(userAuth, 4));
    }

}
