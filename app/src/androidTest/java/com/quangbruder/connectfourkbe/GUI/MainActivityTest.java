package com.quangbruder.connectfourkbe.GUI;

import android.Manifest;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.quangbruder.connectfourkbe.R;

import junit.framework.AssertionFailedError;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");


     @Test
     public void test1GetName(){
         try {
             onView(withId(R.id.editText_name)).perform(replaceText("Alice"), closeSoftKeyboard());
             onView(withText("OK")).perform(click());
             // View is displayed
         } catch (AssertionFailedError e) {
             // View not displayed
         }
     }

    // EXTRA_DISCOVERABLE_DURATION
    // MainActivity und alle Elemente, die
    //sie beinhaltet werden mit dem
    //richtigen Text angezeigt
    @Test
    public void testElementsAreDisplayed() {
        onView(ViewMatchers.withId(R.id.main_activity)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.list_devices)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.btn_scan)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_scan)).check(matches(withText("Scan")));
        onView(ViewMatchers.withId(R.id.btn_listen)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_listen)).check(matches(withText("Listen")));
        onView(ViewMatchers.withId(R.id.tv_status)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_status)).check(matches(withText("STATUS")));
    }


    @Test
    public void testScanButton(){
        onView(withId(R.id.btn_scan)).perform(click());
        onView(withId(R.id.tv_status)).check(matches(withText("Connecting: Scanning")));
        onView(ViewMatchers.withId(R.id.list_devices)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testListenButton(){
        onView(withId(R.id.btn_listen)).perform(click());
        onView(withId(R.id.tv_status)).check(matches(withText("Connecting: Listening")));
    }


}