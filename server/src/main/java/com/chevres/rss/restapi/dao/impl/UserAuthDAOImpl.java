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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author anthony
 */
public class UserAuthDAOImpl extends AbstractGenericDAO implements UserAuthDAO {

    @Override
    public List<UserAuth> findByUsername(String username) {
        Session session = this.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(User.class);
        User user = (User)criteria.add(Restrictions.eq("username", username)).uniqueResult();

        criteria = session.createCriteria(UserAuth.class);
        List<UserAuth> ua = criteria.add(Restrictions.eq("user_id", user.getId())).list();
        session.close();
        return ua;
    }

    @Override
    public void update(String username) {
    }

}
