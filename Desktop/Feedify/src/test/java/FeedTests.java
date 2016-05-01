import Controller.FeedController;
import Controller.LoginController;
import Model.User.UserModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bastien
 */
public class FeedTests {
    LoginController logVC;
    FeedController feedVC;
    
    @Before
    public void CreateFeedController() {
        Window win = new Window();
        win.setVisible(true);
        try {
            this.logVC = new LoginController(win);
            this.logVC.applyLogin("TESTUNIT", "TESTUNIT");
            this.feedVC = new FeedController(win);

        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void addWrongFeed() {
        this.feedVC.OnAddFeedComplete("test", "test");
    }
    
    @Test
    public void addGoodFeed() {
        this.feedVC.OnAddFeedComplete("Tuxboard", "http://www.tuxboard.com/feed/");
    }
    
    @Test
    public void removeWrongFeed() {
        this.feedVC.DeleteFeed(-1);
    }
    
    @Test
    public void removeGoodFeed() {
        this.feedVC.DeleteFeed(1);
    }
    
    @Test
    public void updateWrongUser() {
        this.feedVC.UpdateUserInfos("UNKNOWN", "UNKNOWN", Boolean.TRUE);
    }
    
    @Test
    public void updateGoodUser() {
        this.feedVC.UpdateUserInfos(UserModel.Instance.getLogin(), "NEWPASSWORD", Boolean.TRUE);
    }
    
    @Test
    public void updateUserIfNoAdmin() {
        this.feedVC.UpdateUserInfos(UserModel.Instance.getLogin(), "NEWPASSWORD", Boolean.FALSE);
    }
    
    @Test
    public void getAllUsers() {
        this.feedVC.getAllUsers();
    }
    
    @Test
    public void logout() {
        this.feedVC.logout();
    }
}
