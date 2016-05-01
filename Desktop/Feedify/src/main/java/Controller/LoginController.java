/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.User.RegisterResponse;
import Model.User.RegisterResponse.RegisterPost;
import API.RestClient;
import Model.User.GetUserResponse;
import Model.User.LoginResponse;
import Model.User.LoginResponse.LoginPost;
import Model.User.UserModel;
import View.LoginView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
public class LoginController implements LoginView.OnConnectButtonClick {
    JFrame window;
    
    public LoginController(JFrame win) throws IOException {
        UserModel user = new UserModel();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(".log.txt"));
            
            String line;
            Boolean isLogin = true;
            while ((line = in.readLine()) != null) {
                if (isLogin) {
                    user.Instance.setLogin(line);
                    isLogin = false;
                } else {
                    user.Instance.setToken(line);
                }
            }
            in.close();
            getThisUser(user.Instance.getLogin());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LoginView logView = new LoginView();
        logView.setOnConnectButtonClick(this);
        win.setContentPane(logView);
        win.getContentPane().revalidate();
        win.getContentPane().repaint();
        window = win;
    }
    
    
    //CallBack Method
    @Override
    public void applyRegister(String login, String Pwd) {
        RegisterPost p = new RegisterPost(login, Pwd);
        
        Call<RegisterResponse> call = RestClient.get().register(p);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body() != null) {
                    JOptionPane.showMessageDialog(null, "You account has been created", "Register", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "This account already exist or you entered wrong parameters", "Register", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                JOptionPane.showMessageDialog(null, "Register request failed", "Register", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    

    @Override
    public void applyLogin(String login, String pwd) {
        LoginPost p = new LoginPost(login, pwd);
        
        Call<LoginResponse> call = RestClient.get().login(p);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    UserModel.Instance.setToken(response.body().getToken());
                    UserModel.Instance.setLogin(login);
                    try {
                        PrintWriter writer;
                        writer = new PrintWriter(".log.txt", "UTF-8");
                        writer.println(UserModel.Instance.getLogin());
                        writer.println(UserModel.Instance.getToken());
                        writer.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    getThisUser(login);
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong login or password", "Connexion", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                JOptionPane.showMessageDialog(null, "Login request failed", "Connexion", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    void getThisUser(String userName) {
        Call<GetUserResponse> call = RestClient.get(UserModel.Instance.getToken()).getUser(userName);
        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.body() != null) {
                    if (response.body().getType().equals("admin")) {
                        UserModel.Instance.setIsAdmin(true);
                    } else {
                        UserModel.Instance.setIsAdmin(false);
                    }
                    FeedController FeedVC = new FeedController(window);
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
}
