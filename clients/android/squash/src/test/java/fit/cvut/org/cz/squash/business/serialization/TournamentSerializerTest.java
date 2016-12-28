package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;
import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.Date;
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

    private static final String name = "Davis Cup 2016";
    private static final String note = "By BNP Paribas";
    private static final Date startDate = new Date(1012604400000L);
    private static final Date endDate = new Date(1046646000000L);
    private static long tournamentId;
    private static String uid;

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
     * Verify serialization and deserialization works correctly.
     */
    @Test
    public void serialization() {
        TournamentSerializer tournamentSerializer = TournamentSerializer.getInstance(context);
        addCompetitionTournament();
        addTeams();
        addMatches();

        Tournament tournament = tournamentManager.getById(tournamentId);
        assertNotNull(tournament);
        String json = tournamentSerializer.serialize(tournament).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem deserializedItem = gson.fromJson(json, ServerCommunicationItem.class);
        Tournament deserializedTournament = tournamentSerializer.deserialize(deserializedItem);

        assertEquals(name, deserializedTournament.getName());
        assertEquals(startDate, deserializedTournament.getStartDate());
        assertEquals(endDate, deserializedTournament.getEndDate());
        assertEquals(note, deserializedTournament.getNote());

        assertNotNull(deserializedItem);
        assertEquals(tournamentId, (long) deserializedItem.getId());

        List<ServerCommunicationItem> teams = new ArrayList<>();
        List<ServerCommunicationItem> matches = new ArrayList<>();
        assertEquals(10, deserializedItem.subItems.size());
        for (ServerCommunicationItem item : deserializedItem.subItems) {
            if (item.getType().equals("Team")) {
                teams.add(item);
            } else if (item.getType().equals("Match")) {
                matches.add(item);
            }
        }

        assertEquals(4, teams.size());
        assertEquals(6, matches.size());
//        assertEquals(uid, deserializedItem.getUid()); // FIXME: 17.12.2016
    }

    private void addCompetitionTournament() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setName(name);
        t.setStartDate(startDate);
        t.setEndDate(endDate);
        t.setNote(note);
        t.setCompetitionId(c.getId());
        uid = t.getUid();
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
}
