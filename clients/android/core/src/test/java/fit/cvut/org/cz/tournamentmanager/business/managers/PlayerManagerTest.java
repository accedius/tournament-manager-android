package fit.cvut.org.cz.tournamentmanager.business.managers;

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

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.IPlayerManager;

/**
 * Created by kevin on 16.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PlayerManagerTest extends AndroidTestCase {
    private Context context;
    private static IPlayerManager playerManager = null;

    private static final String name = "Martin";
    private static final String email = "martin@email.cz";
    private static final String note = "606 111 222";
    private static long playerId;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        playerManager = ManagerFactory.getInstance(context).getEntityManager(Player.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(playerManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify player insertion
     */
    @Test
    public void insert() {
        add();
    }

    /**
     * Verify get Player by id
     */
    @Test
    public void getById() {
        add();
        Player player = playerManager.getById(playerId);
        assertNotNull(player);

        assertEquals(name, player.getName());
        assertEquals(note, player.getNote());
        assertEquals(email, player.getEmail());
    }

    /**
     * Verify delete Player
     */
    @Test
    public void delete() {
        add();
        List<Player> players = playerManager.getAll();
        assertNotNull(players);
        assertFalse(players.isEmpty());

        assertTrue(playerManager.delete(playerId));
        Player player = playerManager.getById(playerId);
        assertNull(player);

        players = playerManager.getAll();
        assertNotNull(players);
        assertTrue(players.isEmpty());
    }

    private void add() {
        Player inserted = new Player();
        inserted.setName(name);
        inserted.setNote(note);
        inserted.setEmail(email);
        playerManager.insert(inserted);
        playerId = inserted.getId();
        assertTrue(playerId > 0);
    }
}