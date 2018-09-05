package edu.gatech.cs2340.testingdimockitoexample;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.cs2340.testingdimockitoexample.dummy.DummyContent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.gatech.cs2340.testingdimockitoexample", appContext.getPackageName());

    }

    @Test
    public void checkNotificationButtonPress() {
        onView(withId(R.id.navigation_dashboard)).perform(click());
        assertEquals("initial count incorrect", 25, DummyContent.COUNT);
        onView(withId(R.id.add_item_button)).perform(click());
        assertEquals("count after add wrong", 26, mActivityRule.getActivity().dataSource.getItemCount());
    }



}
