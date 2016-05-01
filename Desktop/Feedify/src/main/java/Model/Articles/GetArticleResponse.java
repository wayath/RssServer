/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Articles;

import java.util.List;

/**
 *
 * @author Bastien
 */
public class GetArticleResponse {
    List<Articles> articles;

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public List<Articles> getArticles() {
        return articles;
    }
 
    public class Articles {
        private int id;
        private int feed_id;
        private String status;
        private String link;
        private String title;
        private String preview;
        private String full;

        public void setId(int id) {
            this.id = id;
        }

        public void setFeed_id(int feed_id) {
            this.feed_id = feed_id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public int getId() {
            return id;
        }

        public int getFeed_id() {
            return feed_id;
        }

        public String getStatus() {
            return status;
        }

        public String getLink() {
            return link;
        }

        public String getTitle() {
            return title;
        }

        public String getPreview() {
            return preview;
        }

        public String getFull() {
            return full;
        }
    }
}
