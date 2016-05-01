package fr.do_f.rssfeedify.api;

import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse.AddFeedPost;
import fr.do_f.rssfeedify.api.json.feeds.DeleteFeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.MarkAllFeedAsReadResponse;
import fr.do_f.rssfeedify.api.json.feeds.article.ReadArticleResponse;
import fr.do_f.rssfeedify.api.json.feeds.worker.WorkerResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse.LoginPost;
import fr.do_f.rssfeedify.api.json.login.LogoutResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse.RegisterPost;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.users.DeleteUserResponse;
import fr.do_f.rssfeedify.api.json.users.GetUserReponse;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse.*;
import fr.do_f.rssfeedify.api.json.users.UsersReponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by do_f on 03/04/16.
 */
public interface ApiService {

    @POST("register")
    Call<RegisterResponse>      register(@Body RegisterPost post);

    @POST("login")
    Call<LoginResponse>         login(@Body LoginPost post);

    @POST("logout")
    Call<LogoutResponse>        logout();

    @POST("feed")
    Call<AddFeedResponse>       addFeed(@Body AddFeedPost post);

    @DELETE("feed/{id}")
    Call<DeleteFeedResponse>    deleteFeed(@Path("id") int id);

    @POST("feed/as_read/{feedid}")
    Call<MarkAllFeedAsReadResponse> markAllFeedAsRead(@Path("feedid") int id);

    @GET("feeds")
    Call<GetFeedResponse>       getFeed();

    @GET("feeds/articles/{id}")
    Call<FeedResponse>          getAllFeed(@Path("id") int page);

    @GET("feed/{feedid}/articles/{page}")
    Call<FeedResponse>          getAllFeedById(@Path("feedid") int id, @Path("page") int page);

    // WORKER

    @GET("worker/refresh/feed/{feedid}")
    Call<WorkerResponse>        worker(@Path("feedid") int id);

    // USERS

    @GET("users")
    Call<UsersReponse>          getAllUser();

    @GET("user/{username}")
    Call<GetUserReponse>        getUser(@Path("username") String username);

    @DELETE("user/{username}")
    Call<DeleteUserResponse>    deleteUser(@Path("username") String username);

    @PUT("user/{username}")
    Call<UpdateUserResponse>    updateUser(@Path("username") String username, @Body User user);

    // ARTICLE

    @POST("article/as_read/{id}")
    Call<ReadArticleResponse>   readArticle(@Path("id") int id);
}
