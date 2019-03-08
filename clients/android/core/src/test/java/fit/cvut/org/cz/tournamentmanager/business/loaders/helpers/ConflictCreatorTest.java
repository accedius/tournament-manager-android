package fit.cvut.org.cz.tournamentmanager.business.loaders.helpers;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.helpers.ConflictCreator;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 5.1.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConflictCreatorTest extends AndroidTestCase {
    ConflictCreator conflictCreator = null;
    Context context = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        conflictCreator = new ConflictCreator();

        assertNotNull(context);
        assertNotNull(conflictCreator);
    }

    @Test
    public void create() {
        String firstName = "First name";
        String secondName = "Second name";
        String firstNote = "First note";
        String secondNote = "Second note";
        String email = "email";

        Conflict conflict = conflictCreator.createConflict(
                new Player(1, firstName, email, firstNote),
                new Player(2, secondName, email, secondNote),
                context.getResources());

        assertNotNull(conflict);
        assertEquals(email, conflict.getTitle());
        assertEquals(2, conflict.getValues().size());

        assertEquals(context.getResources().getString(R.string.name), conflict.getValues().get(0).getAttribute());
        assertEquals(firstName, conflict.getValues().get(0).getLeftValue());
        assertEquals(secondName, conflict.getValues().get(0).getRightValue());

        assertEquals(context.getResources().getString(R.string.note), conflict.getValues().get(1).getAttribute());
        assertEquals(firstNote, conflict.getValues().get(1).getLeftValue());
        assertEquals(secondNote, conflict.getValues().get(1).getRightValue());
    }
}
