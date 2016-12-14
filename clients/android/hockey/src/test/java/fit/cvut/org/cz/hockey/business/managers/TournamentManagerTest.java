package fit.cvut.org.cz.hockey.business.managers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TournamentManagerTest {
    @Mock
    TournamentManager tournamentManager;

    @Before
    public void setUp() throws Exception {
        HockeyDBHelper hockeyDBHelper = new HockeyDBHelper(RuntimeEnvironment.application, "testDb");
        tournamentManager = new TournamentManager(hockeyDBHelper);
        tournamentManager = spy(tournamentManager);
        Tournament t = new Tournament();
        t.setId(1);
        when(tournamentManager.getDao(RuntimeEnvironment.application)).thenReturn(hockeyDBHelper.getTournamentDAO());
    }

    @Test
    public void testInsert() throws Exception {
        Tournament t = new Tournament();
        tournamentManager.insert(RuntimeEnvironment.application, t);
        assertTrue(t.getId() > 0);
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testGetById() throws Exception {
    }

    @Test
    public void testGetByCompId() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}