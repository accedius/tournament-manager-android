package fit.cvut.org.cz.tournamentmanager.presentation.activities;


import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fit.cvut.org.cz.tournamentmanager.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SettingsTest {
    private static Resources resources = InstrumentationRegistry.getTargetContext().getResources();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void saveSettings() {
        CommonFunctions.navigateToSettings();

        CommonFunctions.saveSetting();
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(resources.getString(R.string.settings_saved))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view)).check(matches(CommonFunctions.withRecyclerViewSize(0)));

    }
}
