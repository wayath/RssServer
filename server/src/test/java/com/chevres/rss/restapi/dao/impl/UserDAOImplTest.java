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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserDAOImplTest {

    @Autowired
    UserDAO userDao;

    /**
     * Test of findById method, of class UserDAOImpl.
     */
    @Test
    public void testFindById() {
        User user = userDao.findById(0);
        assertNull(user);
        user = userDao.findById(1);
        assertNotNull(user);
    }

    /**
     * Test of findByUsername method, of class UserDAOImpl.
     */
    @Test
    public void testFindByUsername() {
        User user = userDao.findByUsername("notexistuser");
        assertNull(user);
        user = userDao.findByUsername("user1");
        assertNotNull(user);
    }

    /**
     * Test of updateUser method, of class UserDAOImpl.
     */
    @Test
    public void testUpdateUser() {
        User oldUser = userDao.findByUsername("user1");
        User newUser = new User();
        newUser.setUsername("Anthony");
        newUser.setPassword("updatepwd");
        newUser.setType("admin");
        userDao.updateUser(oldUser, newUser, true);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean doesMatch = passwordEncoder.matches("updatepwd", oldUser.getPassword());

        assertEquals(oldUser.getUsername(), "Anthony");
        assertTrue(doesMatch);
        assertEquals(oldUser.getType(), "admin");
    }

    /**
     * Test of doesExist method, of class UserDAOImpl.
     */
    @Test
    public void testDoesExist() {
        assertTrue(userDao.doesExist("user1"));
        assertFalse(userDao.doesExist("notregistereduser"));
    }

    /**
     * Test of findByUsernameAndPassword method, of class UserDAOImpl.
     */
    @Test
    public void testFindByUsernameAndPassword() {
        assertNotNull(userDao.findByUsernameAndPassword("user1", "password"));
        assertNull(userDao.findByUsernameAndPassword("user1", "passwor"));
        assertNull(userDao.findByUsernameAndPassword("user", "password"));
    }

    /**
     * Test of isAdmin method, of class UserDAOImpl.
     */
    @Test
    public void testIsAdmin() {
        assertFalse(userDao.isAdmin(1));
        assertTrue(userDao.isAdmin(4));
    }

    /**
     * Test of findEveryone method, of class UserDAOImpl.
     */
    @Test
    public void testFindEveryone() {
        List<User> userList = userDao.findEveryone();
        assertEquals(userList.size(), 4);
        assertNotEquals(userList.size(), 2);
    }

    @Test
    public void testCreate() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("pwd");
        user.setType("user");

        userDao.create(user);
        User createdUser = userDao.findByUsername("username");
        assertNotNull(createdUser);
        assertEquals(createdUser.getId(), 5);
        assertEquals(createdUser.getUsername(), "username");
        assertEquals(createdUser.getPassword(), "pwd");
        assertEquals(createdUser.getType(), "user");
    }

    @Test
    public void testDelete() {
        User user = userDao.findByUsername("user1");
        assertNotNull(user);

        userDao.delete(user);

        user = userDao.findByUsername("user1");
        assertNull(user);
    }

}
