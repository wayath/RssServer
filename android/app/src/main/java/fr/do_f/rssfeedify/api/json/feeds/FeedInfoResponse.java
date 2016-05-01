package fr.do_f.rssfeedify.api.json.feeds;

import com.google.gson.annotations.SerializedName;

/**
 * Created by do_f on 20/04/16.
 */
public class FeedInfoResponse {

    private int id;
    private String name;
    private String url;

    @SerializedName("new_articles")
    private int newArticles;

    @SerializedName("refresh_error")
    private boolean refreshError;

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

    public int getNewArticles() {
        return newArticles;
    }

    public void setNewArticles(int newArticles) {
        this.newArticles = newArticles;
    }

    public boolean isRefreshError() {
        return refreshError;
    }

    public void setRefreshError(boolean refreshError) {
        this.refreshError = refreshError;
    }
}
