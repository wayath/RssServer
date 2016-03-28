/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author anthony
 */
public class UserDAOImpl extends AbstractGenericDAO implements UserDAO {

    @Override
    public User findById(int id) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        User u = (User) session.get(User.class, id);
        tx.commit();
        session.close();
        return u;
    }

    @Override
    public User findByUsername(String username) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();

        session.close();
        return user;
    }

    @Override
    public void updateUser(User oldUser, User newUser, boolean isUpdaterAdmin) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newUser.getPassword());
            oldUser.setPassword(hashedPassword);
        }
        if (isUpdaterAdmin && newUser.getType() != null) {
            oldUser.setType(newUser.getType());
        }

        session.update(oldUser);
        tx.commit();
        session.close();
    }

    @Override
    public boolean doesExist(String username) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();

        session.close();

        return user != null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();

        session.close();
        if (user != null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean doesMatch = passwordEncoder.matches(password, user.getPassword());
            return doesMatch ? user : null;
        }
        return null;
    }

    @Override
    public boolean isAdmin(int idUser) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        User u = (User) session.get(User.class, idUser);
        tx.commit();
        session.close();

        return u.getType().equalsIgnoreCase(User.ADMIN_TYPE_LABEL);
    }

    @Override
    public List<User> findEveryone() {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<User> users = session.createCriteria(User.class).list();
        tx.commit();
        session.close();
        
        return users;
    }

}
