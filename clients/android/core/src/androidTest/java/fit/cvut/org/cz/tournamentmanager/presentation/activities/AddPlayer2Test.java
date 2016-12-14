package fit.cvut.org.cz.tournamentmanager.presentation.activities;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fit.cvut.org.cz.tournamentmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddPlayer2Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addPlayer2Test() {
        ViewInteraction imageButton = onView(
                allOf(withContentDescription("drawer_open_action"),
                        withParent(withId(R.id.my_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Players"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        final int[] origin_size = {0};
        onView(withId(R.id.recycler_view)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "getting child count";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView rv = (RecyclerView)view;
                origin_size[0] = rv.getAdapter().getItemCount();
            }
        });
        int original_size = origin_size[0];

        onView(withId(R.id.recycler_view)).check(matches(withRecyclerViewSize(8)));

    }

    public static Matcher<View> getRecyclerViewSize(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int actualListSize = ((RecyclerView) view).getAdapter().getItemCount();
                Log.d("AddPlayer2Test", "RecyclerView actual size " + actualListSize);
                return actualListSize == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("RecyclerView should have " + size + " items");
            }
        };
    }

    public static Matcher<View> withRecyclerViewSize(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                Log.d("TEST", "Item count: "+((RecyclerView) view).getAdapter().getItemCount());
                final int actualListSize = ((RecyclerView) view).getAdapter().getItemCount();
                Log.d("AddPlayer2Test", "RecyclerView actual size " + actualListSize);
                return actualListSize == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("RecyclerView should have " + size + " items");
            }
        };
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
}
