/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
public class UserDAOImplTest {

    @Autowired
    UserDAO userDao;

    /**
     * Test of findById method, of class UserDAOImpl.
     */
    @Test
    @DatabaseSetup("classpath:rss-dataset.xml")
    public void testFindById() {
        System.out.println("findById");
        User user = userDao.findById(0);
        assertNull(user);
        user = userDao.findById(1);
        assertNotNull(user);
    }

}
