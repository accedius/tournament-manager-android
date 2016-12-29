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

import java.util.List;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;

/**
 * Created by kevin on 17.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ParticipantStatManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Hockey";

    private static long tournamentId;
    private static Match match;
    private static Participant participant1;
    private static Participant participant2;
    private static final int scoreHome = 4;
    private static final int scoreAway = 3;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static IParticipantStatManager participantStatManager = null;
    private static ITeamManager teamManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((HockeyPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        matchManager = ManagerFactory.getInstance(context).getEntityManager(Match.class);
        participantManager = ManagerFactory.getInstance(context).getEntityManager(Participant.class);
        participantStatManager = ManagerFactory.getInstance(context).getEntityManager(ParticipantStat.class);
        teamManager = ManagerFactory.getInstance(context).getEntityManager(Team.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(participantStatManager);
        assertNotNull(teamManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify correct score is loaded by participant.
     */
    @Test
    public void getScoreByParticipantId() {
        add();
        Participant homeParticipant = participantManager.getByRoleAndMatchId(ParticipantType.home.toString(), match.getId());
        Participant awayParticipant = participantManager.getByRoleAndMatchId(ParticipantType.away.toString(), match.getId());

        assertEquals(scoreHome, participantStatManager.getScoreByParticipantId(homeParticipant.getId()));
        assertEquals(scoreAway, participantStatManager.getScoreByParticipantId(awayParticipant.getId()));
    }

    /**
     * Verify correct ParticipantStats are returned by Participant.
     */
    @Test
    public void getByParticipantId() {
        add();
        List<ParticipantStat> stats = participantStatManager.getByParticipantId(participant1.getId());
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
        assertEquals(1, stats.size());
        assertEquals(scoreHome, stats.get(0).getScore());

        stats = participantStatManager.getByParticipantId(participant2.getId());
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
        assertEquals(1, stats.size());
        assertEquals(scoreAway, stats.get(0).getScore());
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

        participantStatManager.insert(new ParticipantStat(participant1.getId(), scoreHome));
        participantStatManager.insert(new ParticipantStat(participant2.getId(), scoreAway));
    }
}
