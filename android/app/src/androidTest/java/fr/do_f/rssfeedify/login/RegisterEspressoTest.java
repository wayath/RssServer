package fr.do_f.rssfeedify.login;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockApplication;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse.*;

import fr.do_f.rssfeedify.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by do_f on 30/04/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterEspressoTest {

    public static final String USERNAME = "espresso1sssss";
    public static final String PASSWORD = "tamerelapute";

    private List mockedList;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void setup() {
        //initMocks(this);

        //mockedList = mock(List.class);
    }

    @Test
    public void register_LoginActivity() {
//
//        RegisterPost test = mock(RegisterPost.class);
//        System.out.println("TAMERE : "+test.getUsername());
//
//        // mock creation
//        // using mock object - it does not throw any "unexpected interaction" exception
//        mockedList.add("one");
//        mockedList.clear();
//
//        // selective, explicit, highly readable verification
//        verify(mockedList).add("one");
//        verify(mockedList).clear();

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
