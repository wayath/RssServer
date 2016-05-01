package fr.do_f.rssfeedify.api.json.menu;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by do_f on 18/04/16.
 */
public class GetFeedResponse {

    @SerializedName("feeds")
    private List<Feed> feed;

    public List<Feed> getFeed() {
        return feed;
    }

    public void setFeed(List<Feed> feed) {
        this.feed = feed;
    }

    public static class Feed implements Serializable {

        private int id;
        private String name;
        private String url;
        @SerializedName("refresh_error")
        private boolean refreshError;

        @SerializedName("new_articles")
        private int newArticles;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isRefreshError() {
            return refreshError;
        }

        public void setRefreshError(boolean refreshError) {
            this.refreshError = refreshError;
        }

        public int getNewArticles() {
            return newArticles;
        }

        public void setNewArticles(int newArticles) {
            this.newArticles = newArticles;
        }
    }
}
