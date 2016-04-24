/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.worker.feedupdater;

import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.dao.FeedDAO;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
 * @author zanchi_r
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:datasets/dataset_worker.xml")
public class FeedUpdaterTest {
    
    @Autowired
    FeedDAO feedDAO;
    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    ArticleStateDAO articleStateDAO;
    
    @Test
    public void testUpdateFeedById() {
        FeedUpdater feedUpdater = new FeedUpdater(feedDAO, articleDAO, articleStateDAO);
        assertFalse(feedUpdater.updateFeedById(420));
        assertFalse(feedUpdater.updateFeedById(1));
        assertTrue(feedUpdater.updateFeedById(2));
        assertFalse(feedUpdater.updateFeedById(3));
    }
}
