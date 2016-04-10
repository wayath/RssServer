/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chevres.rss.restapi.dao;

import com.chevres.rss.restapi.model.ArticleState;

/**
 *
 * @author zanchi_r
 */
public interface ArticleStateDAO extends GenericDAO {
    public ArticleState findByLabel(String label);
}