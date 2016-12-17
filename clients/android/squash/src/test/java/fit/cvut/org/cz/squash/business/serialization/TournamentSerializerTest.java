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
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 17.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TournamentSerializerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    public static final String name = "Davis Cup 2016";
    public static final String note = "By BNP Paribasw";
    public static final Date startDate = new Date(1012604400000L);
    public static final Date endDate = new Date(1046646000000L);
    public static long tournamentId = 12;
    public static final long competitionId = 3;
    public static String uid;

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
     * Verify serialize sync data works correctly.
     */
    @Test
    public void serializeSyncData() throws ParseException {
        TournamentSerializer tournamentSerializer = new TournamentSerializer(context);
        Map<String, Object> serialized = tournamentSerializer.serializeSyncData(getTournament());
        assertNotNull(serialized);
        assertFalse(serialized.isEmpty());
        assertTrue(serialized.containsKey("name")); // TODO constants
        assertTrue(serialized.containsKey("note")); // TODO constants
        assertTrue(serialized.containsKey("start_date")); // TODO constants
        assertTrue(serialized.containsKey("end_date")); // TODO constants

        assertEquals(name, (String)serialized.get("name"));
        assertEquals(note, (String)serialized.get("note"));
        Date serializedStartDate = DateFormatter.getInstance().getDBDateFormat().parse((String)serialized.get("start_date"));
        Date serializedEndDate = DateFormatter.getInstance().getDBDateFormat().parse((String)serialized.get("end_date"));
        assertTrue(startDate.compareTo(serializedStartDate) == 0);
        assertTrue(endDate.compareTo(serializedEndDate) == 0);
    }

    /**
     * Verify serialize sync data works correctly.
     */
    @Test
    public void serialize() {
        TournamentSerializer tournamentSerializer = TournamentSerializer.getInstance(context);
        addCompetitionTournament();
        addTeams();
        addMatches();

        Tournament tournament = tournamentManager.getById(tournamentId);
        assertNotNull(tournament);
        ServerCommunicationItem serialized = tournamentSerializer.serialize(tournament);
        assertNotNull(serialized);
        assertEquals(tournamentId, (long) serialized.getId());

        List<ServerCommunicationItem> teams = new ArrayList<>();
        List<ServerCommunicationItem> matches = new ArrayList<>();
        assertEquals(10, serialized.subItems.size());
        for (ServerCommunicationItem item : serialized.subItems) {
            if (item.getType().equals("Team")) {
                teams.add(item);
            } else if (item.getType().equals("Match")) {
                matches.add(item);
            }
        }
        assertEquals(4, teams.size());
        assertEquals(6, matches.size());
//        assertEquals(uid, serialized.getUid()); // FIXME: 17.12.2016
    }

    /**
     * Verify deserialization works correctly.
     */
    @Test
    public void deserialize() {
        TournamentSerializer tournamentSerializer = TournamentSerializer.getInstance(context);
        ServerCommunicationItem item = tournamentSerializer.serialize(getTournament());
        Tournament team = tournamentSerializer.deserialize(item);
        assertNotNull(team);
        assertEquals(team.getName(), getTournament().getName());
        assertEquals(team.getNote(), getTournament().getNote());
        assertEquals(team.getStartDate(), getTournament().getStartDate());
        assertEquals(team.getEndDate(), getTournament().getEndDate());
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

    private void addTeams() {
        Team a = new Team();
        a.setName("A team");
        a.setTournamentId(tournamentId);
        teamManager.insert(a);

        Team b = new Team();
        b.setName("B team");
        b.setTournamentId(tournamentId);
        teamManager.insert(b);

        Team c = new Team();
        c.setName("C team");
        c.setTournamentId(tournamentId);
        teamManager.insert(c);

        Team d = new Team();
        d.setName("D team");
        d.setTournamentId(tournamentId);
        teamManager.insert(d);
    }

    private void addMatches() {
        matchManager.generateRound(tournamentId);
    }

    private Tournament getTournament() {
        Tournament tournament = new Tournament(tournamentId, competitionId, name, startDate, endDate, note);
        uid = tournament.getUid();
        return tournament;
    }
}
