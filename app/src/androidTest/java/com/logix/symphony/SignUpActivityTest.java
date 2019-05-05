package com.logix.symphony;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    private SignUpActivity signUpActivity = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SongActivity.class.getName(),null,false);


    @Before
    public void setUp() throws Exception {
        signUpActivity = signUpActivityActivityTestRule.getActivity();
    }

    @Test
    public void googgleSignIn(){
        assertNotNull(signUpActivity.findViewById(R.id.google));
        onView(withId(R.id.google)).perform(click());

        Activity songActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(songActivity);

        songActivity.finish();

    }

    @After
    public void tearDown() throws Exception {
        signUpActivity = null;
    }
}