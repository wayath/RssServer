/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.User;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author anthony
 */
public class FeedDAOImpl extends AbstractGenericDAO implements FeedDAO {

    @Override
    public boolean doesExist(UserAuth userAuth, String url) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(Feed.class);
        Feed feed = (Feed) criteria.add(Restrictions.eq("url", url))
                .add(Restrictions.eq("idUser", userAuth.getIdUser())).uniqueResult();

        session.close();

        return feed != null;
    }

    @Override
    public void removeAllForUser(User user) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        String id = Integer.toString(user.getId());
        String hql = "delete from Feed where id_user= :userId";
        session.createQuery(hql).setString("userId", id).executeUpdate();

        tx.commit();
        session.close();
    }

    @Override
    public List<Feed> findAll(UserAuth userAuth) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Feed.class);
        List<Feed> feeds = criteria.add(Restrictions.eq("idUser", userAuth.getIdUser())).list();
        tx.commit();
        session.close();

        return feeds;
    }

}
