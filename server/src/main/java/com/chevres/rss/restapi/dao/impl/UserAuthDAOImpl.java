/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao.impl;

import com.chevres.rss.restapi.dao.UserAuthDAO;
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
public class UserAuthDAOImpl extends AbstractGenericDAO implements UserAuthDAO {

    
    @Override
    public UserAuth findByToken(String token) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(UserAuth.class);
        UserAuth ua = (UserAuth) criteria.add(Restrictions.eq("token", token)).uniqueResult();
        session.close();

        return ua;
    }

    @Override
    public void removeAllForUser(User user) {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        String id = Integer.toString(user.getId());
        String hql = "delete from UserAuth where id_user= :userId";
        session.createQuery(hql).setString("userId", id).executeUpdate();

        tx.commit();
        session.close();
    }

}
