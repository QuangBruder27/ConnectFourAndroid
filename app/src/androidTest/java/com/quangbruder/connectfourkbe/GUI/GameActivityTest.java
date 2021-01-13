package com.quangbruder.connectfourkbe.GUI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import GameData.Board;
import GameData.BoardImpl;
import GameLogic.Logic;
import GameLogic.LogicImpl;

import com.quangbruder.connectfourkbe.GameController;
import com.quangbruder.connectfourkbe.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.quangbruder.connectfourkbe.GUI.GameActivity.btn_replay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> activityRule = new ActivityTestRule<>(GameActivity.class);

    @Test
    public void testElementsAreDisplayed() {
        onView(ViewMatchers.withId(R.id.game_activity)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rvBoardColumn)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.imgView_player)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.tv_nameOfPlayer)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.tv_player)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.btn_replay)).check(matches(not(isDisplayed())));
    }


    @Test
    public void testBoardColum4Click() {
        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.rvBoardColumn),
                        childAtPosition(
                                withId(R.id.game_activity),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(3, click()));
    }

    @Test
    public void testReplayButton(){
        onView(ViewMatchers.withId(R.id.btn_replay)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testImageViewColumn4Row1(){
        onView(
                allOf(withId(R.id.imgView),
                        withContentDescription("C"+3)))
                .check(matches(new DrawableMatcher(R.drawable.white)));
    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public class DrawableMatcher extends TypeSafeMatcher<View> {

        private final int expectedId;
        String resourceName;

        public DrawableMatcher(int expectedId) {
            super(View.class);
            this.expectedId = expectedId;
        }

        @Override
        protected boolean matchesSafely(View target) {
            if (!(target instanceof ImageView)) {
                return false;
            }
            ImageView imageView = (ImageView) target;
            if (expectedId < 0) {
                return imageView.getDrawable() == null;
            }
            Resources resources = target.getContext().getResources();
            Drawable expectedDrawable = resources.getDrawable(expectedId);
            resourceName = resources.getResourceEntryName(expectedId);

            if (expectedDrawable == null) {
                return false;
            }

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap otherBitmap = ((BitmapDrawable) expectedDrawable).getBitmap();
            return bitmap.sameAs(otherBitmap);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with drawable from resource id: ");
            description.appendValue(expectedId);
            if (resourceName != null) {
                description.appendText("[");
                description.appendText(resourceName);
                description.appendText("]");
            }
        }
    }

}