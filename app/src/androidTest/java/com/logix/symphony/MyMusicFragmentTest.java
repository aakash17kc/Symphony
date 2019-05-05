package com.logix.symphony;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.RelativeLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MyMusicFragmentTest {

    @Rule
    public ActivityTestRule<TestActivity> testActivityActivityTestRule = new ActivityTestRule<>(TestActivity.class);

    private TestActivity testActivity = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SignUpActivity.class.getName(),null,false);


    @Before
    public void setUp() throws Exception {
        testActivity = testActivityActivityTestRule.getActivity();

    }
    @Test
    public void launchFragment(){
        RelativeLayout container = testActivity.findViewById(R.id.test_container);
        assertNotNull(container);
        MyMusicFragment myMusicFragment = new MyMusicFragment();

        testActivity.getSupportFragmentManager().beginTransaction().add(container.getId(), myMusicFragment).commit();
        getInstrumentation().waitForIdleSync();
        Activity songActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);


        View view =  myMusicFragment.getView().findViewById(R.id.logout);
      onView(withId(R.id.logout)).perform(click());

        assertNotNull(view);
      //  assertNotNull(songActivity);

    //    songActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        testActivity = null;
    }
}