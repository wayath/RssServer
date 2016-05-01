/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Articles;

import Model.Articles.GetArticleResponse.Articles;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bastien
 */
public class AllArticlesModel {
    public static AllArticlesModel Instance;
    public List<Articles> allArticles;
    public Boolean isTheFeedDead;

    public void setIsTheFeedDead(Boolean isTheFeedDead) {
        this.isTheFeedDead = isTheFeedDead;
    }

    public Boolean getIsTheFeedDead() {
        return isTheFeedDead;
    }

    public void setAllArticles(List<Articles> allArticles) {
        this.allArticles = allArticles;
    }

    public List<Articles> getAllArticles() {
        return allArticles;
    }
    
    public AllArticlesModel() {
        Instance = this;
        isTheFeedDead = false;
    }
}
