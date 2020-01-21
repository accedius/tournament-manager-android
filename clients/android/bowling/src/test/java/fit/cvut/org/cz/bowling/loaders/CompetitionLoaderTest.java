package fit.cvut.org.cz.bowling.loaders;

import android.content.Context;

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

import fit.cvut.org.cz.bowling.BuildConfig;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.loaders.CompetitionLoader;
import fit.cvut.org.cz.bowling.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.bowling.presentation.BowlingPackage;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CompetitionLoaderTest {
    private Context context;
    private static final String sportContext = "Bowling";

    private static final String name = "Bowling world championship 2016";
    private static final String note = "Praha";
    private static final Date startDate = new Date(1012604400000L);
    private static final Date endDate = new Date(1046646000000L);
    private static final CompetitionType type = CompetitionTypes.teams();
    private static long competitionId = 3;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;
    private static CompetitionSerializer competitionSerializer = null;

    @Before
    public void setUp() {


        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);
        competitionSerializer = CompetitionSerializer.getInstance(context);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify get import info works correctly.
     */
    @Test
    public void getImportInfo() {
        addCompetitionTournament();

        Competition competition = competitionManager.getById(competitionId);
        assertNotNull(competition);
        String json = competitionSerializer.serialize(competition).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem competitionItem = gson.fromJson(json, ServerCommunicationItem.class);

        ImportInfo competitionInfo;
        List<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
        List<PlayerImportInfo> playersInfo = new ArrayList<>();
        ArrayList<Conflict> conflicts = new ArrayList<>();
        competitionInfo = CompetitionLoader.getImportInfo(context, context.getResources(), competitionItem, tournamentsInfo, playersInfo, conflicts);

        assertEquals(name, competitionInfo.getName());
        assertEquals(type.id, ((CompetitionImportInfo)competitionInfo).getType().id);

        assertFalse(tournamentsInfo.isEmpty());
        assertEquals(1, tournamentsInfo.size());
        assertTrue(playersInfo.isEmpty());
        assertTrue(conflicts.isEmpty());
    }

    /**
     * Verify import competition works correctly.
     */
    @Test
    public void importCompetition() {
        addCompetitionTournament();

        Competition competition = competitionManager.getById(competitionId);
        assertNotNull(competition);
        String json = competitionSerializer.serialize(competition).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem competitionItem = gson.fromJson(json, ServerCommunicationItem.class);

        Map<String, String> conflictSolutions = new HashMap<>();
        Competition imported = CompetitionLoader.importCompetition(context, competitionItem, conflictSolutions);

        assertTrue(imported.getName().startsWith(name));
        assertEquals(startDate, imported.getStartDate());
        assertEquals(endDate, imported.getEndDate());
        assertEquals(note, imported.getNote());
        assertEquals(type.id, imported.getType().id);

        Competition inserted = competitionManager.getById(imported.getId());
        assertEquals(imported.getName(), inserted.getName());

        List<Tournament> tournaments = tournamentManager.getByCompetitionId(imported.getId());
        assertNotNull(tournaments);
        assertFalse(tournaments.isEmpty());
        assertEquals(1, tournaments.size());
    }

    private void addCompetitionTournament() {
        Competition c = new Competition();
        c.setName(name);
        c.setStartDate(startDate);
        c.setEndDate(endDate);
        c.setNote(note);
        c.setType(type);
        competitionManager.insert(c);
        competitionId = c.getId();
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
    }
}
