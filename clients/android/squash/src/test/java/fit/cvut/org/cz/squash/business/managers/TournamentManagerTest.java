package fit.cvut.org.cz.squash.business.managers;

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

import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 16.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TournamentManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String name = "Wimbledon";
    private static final String note = "All England Cup";
    private static long tournamentId;
    private static final long competitionId = 1;

    public static ITournamentManager tournamentManager = null;
    public static IPointConfigurationManager pointConfigurationManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        tournamentManager = ManagerFactory.getInstance(context).tournamentManager;
        pointConfigurationManager = ManagerFactory.getInstance(context).pointConfigManager;

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertEquals(sportContext, ManagerFactory.getInstance(context).sportDBHelper.getDBName());
        assertEquals(true, ManagerFactory.getInstance(context).sportDBHelper.isOpen());
        assertNotNull(tournamentManager);
        assertNotNull(pointConfigurationManager);
    }

    @Test
    public void procedure() {
        insert();
        getByCompetitionId();
        delete();
    }

    /**
     * Verify that when tournament is created, default point configuration is created.
     */
    private void insert() {
        Tournament inserted = new Tournament(0, competitionId, name, new Date(), new Date(), note);
        tournamentManager.insert(inserted);
        tournamentId = inserted.getId();
        Tournament tournament = tournamentManager.getById(tournamentId);
        assertEquals(competitionId, tournament.getCompetitionId());
        assertEquals(name, tournament.getName());
        assertEquals(note, tournament.getNote());

        PointConfiguration pointConfiguration = pointConfigurationManager.getById(tournamentId);
        assertEquals(pointConfiguration.getWin(), PointConfiguration.defaultConfig().getWin());
        assertEquals(pointConfiguration.getDraw(), PointConfiguration.defaultConfig().getDraw());
        assertEquals(pointConfiguration.getLoss(), PointConfiguration.defaultConfig().getLoss());
    }

    /**
     * Verify getByCompetitionId method returns correct tournament list.
     */
    private void getByCompetitionId() {
        List<Tournament> tournaments = tournamentManager.getByCompetitionId(competitionId);
        assertEquals(1, tournaments.size());
        Tournament tournament = tournaments.get(0);
        assertEquals(competitionId, tournament.getCompetitionId());
        assertEquals(name, tournament.getName());
        assertEquals(note, tournament.getNote());
    }

    /**
     * Verify that when tournament is deleted, related point configuration is also deleted.
     */
    private void delete() {
        tournamentManager.delete(tournamentId);
        Tournament tournament = tournamentManager.getById(tournamentId);
        assertNull(tournament);

        List<Tournament> tournaments = tournamentManager.getByCompetitionId(competitionId);
        assertNotNull(tournaments);
        assertEquals(0, tournaments.size());

        PointConfiguration pointConfiguration = pointConfigurationManager.getById(tournamentId);
        assertNull(pointConfiguration);
    }
}