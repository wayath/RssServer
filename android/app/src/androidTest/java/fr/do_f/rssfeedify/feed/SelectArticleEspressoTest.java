package fr.do_f.rssfeedify.feed;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by do_f on 01/05/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectArticleEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void scroll() {
        onView(withId(R.id.rvFeedFragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));

        onView(withContentDescription("Navigate up")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.rvFeed))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
    }
}
