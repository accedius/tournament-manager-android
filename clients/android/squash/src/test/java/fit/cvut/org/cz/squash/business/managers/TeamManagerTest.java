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
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 17.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TeamManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String name = "H+H";
    private static long teamId1;
    private static long teamId2;
    private static long matchId;
    private static long tournamentId;
    private static Team inserted1;
    private static Team inserted2;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static ITeamManager teamManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage) context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        teamManager = ManagerFactory.getInstance(context).getEntityManager(Team.class);
        matchManager = ManagerFactory.getInstance(context).getEntityManager(Match.class);
        participantManager = ManagerFactory.getInstance(context).getEntityManager(Participant.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(teamManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify team insertion.
     */
    @Test
    public void insert() {
        addCompetitionTournament();
        add();
    }

    /**
     * Verify get teams by tournament.
     */
    @Test
    public void getByTournamentId() {
        addCompetitionTournament();
        add();
        add();
        List<Team> teams = teamManager.getByTournamentId(tournamentId);
        assertNotNull(teams);
        assertEquals(2, teams.size());

        teamManager.delete(teams.get(1).getId());
        teams = teamManager.getByTournamentId(tournamentId);
        assertNotNull(teams);
        assertEquals(1, teams.size());

        teamManager.delete(teams.get(0).getId());
        teams = teamManager.getByTournamentId(tournamentId);
        assertNotNull(teams);
        assertTrue(teams.isEmpty());
    }

    /**
     * Verify team can be deleted when not played any match.
     */
    @Test
    public void delete() {
        addCompetitionTournament();
        add();
        assertNotNull(teamManager.getById(teamId1));

        assertTrue(teamManager.delete(teamId1));
        assertNull(teamManager.getById(teamId1));
    }

    /**
     * Verify team cannot be deleted when played matches.
     */
    @Test
    public void deleteNotEmpty() {
        addCompetitionTournament();
        addMatch();
        assertNotNull(teamManager.getById(teamId1));
        assertNotNull(teamManager.getById(teamId2));
        assertFalse(teamManager.delete(teamId1));
        assertFalse(teamManager.delete(teamId2));

        matchManager.delete(matchId);
        assertNotNull(teamManager.getById(teamId1));
        assertNotNull(teamManager.getById(teamId2));
        assertTrue(teamManager.delete(teamId1));
        assertTrue(teamManager.delete(teamId2));
    }

    private void addCompetitionTournament() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
        tournamentId = t.getId();
    }

    private void add() {
        inserted1 = new Team();
        inserted1.setTournamentId(tournamentId);
        teamManager.insert(inserted1);
        teamId1 = inserted1.getId();
        assertTrue(inserted1.getId() > 0);
    }

    private void addMatch() {
        inserted1 = new Team();
        inserted1.setTournamentId(tournamentId);
        teamManager.insert(inserted1);
        teamId1 = inserted1.getId();

        inserted2 = new Team();
        inserted2.setTournamentId(tournamentId);
        teamManager.insert(inserted2);
        teamId2 = inserted2.getId();

        Match match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);

        matchManager.insert(match);
        matchId = match.getId();
        assertTrue(match.getId() > 0);

        Participant p1 = new Participant();
        p1.setMatchId(match.getId());
        p1.setParticipantId(inserted1.getId());
        p1.setRole(ParticipantType.home.toString());
        participantManager.insert(p1);

        Participant p2 = new Participant();
        p2.setMatchId(match.getId());
        p2.setParticipantId(inserted2.getId());
        p2.setRole(ParticipantType.away.toString());
        participantManager.insert(p2);
    }
}