/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.UserAuthDAO;
import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.sql.Timestamp;
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
public class UserAuthDAOImplTest {

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserAuthDAO userAuthDAO;

    /**
     * Test of findByToken method, of class UserAuthDAOImpl.
     */
    @Test
    public void testFindByToken() {
        assertNull(userAuthDAO.findByToken("fefezfzeggr"));
        assertNotNull(userAuthDAO.findByToken("upok4dqq2r7udqf9p5u21p0bh3"));
    }

    /**
     * Test of removeAllForUser method, of class UserAuthDAOImpl.
     */
    @Test
    public void testRemoveAllForUser() {
        String token = "upok4dqq2r7udqf9p5u21p0bh3";
        assertNotNull(userAuthDAO.findByToken(token));

        User user = userDAO.findById(1);
        userAuthDAO.removeAllForUser(user);

        assertNull(userAuthDAO.findByToken(token));
    }

    @Test
    public void testCreate() {
        String token = "fezfezgezgezg";
        Timestamp createDate = new Timestamp(1132);
        
        UserAuth userAuth = new UserAuth();
        userAuth.setIdUser(1);
        userAuth.setToken(token);
        userAuth.setCreateDate(createDate);

        userAuthDAO.create(userAuth);

        UserAuth createdUserAuth = userAuthDAO.findByToken(token);
        assertNotNull(createdUserAuth);
        assertEquals(createdUserAuth.getId(), 5);
        assertEquals(createdUserAuth.getCreateDate(), createDate);
        assertEquals(createdUserAuth.getIdUser(), 1);
        assertEquals(createdUserAuth.getToken(), token);
    }

    @Test
    public void testDelete() {
        String token = "upok4dqq2r7udqf9p5u21p0bh3";
        UserAuth userAuth = userAuthDAO.findByToken(token);
        assertNotNull(userAuth);

        userAuthDAO.delete(userAuth);

        userAuth = userAuthDAO.findByToken(token);
        assertNull(userAuth);
    }

}
