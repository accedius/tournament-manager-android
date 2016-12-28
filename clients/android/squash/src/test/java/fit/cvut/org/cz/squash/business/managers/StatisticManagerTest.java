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

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
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
public class StatisticManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static long tournamentId;
    private static Match match;
    private static Participant participant1;
    private static Participant participant2;
    private static ArrayList<SetRowItem> sets;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static IParticipantStatManager participantStatManager = null;
    private static ITeamManager teamManager = null;
    private static IStatisticManager statisticManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        matchManager = ManagerFactory.getInstance(context).getEntityManager(Match.class);
        participantManager = ManagerFactory.getInstance(context).getEntityManager(Participant.class);
        participantStatManager = ManagerFactory.getInstance(context).getEntityManager(ParticipantStat.class);
        teamManager = ManagerFactory.getInstance(context).getEntityManager(Team.class);
        statisticManager = ManagerFactory.getInstance(context).getEntityManager(SAggregatedStats.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(participantStatManager);
        assertNotNull(teamManager);
        assertNotNull(statisticManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify Match Sets are loaded correctly.
     */
    @Test
    public void getMatchSets() {
        add();
        List<SetRowItem> matchSets = statisticManager.getMatchSets(match.getId());
        assertEquals(sets.size(), matchSets.size());
        int i=0;
        for (SetRowItem set : matchSets) {
            assertEquals(sets.get(i).getHomeScore(), set.getHomeScore());
            assertEquals(sets.get(i).getAwayScore(), set.getAwayScore());
            i++;
        }
    }

    /**
     * Verify Standing is returned correctly and ordered.
     */
    @Test
    public void getStandingsByTournamentId() {
        add();
        List<Team> teams = teamManager.getByTournamentId(tournamentId);
        List<StandingItem> standings = statisticManager.getStandingsByTournamentId(tournamentId);
        assertEquals(teams.size(), standings.size());

        StandingItem lastStanding;
        int i=0;
        for (StandingItem standing : standings) {
            lastStanding = standings.get(i++);

            assertTrue(lastStanding.points >= standing.points);
            if (lastStanding.points == standing.points) {
                assertTrue(lastStanding.setsWon - lastStanding.setsLost >= standing.setsWon - standing.setsLost);
                if (lastStanding.setsWon - lastStanding.setsLost == standing.setsWon - standing.setsLost) {
                    assertTrue(lastStanding.setsWon >= standing.setsWon);
                    if (lastStanding.setsWon == standing.setsWon)
                        assertTrue(lastStanding.getMatches() >= standing.getMatches());
                }
            }
        }
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

        match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);

        matchManager.insert(match);
        assertTrue(match.getId() > 0);

        participant1 = new Participant();
        participant1.setMatchId(match.getId());
        participant1.setParticipantId(t1.getId());
        participant1.setRole(ParticipantType.home.toString());
        participantManager.insert(participant1);

        participant2 = new Participant();
        participant2.setMatchId(match.getId());
        participant2.setParticipantId(t2.getId());
        participant2.setRole(ParticipantType.away.toString());
        participantManager.insert(participant2);

        sets = new ArrayList<>();
        sets.add(new SetRowItem(10, 5));
        sets.add(new SetRowItem(5, 15));
        sets.add(new SetRowItem(0, 10));
        matchManager.updateMatch(match.getId(), sets);
        match = matchManager.getById(match.getId());
    }
}
