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
    public Article findById(UserAuth userAuth, int id) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Article.class);
        Article article = (Article) criteria.add(Restrictions.eq("id", id)).uniqueResult();

        tx.commit();
        session.close();

        if (article.getFeed().getIdUser() == userAuth.getIdUser()) {
            return article;
        }
        return null;
    }

    @Override
    public List<Article> findArticlesByPageId(List<Feed> feeds, int pageNumber) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Article.class);
        List<Article> articles = criteria
                .add(Restrictions.in("feed", feeds))
                .addOrder(Order.desc("pubDate"))
                .setFirstResult((pageNumber - 1) * ARTICLE_BY_PAGE)
                .setMaxResults(ARTICLE_BY_PAGE).list();

        tx.commit();
        session.close();

        return articles;
    }

    @Override
    public List<Article> findArticlesByFeedAndPageId(Feed feed, int pageNumber) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Criteria criteria = session.createCriteria(Article.class);
        List<Article> articles = criteria
                .add(Restrictions.eq("feed", feed))
                .addOrder(Order.desc("pubDate"))
                .setFirstResult((pageNumber - 1) * ARTICLE_BY_PAGE)
                .setMaxResults(ARTICLE_BY_PAGE).list();

        tx.commit();
        session.close();

        return articles;
    }

    @Override
    public void markAsRead(Article article) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        ArticleState articleState = new ArticleState();
        articleState.setLabel(ArticleState.READ_LABEL);
        articleState.setStatus(ArticleState.READ_STATUS);
        article.setStatus(articleState);

        session.update(article);

        tx.commit();
        session.close();

    }
}
