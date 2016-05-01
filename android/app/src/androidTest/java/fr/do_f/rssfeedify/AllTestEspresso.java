package fr.do_f.rssfeedify;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.do_f.rssfeedify.api.json.login.RegisterResponse.*;
import fr.do_f.rssfeedify.login.LoginActivity;
import fr.do_f.rssfeedify.main.MainActivity;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by do_f on 01/05/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AllTestEspresso {
    public static final String USERNAME = "espressod";
    public static final String PASSWORD = "tamerelapute";
    public static final String FEEDNAME = "Tuxboard";
    public static final String FEEDURL = "http://www.tuxboard.com/feed/";

    public RegisterPost p;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void setUp() {
        p = Mockito.mock(RegisterPost.class);

        Mockito.when(p.getUsername()).thenReturn(USERNAME);
        Mockito.when(p.getPassword()).thenReturn(PASSWORD);
    }

    @Test
    public void aRegisterLoginActivity() {
        onView(ViewMatchers.withId(R.id.menu_register)).perform(click());

        onView(withId(R.id.register_username))
                .perform(typeText(p.getUsername()), closeSoftKeyboard());

        onView(withId(R.id.register_password))
                .perform(typeText(p.getPassword()), closeSoftKeyboard());

        onView(withId(R.id.register_submit)).perform(click());
    }

    @Test
    public void bLoginLoginActivity() {

        onView(withId(R.id.menu_login)).perform(click());

        onView(withId(R.id.login_username))
                .perform(typeText(p.getUsername()), closeSoftKeyboard());

        onView(withId(R.id.login_password))
                .perform(typeText(p.getPassword()), closeSoftKeyboard());

        onView(withId(R.id.login_submit)).perform(click());
    }

    @Test
    public void cAddMainActivity() {
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.feed_add_name))
                .perform(typeText(FEEDNAME), closeSoftKeyboard());

        onView(withId(R.id.feed_add_url))
                .perform(typeText(FEEDURL), closeSoftKeyboard());

        onView(withId(R.id.feed_add_submit)).perform(click());
    }

    @Test
    public void dScroll() {
        onView(withId(R.id.rvFeedFragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));

        onView(withContentDescription("Navigate up")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.rvFeed))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void eDeleteMainActivity() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withId(R.id.updateuser_delete)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }
}
