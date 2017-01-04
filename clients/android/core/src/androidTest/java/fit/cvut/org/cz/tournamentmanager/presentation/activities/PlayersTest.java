package fit.cvut.org.cz.tournamentmanager.presentation.activities;


import android.content.res.Resources;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fit.cvut.org.cz.tournamentmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlayersTest {

    private static final String name = "Martin";
    private static final String email = "Martin@email.com";
    private static final String note = "Not about martin";

    private static Resources resources = InstrumentationRegistry.getTargetContext().getResources();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void before() {
        CommonFunctions.deleteAllPlayers();
    }

    @Test
    public void playersOrder() {
        String name2 = "David";
        String email2 = "ZzzDavid@email.com";
        String note2 = "Not about david.";
        CommonFunctions.navigateToPlayerCreate();

        CommonFunctions.fillPlayerInfo(name, email, note);
        CommonFunctions.savePlayer();

        CommonFunctions.navigateToPlayerCreate();

        CommonFunctions.fillPlayerInfo(name2, email2, note2);
        CommonFunctions.savePlayer();

        CommonFunctions.navigateToPlayersSort();
        /* Order players descendant by name */
        CommonFunctions.sortPlayersByName();

        CommonFunctions.verifyPlayerItemAtPosition(0, name, email);
        CommonFunctions.verifyPlayerItemAtPosition(1, name2, email2);

        CommonFunctions.navigateToPlayersSort();
        /* Order players ascendant by name */
        CommonFunctions.sortPlayersByName();

        CommonFunctions.verifyPlayerItemAtPosition(0, name2, email2);
        CommonFunctions.verifyPlayerItemAtPosition(1, name, email);

        CommonFunctions.navigateToPlayersSort();
        /* Order players ascendant by email */
        CommonFunctions.sortPlayersByEmail();

        CommonFunctions.verifyPlayerItemAtPosition(0, name, email);
        CommonFunctions.verifyPlayerItemAtPosition(1, name2, email2);

        CommonFunctions.navigateToPlayersSort();
        /* Order players descendant by email */
        CommonFunctions.sortPlayersByEmail();

        CommonFunctions.verifyPlayerItemAtPosition(0, name2, email2);
        CommonFunctions.verifyPlayerItemAtPosition(1, name, email);
    }

    @Test
    public void addNotUniqueEmail() {
        CommonFunctions.navigateToPlayerCreate();

        CommonFunctions.fillPlayerInfo(name, email, note);
        CommonFunctions.savePlayer();

        CommonFunctions.navigateToPlayerCreate();

        CommonFunctions.fillPlayerInfo(name, email, note);
        CommonFunctions.savePlayer();
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(resources.getString(R.string.email_exists_error))))
                .check(matches(isDisplayed()));

        CommonFunctions.navigateUp();
    }

    @Test
    public void addAndDeletePlayer() {
        CommonFunctions.navigateToPlayerCreate();

        CommonFunctions.fillPlayerInfo(name, email, note);
        CommonFunctions.savePlayer();

        CommonFunctions.verifyPlayerItemAtPosition(0, name, email);
        /* Navigate to detail of first player. */
        CommonFunctions.navigateToPlayerDetailAtPosition(0);

        CommonFunctions.verifyPlayerDetail(name, email, note);
        CommonFunctions.navigateUp();

        CommonFunctions.deletePlayerAtPosition(0);
        onView(withId(R.id.recycler_view)).check(matches(CommonFunctions.withRecyclerViewSize(0)));
    }

    @After
    public void after() {
        try {
            while (true) {
                CommonFunctions.navigateUp();
            }
        } catch (NoMatchingViewException e) {}
        CommonFunctions.deleteAllPlayers();
    }
}
