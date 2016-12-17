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
 * Created by kevin on 16.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String note = "Center Court";
    private static long tournamentId;
    private static Match inserted;
    private static long matchId;
    private static final int round = 1;
    private static final int period = 1;
    private static final boolean played = true;

    public static ICompetitionManager competitionManager = null;
    public static ITournamentManager tournamentManager = null;
    public static IMatchManager matchManager = null;
    public static IParticipantManager participantManager = null;
    public static ITeamManager teamManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        matchManager = ManagerFactory.getInstance(context).getEntityManager(Match.class);
        participantManager = ManagerFactory.getInstance(context).getEntityManager(Participant.class);
        teamManager = ManagerFactory.getInstance(context).getEntityManager(Team.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(teamManager);
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
        Match match = matchManager.getById(matchId);
        assertEquals(tournamentId, match.getTournamentId());
        assertEquals(played, match.isPlayed());
        assertEquals(note, match.getNote());
        assertEquals(period, match.getPeriod());
        assertEquals(round, match.getRound());
    }

    /**
     * Verify match is not played after reset.
     */
    @Test
    public void resetMatch() {
        add();
        matchManager.resetMatch(matchId);
        Match match = matchManager.getById(matchId);
        assertNotNull(match);
        assertFalse(match.isPlayed());
        assertEquals(0, match.getSetsNumber());
    }

    /**
     * Verify getByTournamentId method returns correct matches list.
     */
    @Test
    public void getByTournamentId() {
        add();
        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertFalse(matches.isEmpty());
        Match match = matches.get(0);
        assertEquals(tournamentId, match.getTournamentId());
        assertEquals(played, match.isPlayed());
        assertEquals(note, match.getNote());
        assertEquals(period, match.getPeriod());
        assertEquals(round, match.getRound());
    }

    /**
     * Verify match deleting.
     */
    @Test
    public void delete() {
        matchManager.delete(matchId);
        Match match = matchManager.getById(matchId);
        assertNull(match);

        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }

    private void add() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
        tournamentId = t.getId();

        Team t1 = new Team();
        t1.setTournamentId(tournamentId);
        teamManager.insert(t1);

        Team t2 = new Team();
        t2.setTournamentId(tournamentId);
        teamManager.insert(t2);

        inserted = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        inserted.setTournamentId(tournamentId);
        inserted.setPlayed(played);
        inserted.setNote(note);
        inserted.setPeriod(period);
        inserted.setRound(round);
        inserted.setSetsNumber(0);

        matchManager.insert(inserted);
        matchId = inserted.getId();
        assertTrue(matchId > 0);

        Participant p1 = new Participant();
        p1.setMatchId(matchId);
        p1.setParticipantId(t1.getId());
        p1.setRole(ParticipantType.home.toString());
        participantManager.insert(p1);

        Participant p2 = new Participant();
        p2.setMatchId(matchId);
        p2.setParticipantId(t2.getId());
        p2.setRole(ParticipantType.away.toString());
        participantManager.insert(p2);
    }
}