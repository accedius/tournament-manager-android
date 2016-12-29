package fit.cvut.org.cz.squash.business.loaders;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.business.serialization.MatchSerializer;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;

/**
 * Created by kevin on 17.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchLoaderTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Squash";

    private static final String note = "Helsinki Arena";
    private static final Date date = new Date(1012604400000L);
    private static long tournamentId;
    private static long matchId;
    private static final int round = 1;
    private static final int period = 1;
    private static final boolean played = true;
    private static final int setsNumber = 3;
    private static ArrayList<SetRowItem> sets;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static IParticipantStatManager participantStatManager = null;
    private static ITeamManager teamManager = null;
    private static IStatisticManager statisticManager = null;
    private static MatchSerializer matchSerializer = null;

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

        matchSerializer = MatchSerializer.getInstance(context);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(participantStatManager);
        assertNotNull(teamManager);
        assertNotNull(statisticManager);
        assertNotNull(matchSerializer);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    @Test
    public void importMatches() {
        addCompetitionTournament();
        addTeamsMatch();

        Competition newCompetition = new Competition();
        newCompetition.setType(CompetitionTypes.teams());
        competitionManager.insert(newCompetition);
        Tournament newTournament = new Tournament();
        newTournament.setCompetitionId(newCompetition.getId());
        tournamentManager.insert(newTournament);

        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertNotNull(matches);
        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertNotNull(match);
        String json = matchSerializer.serialize(match).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem matchItem = gson.fromJson(json, ServerCommunicationItem.class);
        List<ServerCommunicationItem> matchesItems = new ArrayList<>();
        matchesItems.add(matchItem);

        Map<String, Team> teams = new HashMap<>();
        for (Team team : teamManager.getByTournamentId(tournamentId))
            teams.put(String.valueOf(team.getId()), team);

        Map<String, Player> players = new HashMap<>();
        MatchLoader.importMatches(context, matchesItems, newTournament, newCompetition, teams, players);

        List<Match> importedMatches = matchManager.getByTournamentId(newTournament.getId());
        assertNotNull(importedMatches);
        assertFalse(importedMatches.isEmpty());
        assertEquals(1, importedMatches.size());

        Match imported = importedMatches.get(0);
        assertEquals(note, imported.getNote());
        assertEquals(date, imported.getDate());
        assertEquals(played, imported.isPlayed());
        assertEquals(setsNumber, imported.getSetsNumber());
        assertEquals(round, imported.getRound());
        assertEquals(period, imported.getPeriod());

        List<Participant> importedParticipants = participantManager.getByMatchId(imported.getId());
        assertNotNull(importedParticipants);
        assertFalse(importedParticipants.isEmpty());
        assertEquals(2, importedParticipants.size());

        List<SetRowItem> importedSets = statisticManager.getMatchSets(imported.getId());
        assertNotNull(importedSets);
        assertFalse(importedSets.isEmpty());
        assertEquals(3, importedSets.size());
        for (int i=0; i<importedSets.size(); i++) {
            assertEquals(sets.get(i).getHomeScore(), importedSets.get(i).getHomeScore());
            assertEquals(sets.get(i).getAwayScore(), importedSets.get(i).getAwayScore());
        }
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

    private void addTeamsMatch() {
        Team t1 = new Team();
        t1.setTournamentId(tournamentId);
        teamManager.insert(t1);

        Team t2 = new Team();
        t2.setTournamentId(tournamentId);
        teamManager.insert(t2);

        Team t3 = new Team();
        t3.setTournamentId(tournamentId);
        teamManager.insert(t3);

        Match match = new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match());
        match.setTournamentId(tournamentId);
        match.setDate(date);
        match.setPlayed(played);
        match.setNote(note);
        match.setPeriod(period);
        match.setRound(round);

        matchManager.insert(match);
        matchId = match.getId();
        assertTrue(match.getId() > 0);

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
    }
}
