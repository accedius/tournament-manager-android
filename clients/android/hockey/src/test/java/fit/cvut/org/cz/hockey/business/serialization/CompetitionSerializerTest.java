package fit.cvut.org.cz.hockey.business.serialization;

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
import java.util.Date;
import java.util.Map;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 17.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CompetitionSerializerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Floorball";

    public static final String name = "IFBU World Cup 2016";
    public static final String note = "Latvia";
    public static final Date startDate = new Date(1012604400000L);
    public static final Date endDate = new Date(1046646000000L);
    public static final long tournamentId = 12;
    public static final long competitionId = 3;
    public static String uid;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((HockeyPackage)context.getApplicationContext()).setSportContext(sportContext);
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
        ServerCommunicationItem serialized = tournamentSerializer.serialize(getTournament());
        assertNotNull(serialized);
        assertEquals(tournamentId, (long) serialized.getId());
        assertEquals(uid, serialized.getUid());
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

    private Tournament getTournament() {
        Tournament tournament = new Tournament(tournamentId, competitionId, name, startDate, endDate, note);
        uid = tournament.getUid();
        return tournament;
    }
}
