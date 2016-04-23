/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.model.ArticleState;
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
 * @author anthony
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:datasets/dataset.xml")
public class ArticleStateDAOImplTest {

    @Autowired
    ArticleStateDAO articleStateDAO;

    /**
     * Test of findByLabel method, of class ArticleStateDAOImpl.
     */
    @Test
    public void testFindByLabel() {
        ArticleState articleState = articleStateDAO.findByLabel("new");
        assertNotNull(articleState);
        assertEquals(articleState.getId(), 1);
        assertEquals(articleState.getLabel(), "new");
        assertFalse(articleState.getStatus());
        articleState = articleStateDAO.findByLabel("lol");
        assertNull(articleState);
    }

}
