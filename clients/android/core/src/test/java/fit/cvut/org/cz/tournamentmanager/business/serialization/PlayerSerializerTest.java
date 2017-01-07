package fit.cvut.org.cz.tournamentmanager.business.serialization;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerToken;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerTokenType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;

/**
 * Created by kevin on 5.1.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PlayerSerializerTest extends AndroidTestCase {
    PlayerSerializer playerSerializer = null;
    Context context = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        playerSerializer = PlayerSerializer.getInstance(context);

        assertNotNull(context);
        assertNotNull(playerSerializer);
    }

    @Test
    public void serialize() {
        Player player = new Player(1, "uid", "name", "email", "note");
        player.setEtag("etag");
        player.setTokenType(ServerTokenType.own);
        player.setTokenValue("tokenValue");

        ServerCommunicationItem item = playerSerializer.serialize(player);
        assertNotNull(item);
        assertEquals("etag", item.getEtag());
        assertEquals("1", item.getUid());
        assertEquals("Player", item.getType());
        assertEquals("Player", item.getPresenter());
        assertEquals(ServerTokenType.own, item.getToken().getType());
        assertEquals("tokenValue", item.getToken().getValue());
    }

    @Test
    public void deserialize() {
        Player player = new Player(1, "uid", "name", "email", "note");
        player.setEtag("etag");
        player.setTokenType(ServerTokenType.own);
        player.setTokenValue("tokenValue");

        ServerCommunicationItem item = new ServerCommunicationItem(
                "uid", "etag", new ServerToken(ServerTokenType.own, "tokenValue"),
                "Player", "Player");
        Player deserialized = playerSerializer.deserialize(item);
        assertNotNull(deserialized);
        assertEquals("uid", deserialized.getUid());
        assertEquals("etag", deserialized.getEtag());
        assertEquals(ServerTokenType.own, deserialized.getTokenType());
        assertEquals("tokenValue", deserialized.getTokenValue());
    }
}
