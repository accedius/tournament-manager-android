package fit.cvut.org.cz.squash.business.serialization;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
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
public class MatchSerializerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String note = "Center Court";
    private static long tournamentId;
    private static Match inserted;
    private static long matchId;
    private static final int round = 1;
    private static final int period = 1;
    private static final int setsNumber = 0;
    public static final Date date = new Date(1012604400000L);
    private static final boolean played = false;

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
     * Verify serializeSyncData and deserializeSyncData works correctly.
     */
    @Test
    public void serializeSyncData() throws ParseException {
        MatchSerializer matchSerializer = new MatchSerializer(context);
        addCompetitionTournament();
        addMatch();
        Match origin = matchManager.getById(matchId);
        HashMap<String, Object> serialized = matchSerializer.serializeSyncData(origin);
        assertNotNull(serialized);
        assertFalse(serialized.isEmpty());
        assertTrue(serialized.containsKey("note")); // TODO constants
        assertTrue(serialized.containsKey("date")); // TODO constants
        assertTrue(serialized.containsKey("sets")); // TODO constants
        assertTrue(serialized.containsKey("sets_number")); // TODO constants
        assertTrue(serialized.containsKey("players_home")); // TODO constants
        assertTrue(serialized.containsKey("players_away")); // TODO constants

        Match match = new Match();
        matchSerializer.deserializeSyncData(serialized, match);
        assertEquals(origin.getNote(), match.getNote());
        assertEquals(origin.getDate(), match.getDate());
        assertEquals(origin.getSetsNumber(), match.getSetsNumber());
        assertEquals(origin.getHomePlayers(), match.getHomePlayers());
        assertEquals(origin.getAwayPlayers(), match.getAwayPlayers());
    }

    /**
     * Verify serialization works correctly.
     */
    @Test
    public void serialize() {
        MatchSerializer matchSerializer = MatchSerializer.getInstance(context);
        addCompetitionTournament();
        addMatch();

        Match match = matchManager.getById(matchId);
        assertNotNull(match);
        ServerCommunicationItem serialized = matchSerializer.serialize(match);
        assertNotNull(serialized);
        assertEquals(matchId, (long) serialized.getId());

        List<ServerCommunicationItem> teams = new ArrayList<>();
        assertEquals(2, serialized.subItems.size());
        for (ServerCommunicationItem item : serialized.subItems) {
            if (item.getType().equals("Team")) {
                teams.add(item);
            }
        }
        assertEquals(2, teams.size());
//        assertEquals(uid, serialized.getUid()); // FIXME: 17.12.2016
    }

    /**
     * Verify deserialization works correctly.
     */
    @Test
    public void deserialize() {
        MatchSerializer matchSerializer = MatchSerializer.getInstance(context);
        addCompetitionTournament();
        addMatch();
        Match origin = matchManager.getById(matchId);
        ServerCommunicationItem item = matchSerializer.serialize(origin);
        Match match = matchSerializer.deserialize(item);
        assertNotNull(match);
        assertEquals(note, match.getNote());
        assertEquals(date, match.getDate());
        assertEquals(round, match.getRound());
        assertEquals(period, match.getPeriod());
        assertEquals(played, match.isPlayed());
        assertEquals(setsNumber, match.getSetsNumber());
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
        inserted.setDate(date);
        inserted.setPlayed(played);
        inserted.setNote(note);
        inserted.setPeriod(period);
        inserted.setRound(round);
        inserted.setSetsNumber(setsNumber);

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
