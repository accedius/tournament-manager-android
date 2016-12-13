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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SquashCompetition {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void squashCompetition() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_order), withContentDescription("Seřadit"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatTextView27 = onView(
                allOf(withId(android.R.id.text1), withText("Podle názvu"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView27.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_order), withContentDescription("Seřadit"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatTextView54 = onView(
                allOf(withId(android.R.id.text1), withText("Podle názvu"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView54.perform(click());

        ViewInteraction competitionFirst = onView(
                allOf(withId(R.id.tv_name), withText("SQ T CMP 1"), isDisplayed())
        );
        competitionFirst.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_name), withText("Martin"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Martin")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tv_name), withText("Radek"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Radek")));

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
