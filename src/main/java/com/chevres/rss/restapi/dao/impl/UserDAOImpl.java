/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.UserDAO;
import com.chevres.rss.restapi.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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
    public void updatePassword(int id, String password) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String hqlUpdate = "UPDATE user set password = :password where id = :id";
        session.createQuery(hqlUpdate)
                .setString("password", password)
                .setString("id", Integer.toString(id))
                .executeUpdate();
        tx.commit();
        session.close();
    }

    @Override
    public boolean doesExist(String username) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        User user = (User) criteria.add(Restrictions.eq("username", username)).uniqueResult();

        session.close();

        if (user != null) {
            return true;
        }

        return false;
    }

}
