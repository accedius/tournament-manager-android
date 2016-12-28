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
public class ParticipantManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static long tournamentId;
    private static long matchId;
    private static Participant participant1;
    private static Participant participant2;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static ITeamManager teamManager = null;

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
     * Verify correct Participants are returned by match id.
     */
    @Test
    public void getByMatchId() {
        add();
        List<Participant> participants = participantManager.getByMatchId(matchId);
        assertNotNull(participants);
        assertEquals(2, participants.size());

        for (Participant participant : participants) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                assertEquals(participant1.getId(), participant.getId());
                assertEquals(participant1.getMatchId(), participant.getMatchId());
                assertEquals(participant1.getParticipantId(), participant.getParticipantId());
            } else {
                assertEquals(participant2.getId(), participant.getId());
                assertEquals(participant2.getMatchId(), participant.getMatchId());
                assertEquals(participant2.getParticipantId(), participant.getParticipantId());
            }
        }
    }

    /**
     * Verify correct Participant is returned by role and match.
     */
    @Test
    public void getByRoleAndMatchId() {
        add();
        Participant p1 = participantManager.getByRoleAndMatchId(ParticipantType.home.toString(), matchId);
        assertNotNull(p1);
        assertEquals(participant1.getId(), p1.getId());
        assertEquals(participant1.getMatchId(), p1.getMatchId());
        assertEquals(participant1.getParticipantId(), p1.getParticipantId());

        Participant p2 = participantManager.getByRoleAndMatchId(ParticipantType.away.toString(), matchId);
        assertNotNull(p2);
        assertEquals(participant2.getId(), p2.getId());
        assertEquals(participant2.getMatchId(), p2.getMatchId());
        assertEquals(participant2.getParticipantId(), p2.getParticipantId());
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

        Match match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);

        matchManager.insert(match);
        matchId = match.getId();
        assertTrue(matchId > 0);

        participant1 = new Participant();
        participant1.setMatchId(matchId);
        participant1.setParticipantId(t1.getId());
        participant1.setRole(ParticipantType.home.toString());
        participantManager.insert(participant1);

        participant2 = new Participant();
        participant2.setMatchId(matchId);
        participant2.setParticipantId(t2.getId());
        participant2.setRole(ParticipantType.away.toString());
        participantManager.insert(participant2);
    }
}
