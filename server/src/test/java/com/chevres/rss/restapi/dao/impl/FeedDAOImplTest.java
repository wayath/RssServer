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
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class FeedDAOImplTest {

    private static final String TOKEN_USER1 = "upok4dqq2r7udqf9p5u21p0bh3";

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserAuthDAO userAuthDAO;
    @Autowired
    FeedDAO feedDAO;
    @Autowired
    ArticleStateDAO articleStateDAO;
    @Autowired
    ArticleDAO articleDAO;

    /**
     * Test of doesExist method, of class FeedDAOImpl.
     */
    @Test
    public void testDoesExist() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        assertTrue(feedDAO.doesExist(userAuth, "http://rss.feed.com/"));
        assertFalse(feedDAO.doesExist(userAuth, "http://fake.feed.com/"));
        assertFalse(feedDAO.doesExist(userAuth, "http://rs.feed.com/"));
    }

    /**
     * Test of removeAllForUser method, of class FeedDAOImpl.
     */
    @Test
    public void testRemoveAllForUser() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        assertTrue(feedDAO.doesExist(userAuth, "http://rss.feed.com/"));
        for (Feed feed : feedDAO.findAll(userAuth)) {
            feedDAO.deleteArticles(feed);
        }
        feedDAO.removeAllForUser(userDAO.findById(1));
        assertFalse(feedDAO.doesExist(userAuth, "http://rss.feed.com/"));
    }

    /**
     * Test of findAll method, of class FeedDAOImpl.
     */
    @Test
    public void testFindAll() {
        assertEquals(feedDAO.findAll().size(), 3);
    }

    /**
     * Test of findAll method, of class FeedDAOImpl.
     */
    @Test
    public void testFindAllAuth() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        assertEquals(feedDAO.findAll(userAuth).size(), 2);
    }

    /**
     * Test of findById method, of class FeedDAOImpl.
     */
    @Test
    public void testFindByIdAuth() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        assertNotNull(feedDAO.findById(userAuth, 1));
        assertNull(feedDAO.findById(userAuth, 3));
        assertNull(feedDAO.findById(userAuth, 4));
    }

    /**
     * Test of findById method, of class FeedDAOImpl.
     */
    @Test
    public void testFindById() {
        assertNotNull(feedDAO.findById(1));
        assertNotNull(feedDAO.findById(2));
        assertNotNull(feedDAO.findById(3));
        assertNull(feedDAO.findById(4));
    }

    /**
     * Test of updateName method, of class FeedDAOImpl.
     */
    @Test
    public void testUpdateName() {
        Feed oldFeed = feedDAO.findById(1);
        Feed newFeed = oldFeed;
        newFeed.setName("Feed rename");
        feedDAO.updateName(oldFeed, newFeed);
        oldFeed = feedDAO.findById(1);
        assertEquals(oldFeed.getName(), "Feed rename");
    }

    /**
     * Test of updateRefreshError method, of class FeedDAOImpl.
     */
    @Test
    public void testUpdateRefreshError() {
        Feed feed = feedDAO.findById(1);
        assertEquals(feed.getRefreshError(), false);

        feedDAO.updateRefreshError(feed, true);

        assertEquals(feed.getRefreshError(), true);
    }

    /**
     * Test of getNewArticles method, of class FeedDAOImpl.
     */
    @Test
    public void testGetNewArticles() {
        List<Feed> feeds = feedDAO.findAll();
        ArticleState newState = articleStateDAO.findByLabel(ArticleState.NEW_LABEL);
        assertEquals(feedDAO.getNewArticles(feeds, newState), 2);
    }

    /**
     * Test of getNewArticlesByFeed method, of class FeedDAOImpl.
     */
    @Test
    public void testGetNewArticlesByFeed() {
        Feed feed = feedDAO.findById(1);
        ArticleState newState = articleStateDAO.findByLabel(ArticleState.NEW_LABEL);
        assertEquals(feedDAO.getNewArticlesByFeed(feed, newState), 2);
    }

    /**
     * Test of deleteArticles method, of class FeedDAOImpl.
     */
    @Test
    public void testDeleteArticles() {
        UserAuth userAuth = userAuthDAO.findByToken(TOKEN_USER1);
        Feed feed = feedDAO.findById(userAuth, 1);
        assertNotNull(articleDAO.findById(userAuth, 1));
        assertNotNull(articleDAO.findById(userAuth, 2));

        feedDAO.deleteArticles(feed);

        assertNull(articleDAO.findById(userAuth, 1));
        assertNull(articleDAO.findById(userAuth, 2));
    }

}
