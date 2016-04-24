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
    }

    /**
     * Test of findById method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindById() {
    }

    /**
     * Test of findArticlesByPageId method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindArticlesByPageId() {
    }

    /**
     * Test of findArticlesByFeedAndPageId method, of class ArticleDAOImpl.
     */
    @Test
    public void testFindArticlesByFeedAndPageId() {
    }

    /**
     * Test of markAsRead method, of class ArticleDAOImpl.
     */
    @Test
    public void testMarkAsRead() {
    }

    /**
     * Test of markAllArticlesInFeedAsRead method, of class ArticleDAOImpl.
     */
    @Test
    public void testMarkAllArticlesInFeedAsRead() {
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
