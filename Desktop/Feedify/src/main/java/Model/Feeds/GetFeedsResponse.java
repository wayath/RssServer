/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Feeds;

import java.util.List;


/**
 *
 * @author Bastien
 */

public class GetFeedsResponse {
    private List<Feed> feeds;
    private int new_articles;

    public void setNew_articles(int new_articles) {
        this.new_articles = new_articles;
    }

    public int getNew_articles() {
        return new_articles;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }
}