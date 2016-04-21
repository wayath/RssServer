/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.GenericDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author anthony
 */
public abstract class AbstractGenericDAO implements GenericDAO {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Object o) {
        Session session = this.sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            session.persist(o);
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
    public void delete(Object o) {
        Session session = this.sessionFactory.openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            session.close();
        }
    }

}
