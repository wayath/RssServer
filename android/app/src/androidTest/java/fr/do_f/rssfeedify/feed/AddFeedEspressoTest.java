package fr.do_f.rssfeedify.feed;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.main.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by do_f on 01/05/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddFeedEspressoTest {
    public static final String FEEDNAME = "Tuxboard";
    public static final String FEEDURL = "http://www.tuxboard.com/feed/";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void add_Feed() {
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.feed_add_name))
                .perform(typeText(FEEDNAME), closeSoftKeyboard());

        onView(withId(R.id.feed_add_url))
                .perform(typeText(FEEDURL), closeSoftKeyboard());

        onView(withId(R.id.feed_add_submit)).perform(click());
    }

    @Test
    public void openMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_settings)).perform(click());
        onView(withId(R.id.updateuser_delete)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }
}
