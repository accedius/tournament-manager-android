package fit.cvut.org.cz.bowling.serialization;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.bowling.BuildConfig;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.serialization.TeamSerializer;
import fit.cvut.org.cz.bowling.presentation.BowlingPackage;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TeamSerializerTest {
    private Context context;
    private static final String sportContext = "Floorball";

    private static final String name = "Berdych & Štěpánek";
    private static final long teamId = 1;
    private static final long tournamentId = 12;
    private static String uid;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((BowlingPackage)context.getApplicationContext()).setSportContext(sportContext);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify serialize sync data works correctly.
     */
    @Test
    public void serializeSyncData() {
        TeamSerializer teamSerializer = new TeamSerializer(context);
        Map<String, Object> serialized = teamSerializer.serializeSyncData(getTeam());
        assertNotNull(serialized);
        assertFalse(serialized.isEmpty());
        assertTrue(serialized.containsKey("name")); // TODO constants
        assertEquals(name, (String)serialized.get("name"));
    }

    /**
     * Verify serialize sync data works correctly.
     */
    @Test
    public void serialize() {
        TeamSerializer teamSerializer = TeamSerializer.getInstance(context);
        ServerCommunicationItem serialized = teamSerializer.serialize(getTeam());
        assertNotNull(serialized);
        assertEquals(teamId, (long) serialized.getId());
//        assertEquals(uid, serialized.getUid());
    }

    /**
     * Verify deserialization works correctly.
     */
    @Test
    public void deserialize() {
        TeamSerializer teamSerializer = TeamSerializer.getInstance(context);
        ServerCommunicationItem item = teamSerializer.serialize(getTeam());
        Team team = teamSerializer.deserialize(item);
        assertNotNull(team);
        assertEquals(team.getName(), getTeam().getName());
    }

    /**
     * Verify deserialization works correctly.
     */
    @Test
    public void deserializeSyncData() {
        TeamSerializer teamSerializer = TeamSerializer.getInstance(context);
        HashMap<String, Object> serialized = teamSerializer.serializeSyncData(getTeam());
        Team team = new Team();
        teamSerializer.deserializeSyncData(serialized, team);
        assertNotNull(team);
        assertEquals(team.getName(), getTeam().getName());
    }

    private Team getTeam() {
        Team team = new Team(teamId, tournamentId, name);
        uid = team.getUid();
        return team;
    }
}