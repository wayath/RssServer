import Controller.FeedController;
import Controller.LoginController;
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
public class LoginTests {
    LoginController logVC;
    
    @Before
    public void CreateLoginController() {
        Window win = new Window();
        win.setVisible(true);
        try {
            this.logVC = new LoginController(win);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testLoginViewCreation() throws Exception {
        Window win = new Window();
        win.setVisible(true);
        try {
            LoginController logVC = new LoginController(win);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testRegister() throws Exception {
        logVC.applyRegister("TESTUNIT", "TESTUNIT");
    }
    
    @Test
    public void testBadLogin() throws Exception {
        logVC.applyLogin("TESTUNIT", "wrongPassword");
    }
    
    @Test
    public void testGoodLogin() throws Exception {
        logVC.applyLogin("TESTUNIT", "TESTUNIT");
    }
}
