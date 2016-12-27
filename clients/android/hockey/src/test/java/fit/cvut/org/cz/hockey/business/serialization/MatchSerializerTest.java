package fit.cvut.org.cz.hockey.business.serialization;

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

import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
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
    private static final String sportContext = "Floorball";

    private static final String note = "Center Court";
    private static long tournamentId;
    private static long matchId;
    private static final int round = 1;
    private static final int period = 1;
    private static final boolean played = true;
    private static final int homeScore = 4;
    private static final int awayScore = 3;
    private static final boolean overtime = true;
    private static final boolean shootouts = true;
    private static final Date date = new Date(1012604400000L);
    private static String uid;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static ITeamManager teamManager = null;

    private static MatchSerializer matchSerializer = null;

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

        matchSerializer = MatchSerializer.getInstance(context);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(teamManager);

        assertNotNull(matchSerializer);
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
        Match origin = matchManager.getByTournamentId(tournamentId).get(0);
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
        assertEquals(origin.isOvertime(), deserializedMatch.isOvertime());
        assertEquals(origin.isShootouts(), deserializedMatch.isShootouts());
        assertEquals(origin.getHomeScore(), deserializedMatch.getHomeScore());
        assertEquals(origin.getAwayScore(), deserializedMatch.getAwayScore());

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

        Match match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);
        match.setDate(date);
        match.setPlayed(played);
        match.setNote(note);
        match.setPeriod(period);
        match.setRound(round);
        match.setOvertime(overtime);
        match.setShootouts(shootouts);

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

        ParticipantStat s1 = new ParticipantStat();
        s1.setParticipantId(p1.getId());
        s1.setScore(homeScore);

        ParticipantStat s2 = new ParticipantStat();
        s2.setParticipantId(p2.getId());
        s2.setScore(awayScore);
    }
}
