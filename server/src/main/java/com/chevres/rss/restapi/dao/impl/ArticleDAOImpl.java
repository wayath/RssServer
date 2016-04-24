/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.ArticleDAO;
import com.chevres.rss.restapi.model.Article;
import com.chevres.rss.restapi.model.ArticleState;
import com.chevres.rss.restapi.model.Feed;
import com.chevres.rss.restapi.model.UserAuth;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author anthony
 */
public class ArticleDAOImpl extends AbstractGenericDAO implements ArticleDAO {

    private static final int ARTICLE_BY_PAGE = 10;

    @Override
    public Boolean doesExist(Feed feed, String link) {
        Article article;
        Session session = this.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(Article.class);
            article = (Article) criteria.add(Restrictions.eq("link", link))
                    .add(Restrictions.eq("feed", feed)).uniqueResult();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        return (article != null);
    }

    @Override
    public Article findById(UserAuth userAuth, int id) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Article.class);
            Article article = (Article) criteria.add(Restrictions.eq("id", id)).uniqueResult();
            tx.commit();

            if (article != null) {
                if (article.getFeed().getIdUser() == userAuth.getIdUser()) {
                    return article;
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Article> findArticlesByPageId(List<Feed> feeds, int pageNumber) {
        List<Article> articles;
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Article.class);
            articles = criteria
                    .add(Restrictions.in("feed", feeds))
                    .addOrder(Order.desc("pubDate"))
                    .setFirstResult((pageNumber - 1) * ARTICLE_BY_PAGE)
                    .setMaxResults(ARTICLE_BY_PAGE).list();

            tx.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        return articles;
    }

    @Override
    public List<Article> findArticlesByFeedAndPageId(Feed feed, int pageNumber) {
        List<Article> articles;
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Article.class);
            articles = criteria
                    .add(Restrictions.eq("feed", feed))
                    .addOrder(Order.desc("pubDate"))
                    .setFirstResult((pageNumber - 1) * ARTICLE_BY_PAGE)
                    .setMaxResults(ARTICLE_BY_PAGE).list();
            tx.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        return articles;
    }

    @Override
    public void markAsRead(Article article) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            ArticleState articleState = new ArticleState();
            articleState.setLabel(ArticleState.READ_LABEL);
            articleState.setStatus(ArticleState.READ_STATUS);
            article.setStatus(articleState);

            session.update(article);

            tx.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void markAllArticlesInFeedAsRead(Feed feed) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx;
        try {
            tx = session.beginTransaction();
            ArticleState articleState = new ArticleState();
            articleState.setLabel(ArticleState.READ_LABEL);
            articleState.setStatus(ArticleState.READ_STATUS);

            String hql = "update Article set status = :newState "
                    + "where feed = :feedObject";
            Query query = session.createQuery(hql);
            query.setParameter("newState", articleState);
            query.setParameter("feedObject", feed);
            query.executeUpdate();

            tx.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
