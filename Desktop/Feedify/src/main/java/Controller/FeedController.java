/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import API.RestClient;
import Model.Articles.AllArticlesModel;
import Model.Feeds.DeleteFeedResponse;
import Model.Feeds.GetFeedsResponse;
import Model.User.UserModel;
import Model.Feeds.Feed;
import Model.Feeds.Feed.FeedPost;
import Model.Articles.GetArticleResponse;
import Model.User.LogoutResponse;
import Model.Articles.ReadArticleResponse;
import Model.Feeds.RefreshFeedResponse;
import Model.User.UpdateUserResponse;
import Model.User.UpdateUserResponse.User;
import Model.User.UsersResponse;
import View.FeedView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author Bastien
 */
public  class FeedController implements FeedView.OnFeedViewEventRaised {
    FeedView feedView;
    JFrame window;
    Boolean DoNotReloadFeed = false;
    
    public FeedController(JFrame win) {
        new AllArticlesModel();
        feedView = new FeedView();
        feedView.setOnFeedViewEventRaised(this);
        win.setContentPane(feedView);
        win.getContentPane().repaint();
        win.getContentPane().revalidate();
        window = win;
        getFeedForDisplay();
    }
    
    void getFeedForDisplay() {
        Call<GetFeedsResponse> call = RestClient.get(UserModel.Instance.getToken()).feeds();
        call.enqueue(new Callback<GetFeedsResponse>() {

            @Override
            public void onResponse(Call<GetFeedsResponse> call, Response<GetFeedsResponse> response) {
                if (response.body() != null) {
                    List<Feed> feedList = response.body().getFeeds();
                    if (feedList.size() != 0) {
                        UserModel.Instance.getUserFeeds().clear();
                        for (Feed f : feedList) {
                            UserModel.Instance.getUserFeeds().add(f);
                         }
                        System.out.println("TEST");
                        if (!DoNotReloadFeed) {
                            getAllFeed(-1, 1);
                        } else {
                            DoNotReloadFeed = false;
                        }
                    }
                    feedView.reloadFeed();
                } else {
                    System.out.println("GetFeed " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetFeedsResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    @Override
    public void getAllFeed(int id, int page) {
        Call<GetArticleResponse> call;
        if (id == -1) {
            call = RestClient.get(UserModel.Instance.getToken()).getAllFeed(page);
        } else {
            call = RestClient.get(UserModel.Instance.getToken()).getAllFeedById(id, page);
        }
        
        call.enqueue(new Callback<GetArticleResponse>() {
            @Override
            public void onResponse(Call<GetArticleResponse> call, Response<GetArticleResponse> response) {
                if (response.body() != null) {
                    if (!response.body().getArticles().isEmpty()) {
                        AllArticlesModel.Instance.setAllArticles(response.body().getArticles());
                        feedView.setCurrentPage(page);
                    }
                    feedView.RefreshAndDumpArticles();
                }
            }

            @Override
            public void onFailure(Call<GetArticleResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        } );

    }
    
    @Override
    public void OnAddFeedComplete(String name, String URL) {
        FeedPost p = new FeedPost(name, URL);
        
        Call<Feed> call = RestClient.get(UserModel.Instance.getToken()).addFeed(p);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if (response.body() != null) {
                    System.out.println(response.code());
                    RefreshFeed(response.body());
                   /* if (!UserModel.Instance.getUserFeeds().contains(response.body())) {
                        UserModel.Instance.getUserFeeds().add(response.body());
                        feedView.addThisFeedToList(response.body().getName());
                    }*/
                } else {
                    System.out.println(response.code() + " CODE");
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                System.out.println("failed");
            }
        });
    }

    void RefreshFeed(Feed feed) {
        Call<RefreshFeedResponse> call = RestClient.get(UserModel.Instance.getToken()).refreshFeed(feed.getId());
        call.enqueue(new Callback<RefreshFeedResponse>() {
            @Override
            public void onResponse(Call<RefreshFeedResponse> call, Response<RefreshFeedResponse> response) {
                if (response.body() != null) {
                     if (!UserModel.Instance.getUserFeeds().contains(response.body())) {
                        UserModel.Instance.getUserFeeds().add(feed);
                        feedView.addThisFeedToList(feed.getName());
                    }
                }
                else {
                    DeleteFeed(feed.getId());
                    JOptionPane.showMessageDialog(null, "The feed : "
                            + feed.getUrl()
                            + " you tried to enter is invalid", "Add feed", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<RefreshFeedResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
    }
    
    @Override
    public void logout() {
        Call<LogoutResponse> call = RestClient.get(UserModel.Instance.getToken()).logout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.body() != null) {
                    feedView = null;
                    try {
                        new LoginController(window);
                    } catch (IOException ex) {
                        Logger.getLogger(FeedController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    @Override
    public void getAllUsers() {
        Call<UsersResponse> call = RestClient.get(UserModel.Instance.getToken()).getAllUser();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.body() != null) {
                    feedView.DisplayUserList(response.body().getUsers());
                }
            } 

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
             
        });
    }

    @Override
    public void UpdateUserInfos(String username, String Password, Boolean typeB) {
        String type = typeB ? "admin" : "user";
        User u = new User(username, Password, type);
        Call<UpdateUserResponse> call = RestClient.get(UserModel.Instance.getToken()).updateUser(username, u);
        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.body() != null) {
                    JOptionPane.showMessageDialog(null, "User informations have been updated", "Update user Infos", JOptionPane.INFORMATION_MESSAGE);
                    getAllUsers();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
            }
        });
        
    }

    @Override
    public void DeleteFeed(int feedid) {
        Call<DeleteFeedResponse> call = RestClient.get(UserModel.Instance.getToken()).deleteFeed(feedid);
        call.enqueue(new Callback<DeleteFeedResponse>(){
            @Override
            public void onResponse(Call<DeleteFeedResponse> call, Response<DeleteFeedResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < UserModel.Instance.getUserFeeds().size(); i++) {
                        if (UserModel.Instance.getUserFeeds().get(i).getId() == feedid) {
                            UserModel.Instance.getUserFeeds().remove(UserModel.Instance.getUserFeeds().get(i));
                        }
                    }
                    getFeedForDisplay();
                }  else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<DeleteFeedResponse> call, Throwable t) {
                
            }
            
        });
    }

    @Override
    public void MarkArticleAsRead(int articleid) {
        Call<ReadArticleResponse> call = RestClient.get(UserModel.Instance.getToken()).readArticle(articleid);
        call.enqueue(new Callback<ReadArticleResponse>() {
            @Override
            public void onResponse(Call<ReadArticleResponse> call, Response<ReadArticleResponse> response) {
                if (response.body() != null) {
                    System.out.println(response.body().getMessage());
                    DoNotReloadFeed = true;
                    getFeedForDisplay();
                } else {
                    System.out.println(response.code() + "    " + articleid);
                }
            }

            @Override
            public void onFailure(Call<ReadArticleResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
    }
}
