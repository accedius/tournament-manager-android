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
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
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
    private static Match match;
    private static long matchId;
    private static final int round = 1;
    private static final int period = 1;
    private static final Date date = new Date(1012604400000L);
    private static final boolean played = true;
    private static ArrayList<SetRowItem> sets;
    private static String uid;

    public static ICompetitionManager competitionManager = null;
    public static ITournamentManager tournamentManager = null;
    public static IMatchManager matchManager = null;
    public static IParticipantManager participantManager = null;
    public static ITeamManager teamManager = null;
    public static IStatisticManager statisticManager = null;

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
        statisticManager = ManagerFactory.getInstance(context).getEntityManager(SAggregatedStats.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(teamManager);
        assertNotNull(statisticManager);
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
        MatchSerializer matchSerializer = MatchSerializer.getInstance(context);
        addCompetitionTournament();
        addMatch();
        Match origin = matchManager.getById(matchId);
        String json = matchSerializer.serialize(origin).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem deserializedItem = gson.fromJson(json, ServerCommunicationItem.class);
        Match deserializedMatch = matchSerializer.deserialize(deserializedItem);

        assertNotNull(deserializedItem.subItems);
        assertFalse(deserializedItem.subItems.isEmpty());
        assertEquals(2, deserializedItem.subItems.size());

        assertEquals(origin.getDate(), deserializedMatch.getDate());
        assertEquals(origin.isPlayed(), deserializedMatch.isPlayed());
        assertEquals(origin.getPeriod(), deserializedMatch.getPeriod());
        assertEquals(origin.getRound(), deserializedMatch.getRound());
        assertEquals(origin.getNote(), deserializedMatch.getNote());
        assertEquals(origin.getSetsNumber(), deserializedMatch.getSetsNumber());

        List<SetRowItem> deserializedSets = statisticManager.getMatchSets(matchId);
        for (int i=0; i<3; i++) {
            assertEquals(sets.get(i).getHomeScore(), deserializedSets.get(i).getHomeScore());
            assertEquals(sets.get(i).getAwayScore(), deserializedSets.get(i).getAwayScore());
        }

//        assertEquals(uid, deserializedMatch.getUid());
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

        match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);
        match.setDate(date);
        match.setPlayed(played);
        match.setNote(note);
        match.setPeriod(period);
        match.setRound(round);
        match.setSetsNumber(0);

        matchManager.insert(match);
        matchId = match.getId();
        assertTrue(matchId > 0);
        uid = match.getUid();

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

        sets = new ArrayList<>();
        sets.add(new SetRowItem(10, 5));
        sets.add(new SetRowItem(5, 15));
        sets.add(new SetRowItem(0, 10));
        matchManager.updateMatch(match.getId(), sets);
        match = matchManager.getById(match.getId());
    }
}
