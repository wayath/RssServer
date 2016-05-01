package fr.do_f.rssfeedify.login;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by do_f on 01/05/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterLoginEspressoTest {
    public static final String USERNAME = "espressod";
    public static final String PASSWORD = "tamerelapute";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void register_LoginActivity() {
        onView(ViewMatchers.withId(R.id.menu_register)).perform(click());

        onView(withId(R.id.register_username))
                .perform(typeText(USERNAME), closeSoftKeyboard());

        onView(withId(R.id.register_password))
                .perform(typeText(PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.register_submit)).perform(click());
    }

    @Test
    public void login_LoginActivity() {

        onView(withId(R.id.menu_login)).perform(click());

        onView(withId(R.id.login_username))
                .perform(typeText(USERNAME), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText(PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.login_submit)).perform(click());
    }
}
