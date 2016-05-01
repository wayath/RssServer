/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.User;

import Model.Feeds.Feed;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bastien
 */
public class UserModel {
    public static UserModel Instance;
    String token;
    String login;
    Boolean isAdmin;
    List<Feed> userFeeds;

    public UserModel() {
        Instance = this;
        isAdmin = false;
        userFeeds = new ArrayList<>();
    }
    
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
    
    public List<Feed> getUserFeeds() {
        return userFeeds;
    }

    public void setUserFeeds(List<Feed> userFeeds) {
        this.userFeeds = userFeeds;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return this.token;
    }
}
