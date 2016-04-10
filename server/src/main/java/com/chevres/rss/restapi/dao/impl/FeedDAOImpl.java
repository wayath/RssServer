/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.FeedDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.ArticleState;
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

    @Override
    public List<Feed> findAll() {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Feed.class);
        List<Feed> feeds = criteria.list();
        tx.commit();
        session.close();

        return feeds;
    }
    
    @Override
    public Feed findById(UserAuth userAuth, int id) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Feed.class);
        Feed f = (Feed) criteria.add(Restrictions.eq("idUser", userAuth.getIdUser()))
                .add(Restrictions.eq("id", id)).uniqueResult();
        tx.commit();
        session.close();

        return f;
    }
    
    @Override
    public Feed findById(int id) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Feed.class);
        Feed f = (Feed) criteria.add(Restrictions.eq("id", id)).uniqueResult();
        tx.commit();
        session.close();

        return f;
    }

    @Override
    public void updateName(Feed oldFeed, Feed newFeed) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        oldFeed.setName(newFeed.getName());

        session.update(oldFeed);
        tx.commit();
        session.close();
    }

    @Override
    public int getNewArticles(List<Feed> feeds) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        ArticleState newArticle = new ArticleState();
        newArticle.setLabel(ArticleState.NEW_LABEL);
        newArticle.setStatus(ArticleState.NEW_STATUS);
        
        
        Criteria criteria = session.createCriteria(Article.class);
        int newArticles = criteria
                .add(Restrictions.in("feed", feeds))
                .add(Restrictions.eq("status", newArticle))
                .list().size();
        
        tx.commit();
        session.close();
        
        return newArticles;
    }

    @Override
    public int getNewArticlesByFeed(Feed feed) {
         Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        ArticleState newArticle = new ArticleState();
        newArticle.setLabel(ArticleState.NEW_LABEL);
        newArticle.setStatus(ArticleState.NEW_STATUS);
        
        
        Criteria criteria = session.createCriteria(Article.class);
        int newArticles = criteria
                .add(Restrictions.eq("feed", feed))
                .add(Restrictions.eq("status", newArticle))
                .list().size();
        
        tx.commit();
        session.close();
        
        return newArticles;
    }

}
