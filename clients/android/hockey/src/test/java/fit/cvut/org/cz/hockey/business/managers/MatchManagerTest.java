package fit.cvut.org.cz.hockey.business.managers;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
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
    private static final String sportContext = "Hockey";

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
        ((HockeyPackage)context.getApplicationContext()).setSportContext(sportContext);

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
        addCompetitionTournament();
        addMatch();
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
        addCompetitionTournament();
        addMatch();
        matchManager.resetMatch(matchId);
        Match match = matchManager.getById(matchId);
        assertNotNull(match);
        assertFalse(match.isPlayed());
    }

    /**
     * Verify getByTournamentId method returns correct matches list.
     */
    @Test
    public void getByTournamentId() {
        addCompetitionTournament();
        addMatch();
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
     * Verify All-Play-All generating matches all play all.
     */
    @Test
    public void generateRoundAllPlayAll() {
        addCompetitionTournament();
        addTeam();
        addTeam();
        addTeam();
        addTeam();
        assertTrue(matchManager.getByTournamentId(tournamentId).isEmpty());

        matchManager.generateRound(tournamentId);
        matchManager.generateRound(tournamentId);

        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        assertEquals(12, matches.size());

        Map<Integer, List<Match>> periodMatches = new HashMap<>();
        Map<Integer, List<Match>> roundMatches = new HashMap<>();
        Map<Long, List<Match>> homeTeamMatch = new HashMap<>();
        for (Match match : matches) {
            if (!periodMatches.containsKey(match.getPeriod()))
                periodMatches.put(match.getPeriod(), new ArrayList<Match>());
            periodMatches.get(match.getPeriod()).add(match);

            if (!roundMatches.containsKey(match.getRound()))
                roundMatches.put(match.getRound(), new ArrayList<Match>());
            roundMatches.get(match.getRound()).add(match);

            if (!homeTeamMatch.containsKey(match.getHomeParticipantId()))
                homeTeamMatch.put(match.getHomeParticipantId(), new ArrayList<Match>());
            homeTeamMatch.get(match.getHomeParticipantId()).add(match);
        }

        /* Verify each round has same matches count. */
        assertFalse(roundMatches.containsKey(0));
        assertFalse(roundMatches.containsKey(3));
        for (int i=1; i<=2; i++) {
            assertEquals(6, roundMatches.get(i).size());
        }

        /* Verify each period has same matches count. */
        assertFalse(periodMatches.containsKey(0));
        assertFalse(periodMatches.containsKey(4));
        for (int i=1; i<=2; i++) {
            assertEquals(4, periodMatches.get(i).size());
        }
    }

    /**
     * Verify All-Play-All generating correct number of matches.
     */
    @Test
    public void generateRoundCorrectMatchesNumber() {
        int matchesCount = 0;
        addCompetitionTournament();
        addTeam();
        assertEquals(matchesCount, matchManager.getByTournamentId(tournamentId).size());

        List<Match> matches;
        int sum = 0;
        for (int i=1; i<=4; i++) {
            sum += i;
            addTeam();
            matchesCount += sum;

            matchManager.generateRound(tournamentId);
            matches = matchManager.getByTournamentId(tournamentId);
            assertNotNull(matches);
            assertFalse(matches.isEmpty());
            assertEquals(matchesCount, matches.size());
        }

        matches = matchManager.getAll();
        assertEquals(matchesCount, matches.size());
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

    private void addCompetitionTournament() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
        tournamentId = t.getId();
    }

    private void addMatch() {
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

    private void addTeam() {
        Team t1 = new Team();
        t1.setTournamentId(tournamentId);
        teamManager.insert(t1);
    }
}