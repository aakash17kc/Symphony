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

public class SongActivityTest {
    @Rule
    public ActivityTestRule<SongActivity> songActivityActivityTestRule = new ActivityTestRule<>(SongActivity.class);
    private SongActivity songActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CurrentSongActivity.class.getName(),null,false);


    @Before
    public void setUp() throws Exception {
        songActivity = songActivityActivityTestRule.getActivity();
    }

    @Test
    public void currentSongActivityOpen(){
        assertNotNull(songActivity.findViewById(R.id.current_song_name));
        onView(withId(R.id.current_song_name)).perform(click());

        Activity songActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(songActivity);

        songActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        songActivity = null;
    }
}