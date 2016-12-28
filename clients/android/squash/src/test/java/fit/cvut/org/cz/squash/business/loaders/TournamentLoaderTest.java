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
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
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

/**
 * Created by kevin on 17.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TournamentLoaderTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Squash";

    private static final String name = "Wimbledon 2016";
    private static final String note = "London";
    private static final Date startDate = new Date(1012604400000L);
    private static final Date endDate = new Date(1046646000000L);
    private static long tournamentId;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static IMatchManager matchManager = null;
    private static IParticipantManager participantManager = null;
    private static ITeamManager teamManager = null;
    private static TournamentSerializer tournamentSerializer = null;

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

        tournamentSerializer = TournamentSerializer.getInstance(context);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
        assertNotNull(participantManager);
        assertNotNull(teamManager);
        assertNotNull(tournamentSerializer);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    @Test
    public void getTournamentsImportInfo() {
        addCompetitionTournament();
        addTeamsMatch();
        Tournament tournament = tournamentManager.getById(tournamentId);
        String json = tournamentSerializer.serialize(tournament).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem item = gson.fromJson(json, ServerCommunicationItem.class);
        List<ServerCommunicationItem> tournaments = new ArrayList<>();
        tournaments.add(item);
        List<TournamentImportInfo> info = TournamentLoader.getTournamentsImportInfo(context, tournaments);
        assertNotNull(info);
        assertFalse(info.isEmpty());
        assertEquals(1, info.size());

        assertEquals(0, info.get(0).getPlayersCnt());
        assertEquals(1, info.get(0).getMatchesCnt());
        assertEquals(3, info.get(0).getTeamsCnt());
    }

    @Test
    public void importTournaments() {
        addCompetitionTournament();
        addTeamsMatch();

        Competition newCompetition = new Competition();
        newCompetition.setType(CompetitionTypes.teams());
        competitionManager.insert(newCompetition);
        Tournament tournament = tournamentManager.getById(tournamentId);
        assertNotNull(tournament);
        String json = tournamentSerializer.serialize(tournament).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem tournamentItem = gson.fromJson(json, ServerCommunicationItem.class);
        List<ServerCommunicationItem> tournaments = new ArrayList<>();
        tournaments.add(tournamentItem);

        Map<String, Player> players = new HashMap<>();
        TournamentLoader.importTournaments(context, tournaments, newCompetition, players);

        List<Tournament> importedTournaments = tournamentManager.getByCompetitionId(newCompetition.getId());
        assertNotNull(importedTournaments);
        assertFalse(importedTournaments.isEmpty());
        assertEquals(1, importedTournaments.size());

        Tournament imported = importedTournaments.get(0);
        assertEquals(name, imported.getName());
        assertEquals(note, imported.getNote());
        assertEquals(startDate, imported.getStartDate());
        assertEquals(endDate, imported.getEndDate());

        List<Team> importedTeams = teamManager.getByTournamentId(imported.getId());
        assertNotNull(importedTeams);
        assertFalse(importedTeams.isEmpty());
        assertEquals(3, importedTeams.size());

        List<Match> importedMatches = matchManager.getByTournamentId(imported.getId());
        assertNotNull(importedMatches);
        assertFalse(importedMatches.isEmpty());
        assertEquals(1, importedMatches.size());
    }

    private void addCompetitionTournament() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setName(name);
        t.setNote(note);
        t.setStartDate(startDate);
        t.setEndDate(endDate);
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
        match.setNote(note);

        matchManager.insert(match);
        assertTrue(match.getId() > 0);

        Participant p1 = new Participant();
        p1.setMatchId(match.getId());
        p1.setParticipantId(t1.getId());
        p1.setRole(ParticipantType.home.toString());
        participantManager.insert(p1);

        Participant p2 = new Participant();
        p2.setMatchId(match.getId());
        p2.setParticipantId(t2.getId());
        p2.setRole(ParticipantType.away.toString());
        participantManager.insert(p2);
    }
}
