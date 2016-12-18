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

import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
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
    private static long competitionId = 1;
    private static Tournament inserted;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IPointConfigurationManager pointConfigurationManager = null;
    private static ITeamManager teamManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        pointConfigurationManager = ManagerFactory.getInstance(context).getEntityManager(PointConfiguration.class);
        teamManager = ManagerFactory.getInstance(context).getEntityManager(Team.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(tournamentManager);
        assertNotNull(pointConfigurationManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify that when tournament is created, default point configuration is created.
     */
    @Test
    public void insert() {
        add();
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
    @Test
    public void getByCompetitionId() {
        add();
        List<Tournament> tournaments = tournamentManager.getByCompetitionId(competitionId);
        assertFalse(tournaments.isEmpty());
        Tournament tournament = tournaments.get(0);
        assertEquals(competitionId, tournament.getCompetitionId());
        assertEquals(name, tournament.getName());
        assertEquals(note, tournament.getNote());
    }

    /**
     * Verify that when tournament is deleted, related point configuration is also deleted.
     */
    @Test
    public void delete() {
        add();
        assertTrue(tournamentManager.delete(tournamentId));
        Tournament tournament = tournamentManager.getById(tournamentId);
        assertNull(tournament);

        List<Tournament> tournaments = tournamentManager.getByCompetitionId(competitionId);
        assertNotNull(tournaments);
        assertTrue(tournaments.isEmpty());

        PointConfiguration pointConfiguration = pointConfigurationManager.getById(tournamentId);
        assertNull(pointConfiguration);
    }

    /**
     * Verify that tournament cannot be deleted when contains teams or players.
     */
    @Test
    public void deleteNotEmpty() {
        add();
        long playerId = 1;
        tournamentManager.addPlayer(playerId, tournamentId);
        assertFalse(tournamentManager.delete(tournamentId));
        assertNotNull(tournamentManager.getById(tournamentId));

        tournamentManager.removePlayerFromTournament(playerId, tournamentId);
        assertTrue(tournamentManager.delete(tournamentId));
        assertNull(tournamentManager.getById(tournamentId));

        add();
        Team team = new Team();
        team.setTournamentId(tournamentId);
        teamManager.insert(team);
        assertFalse(tournamentManager.delete(tournamentId));
        assertNotNull(tournamentManager.getById(tournamentId));

        teamManager.delete(team.getId());
        assertTrue(tournamentManager.delete(tournamentId));
        assertNull(tournamentManager.getById(tournamentId));
    }

    private void add() {
        Competition competition = new Competition();
        competition.setType(CompetitionTypes.teams());
        competitionManager.insert(competition);
        competitionId = competition.getId();

        inserted = new Tournament();
        inserted.setCompetitionId(competitionId);
        inserted.setName(name);
        inserted.setNote(note);
        inserted.setStartDate(new Date());
        inserted.setEndDate(new Date());

        tournamentManager.insert(inserted);
        tournamentId = inserted.getId();
    }
}