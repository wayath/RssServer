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
        User u;
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            u = (User) session.get(User.class, id);
            tx.commit();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return u;
    }

    @Override
    public User findByUsername(String username) {
        User user;
        Session session = this.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return user;
    }

    @Override
    public void updateUser(User oldUser, User newUser, boolean isUpdaterAdmin) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();

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
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
    }

    @Override
    public boolean doesExist(String username) {
        User user;
        Session session = this.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return user != null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        Session session = this.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            User user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();

            if (user != null) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                boolean doesMatch = passwordEncoder.matches(password, user.getPassword());
                return doesMatch ? user : null;
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean isAdmin(int idUser) {
        User u;
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            u = (User) session.get(User.class, idUser);
            tx.commit();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return u.getType().equalsIgnoreCase(User.ADMIN_TYPE_LABEL);
    }

    @Override
    public List<User> findEveryone() {
        List<User> users;
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            users = session.createCriteria(User.class).list();
            tx.commit();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
        return users;
    }

}
