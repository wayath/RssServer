package fr.do_f.rssfeedify.login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by do_f on 29/04/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginEspressoTest {

    public static final String USERNAME = "espresso";
    public static final String PASSWORD = "tamerelapute";


    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void login_LoginActivity() {

        onView(ViewMatchers.withId(R.id.menu_login)).perform(click());

        onView(withId(R.id.login_username))
                .perform(typeText(USERNAME), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText(PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.login_submit)).perform(click());
    }
}
