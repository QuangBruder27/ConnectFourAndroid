package com.quangbruder.connectfourkbe;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.quangbruder.connectfourkbe.GUI.BoardColumn.BoardColumnsAdapter;
import com.quangbruder.connectfourkbe.GUI.GameActivity;
import com.quangbruder.connectfourkbe.GUI.GameActivityTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import GameData.Board;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameControllerTest {

    @Rule
    public ActivityTestRule<GameActivity> activityRule = new ActivityTestRule<>(GameActivity.class);

    GameController gameController;

    @Before
    public void setUp(){
        gameController = activityRule.getActivity().gameController;
    }


    @Test
    public void drawCircleTest() throws Throwable {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameController.drawCircle(Board.Player.ONE,(BoardColumnsAdapter.ViewHolder) activityRule.getActivity().recyclerView.findViewHolderForAdapterPosition(0),0);
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(
                allOf(withId(R.id.imgView5),
                        withContentDescription("C"+0)))
                .check(matches(new DrawableMatcher(R.drawable.yellow)));
   }

    @Test
    public void statusNotiTest() throws Throwable {
        gameController.gameboard.setMovesCounter(42);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameController.statusNoti(gameController.gameboard, (BoardColumnsAdapter.ViewHolder) activityRule.getActivity().recyclerView.findViewHolderForAdapterPosition(0));
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.btn_replay)).check(matches(isDisplayed()));
    }


    @Test
    public void setUIforPlayerTest() throws Throwable {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameController.device_role = 1;
                gameController.setUIforPlayer(); }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(
                allOf(withId(R.id.imgView_player)))
                .check(matches(new DrawableMatcher(R.drawable.red)));
        onView(ViewMatchers.withId(R.id.tv_player))
                .check(matches(withText(containsString("You are server"))));
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