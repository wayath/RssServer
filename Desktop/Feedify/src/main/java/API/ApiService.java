/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import Model.Feeds.DeleteFeedResponse;
import Model.Feeds.Feed;
import Model.Feeds.Feed.FeedPost;
import Model.Articles.GetArticleResponse;
import Model.Feeds.GetFeedsResponse;
import Model.User.GetUserResponse;
import Model.User.LoginResponse;
import Model.User.LoginResponse.LoginPost;
import Model.User.LogoutResponse;
import Model.Articles.ReadArticleResponse;
import Model.Feeds.RefreshFeedResponse;
import Model.User.RegisterResponse;
import Model.User.RegisterResponse.RegisterPost;
import Model.User.UpdateUserResponse;
import Model.User.UpdateUserResponse.User;
import Model.User.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 *
 * @author Bastien
 */
public interface ApiService {
    //login - logout - register
    @POST("register")
    Call<RegisterResponse>      register(@Body RegisterPost rPost);
    
    @POST("login")
    Call<LoginResponse>     login(@Body LoginPost post);
    
    @POST("logout")
    Call<LogoutResponse>    logout();
    
    //Feeds and articles
    
    @GET("feeds")
    Call<GetFeedsResponse>  feeds();
    
    @POST("feed")
    Call<Feed>  addFeed(@Body FeedPost post);
    
    @DELETE("feed/{id}")
    Call<DeleteFeedResponse>    deleteFeed(@Path("id") int id);
    
    @GET("feeds/articles/{id}")
    Call<GetArticleResponse>      getAllFeed(@Path("id") int page);

    @GET("feed/{feedid}/articles/{page}")
    Call<GetArticleResponse>      getAllFeedById(@Path("feedid") int id, @Path("page") int page);
    
    @POST("article/as_read/{id}")
    Call<ReadArticleResponse>       readArticle(@Path("id") int id);
    
    @GET("worker/refresh/feed/{id}")
    Call<RefreshFeedResponse>       refreshFeed(@Path("id") int id);
    
    //Users
    
    @GET("user/{username}")
    Call<GetUserResponse>        getUser(@Path("username") String username);
    
    @PUT("user/{username}")
    Call<UpdateUserResponse>    updateUser(@Path("username") String username, @Body User user);
    
    @GET("users")
    Call<UsersResponse>          getAllUser();

}
