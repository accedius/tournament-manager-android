package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 16.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CompetitionManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    public static final String name = "Fed Cup";
    private static final String note = "By BNP Paribas";
    private static long competitionId;
    private static Competition inserted;

    public static ICompetitionManager competitionManager = null;
    public static ITournamentManager tournamentManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify match insertion.
     */
    @Test
    public void insert() {
        add();
    }

    /**
     * Verify get Tournaments by Competition.
     */
    @Test
    public void getByCompetitionId() {
        add();
        Tournament t1 = new Tournament();
        t1.setCompetitionId(competitionId);
        tournamentManager.insert(t1);
        Tournament t2 = new Tournament();
        t2.setCompetitionId(competitionId);
        tournamentManager.insert(t2);

        List<Tournament> tournaments = competitionManager.getByCompetitionId(competitionId);
        assertNotNull(tournaments);
        assertEquals(2, tournaments.size());

        tournamentManager.delete(t2.getId());
        tournaments = competitionManager.getByCompetitionId(competitionId);
        assertNotNull(tournaments);
        assertEquals(1, tournaments.size());

        tournamentManager.delete(t1.getId());
        tournaments = competitionManager.getByCompetitionId(competitionId);
        assertNotNull(tournaments);
        assertTrue(tournaments.isEmpty());
    }

    /**
     * Verify empty competition can be deleted.
     */
    @Test
    public void delete() {
        add();
        List<Competition> competitions = competitionManager.getAll();
        assertNotNull(competitions);
        assertFalse(competitions.isEmpty());

        assertTrue(competitionManager.delete(competitionId));
        Competition competition = competitionManager.getById(competitionId);
        assertNull(competition);

        competitions = competitionManager.getAll();
        assertNotNull(competitions);
        assertTrue(competitions.isEmpty());
    }

    /**
     * Verify competition cannot be deleted when contains tournaments or players.
     */
    @Test
    public void deleteNotEmpty() {
        add();
        long playerId = 1;
        Player player = new Player(playerId, "", "", "");
        competitionManager.addPlayer(inserted, player);
        assertFalse(competitionManager.delete(competitionId));
        assertNotNull(competitionManager.getById(competitionId));

        competitionManager.removePlayerFromCompetition(playerId, competitionId);
        assertTrue(competitionManager.delete(competitionId));
        assertNull(competitionManager.getById(competitionId));

        add();
        Tournament tournament = new Tournament();
        tournament.setCompetitionId(competitionId);
        tournamentManager.insert(tournament);
        assertFalse(competitionManager.delete(competitionId));
        assertNotNull(competitionManager.getById(competitionId));

        tournamentManager.delete(tournament.getId());
        assertTrue(competitionManager.delete(competitionId));
        assertNull(competitionManager.getById(competitionId));
    }

    private void add() {
        inserted = new Competition();
        inserted.setName(name);
        inserted.setNote(note);
        inserted.setType(CompetitionTypes.teams());
        competitionManager.insert(inserted);
        competitionId = inserted.getId();
    }
}