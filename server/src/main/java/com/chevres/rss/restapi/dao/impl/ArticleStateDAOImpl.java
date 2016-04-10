/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.ArticleStateDAO;
import com.chevres.rss.restapi.model.ArticleState;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author zanchi_r
 */
public class ArticleStateDAOImpl extends AbstractGenericDAO implements ArticleStateDAO {
    @Override
    public ArticleState findByLabel(String label) {
        Session session = this.getSessionFactory().openSession();
        
        Criteria criteria = session.createCriteria(ArticleState.class);
        ArticleState articleState = (ArticleState) criteria.add(Restrictions.eq("label", label)).uniqueResult();
        session.close();
        return (articleState);
    }
}
