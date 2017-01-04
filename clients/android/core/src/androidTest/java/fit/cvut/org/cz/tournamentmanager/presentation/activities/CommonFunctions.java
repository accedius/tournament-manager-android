package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;

import fit.cvut.org.cz.tournamentmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by kevin on 3.1.2017.
 */

public class CommonFunctions {
    private static Resources resources = InstrumentationRegistry.getTargetContext().getResources();

    public static void navigateUp() {
        ViewInteraction imageButton4 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.my_toolbar)),
                        isDisplayed()));
        imageButton4.perform(click());
    }

    public static void navigateToPlayersSort() {
        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.action_players_order),
                        isDisplayed()));
        imageButton4.perform(click());
    }

    public static void sortPlayersByName() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText(R.string.sort_by_name),
                        isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(2000);
    }

    public static void sortPlayersByEmail() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText(R.string.sort_by_email),
                        isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(2000);
    }

    public static void navigateToSettings() {
        ViewInteraction imageButton = onView(
                allOf(withContentDescription(R.string.openDrawer),
                        withParent(withId(R.id.my_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText(R.string.settings), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    public static void navigateToPlayers() {
        ViewInteraction imageButton = onView(
                allOf(withContentDescription(R.string.openDrawer),
                        withParent(withId(R.id.my_toolbar)),
                        isDisplayed()));
        imageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText(R.string.players), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    public static void navigateToPlayerDetailAtPosition(int position) {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(position, click()));
    }

    public static void deletePlayerAtPosition(int position) {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(position, longClick()));

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText(R.string.delete),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());
        SystemClock.sleep(2000);
    }

    public static void navigateToPlayerCreate() {
        navigateToPlayers();
        ViewInteraction floatingActionButton = onView(
                allOf(withClassName(is("android.support.design.widget.FloatingActionButton")), isDisplayed()));
        floatingActionButton.perform(click());
    }

    public static void deleteAllPlayers() {
        navigateToPlayers();
        int playersCount = getRecyclerViewItemsCount();
        for (int i=0; i<playersCount; i++) {
            deletePlayerAtPosition(0);
        }
    }

    public static void savePlayer() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_finish), withContentDescription(resources.getString(R.string.finish)), isDisplayed()));
        actionMenuItemView.perform(click());
    }

    public static void saveSetting() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_finish), withContentDescription(resources.getString(R.string.finish)), isDisplayed()));
        actionMenuItemView.perform(click());
    }

    public static void fillPlayerInfo(String name, String email, String note) {
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.et_name),
                        withParent(withId(R.id.til_name))));
        textInputEditText.perform(scrollTo(), replaceText(name), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.et_email),
                        withParent(withId(R.id.til_email))));
        textInputEditText2.perform(scrollTo(), replaceText(email), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.et_note),
                        withParent(withId(R.id.til_note))));
        textInputEditText3.perform(scrollTo(), replaceText(note), closeSoftKeyboard());
    }

    public static void verifyPlayerItemAtPosition(int position, String name, String email) {
        ViewInteraction textView = onView(
                childAtPosition(
                allOf(childAtPosition(
                    allOf(
                        childAtPosition(withId(R.id.recycler_view), position),
                        isDisplayed()),
                    0)),
                0));
        textView.check(matches(withText(name)));

        textView = onView(
            childAtPosition(
                allOf(childAtPosition(
                    allOf(
                        childAtPosition(withId(R.id.recycler_view), position),
                        isDisplayed()),
                    0)),
                1));
        textView.check(matches(withText(email)));
    }

    public static void verifyPlayerDetail(String name, String email, String note) {
        ViewInteraction textView = onView(
                allOf(withText(resources.getString(R.string.player)+" – "+name),
                        childAtPosition(
                                allOf(withId(R.id.my_toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText(resources.getString(R.string.player)+" – "+name)));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.player_email), withText(email),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText(email)));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.player_note), withText(note),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText(note)));
    }

    public static Matcher<View> childAtPosition(
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

    public static Matcher<View> withRecyclerViewSize(final int size) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int actualListSize = ((RecyclerView) view).getAdapter().getItemCount();
                return actualListSize == size;
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }

    public static int getRecyclerViewItemsCount() {
        final int[] origin_size = {0};
        onView(withId(R.id.recycler_view)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Getting child count.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView rv = (RecyclerView) view;
                origin_size[0] = rv.getAdapter().getItemCount();
            }
        });
        return origin_size[0];
    }
}
