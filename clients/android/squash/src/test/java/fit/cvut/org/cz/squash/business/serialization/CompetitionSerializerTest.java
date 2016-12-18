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
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 17.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CompetitionSerializerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String name = "Davis Cup 2016";
    private static final String note = "By BNP Paribas";
    private static final Date startDate = new Date(1012604400000L);
    private static final Date endDate = new Date(1046646000000L);
    private static final CompetitionType type = CompetitionTypes.teams();
    private static long competitionId = 3;
    private static String uid;

    private static ICompetitionManager competitionManager = null;
    private static ITournamentManager tournamentManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        competitionManager = ManagerFactory.getInstance(context).getEntityManager(Competition.class);
        tournamentManager = ManagerFactory.getInstance(context).getEntityManager(Tournament.class);

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
     * Verify serialization and deserialization works correctly.
     */
    @Test
    public void serialization() {
        CompetitionSerializer competitionSerializer = CompetitionSerializer.getInstance(context);
        addCompetitionTournament();

        Competition competition = competitionManager.getById(competitionId);
        assertNotNull(competition);
        String json = competitionSerializer.serialize(competition).toJson();

        Gson gson = new GsonBuilder().serializeNulls().create();
        ServerCommunicationItem deserializedItem = gson.fromJson(json, ServerCommunicationItem.class);
        Competition deserializedCompetition = competitionSerializer.deserialize(deserializedItem);

        assertEquals(name, deserializedCompetition.getName());
        assertEquals(startDate, deserializedCompetition.getStartDate());
        assertEquals(endDate, deserializedCompetition.getEndDate());
        assertEquals(note, deserializedCompetition.getNote());
        assertEquals(type.id, deserializedCompetition.getType().id);

        assertNotNull(deserializedItem);
        assertEquals(competitionId, (long) deserializedItem.getId());

        List<ServerCommunicationItem> tournaments = new ArrayList<>();
        assertEquals(1, deserializedItem.subItems.size());
        for (ServerCommunicationItem item : deserializedItem.subItems) {
            if (item.getType().equals("Tournament")) {
                tournaments.add(item);
            }
        }

        assertEquals(1, tournaments.size());
//        assertEquals(uid, deserializedItem.getUid()); // FIXME: 17.12.2016
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
        uid = c.getUid();
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
    }
}
