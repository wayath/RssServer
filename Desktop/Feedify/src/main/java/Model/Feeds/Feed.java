/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Feeds;

/**
 *
 * @author Bastien
 */
public class Feed {
    private int id;
    private String name;
    private String url;
    private Boolean refresh_error;
    private int new_articles;


    public void setNew_articles(int new_articles) {
        this.new_articles = new_articles;
    }

    public int getNew_articles() {
        return new_articles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRefresh_error(Boolean refresh_error) {
        this.refresh_error = refresh_error;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getRefresh_error() {
        return refresh_error;
    }
    
    public static class FeedPost
    {
        private String  name;
        private String  url;

        public FeedPost(String name, String url) {
            this.name = name;
            this.url = url;
        }
        
        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
