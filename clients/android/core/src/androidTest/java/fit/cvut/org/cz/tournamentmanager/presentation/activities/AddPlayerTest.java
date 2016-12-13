package fit.cvut.org.cz.tournamentmanager.presentation.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fit.cvut.org.cz.tournamentmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddPlayerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addPlayerTest() {
        ViewInteraction imageButton = onView(
                allOf(withContentDescription("drawer_open_action"),
                        withParent(withId(R.id.my_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Players"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withClassName(is("android.support.design.widget.FloatingActionButton")), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.et_name),
                        withParent(withId(R.id.til_name))));
        textInputEditText.perform(scrollTo(), replaceText("Pl"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.et_email),
                        withParent(withId(R.id.til_email))));
        textInputEditText2.perform(scrollTo(), replaceText("emai"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_finish), withContentDescription("Finish"), isDisplayed()));
        actionMenuItemView.perform(click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_name), withText("Pl"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Pl")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tv_email), withText("emai"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("emai")));

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
