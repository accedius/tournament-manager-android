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

import fit.cvut.org.cz.tournamentmanager.BuildConfig;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.ISettingManager;

/**
 * Created by kevin on 16.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SettingManagerTest extends AndroidTestCase {
    private Context context;

    private static final String packageName = "fit.cvut.org.cz.squash";
    private static final String sportName = "Tennis";

    private static long settingId;

    private static ISettingManager settingManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();

        settingManager = ManagerFactory.getInstance(context).getEntityManager(Setting.class);

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertNotNull(settingManager);
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify match insertion.
     */
    @Test
    public void insert() {
        add();
    }

    /**
     * Verify get Setting.
     */
    @Test
    public void getById() {
        add();
        Setting setting = settingManager.getById(settingId);
        assertNotNull(setting);

        assertEquals(packageName, setting.getPackageName());
        assertEquals(sportName, setting.getSportName());
    }

    /**
     * Verify get Setting by Package and Sport.
     */
    @Test
    public void getByPackageSport() {
        add();
        Setting setting = settingManager.getByPackageSport(packageName, sportName);
        assertNotNull(setting);

        assertEquals(packageName, setting.getPackageName());
        assertEquals(sportName, setting.getSportName());

        Setting settingById = settingManager.getById(settingId);
        assertEquals(settingById, setting);
    }

    /**
     * Verify setting can be deleted.
     */
    @Test
    public void delete() {
        add();
        List<Setting> settings = settingManager.getAll();
        assertNotNull(settings);
        assertFalse(settings.isEmpty());

        assertTrue(settingManager.delete(settingId));
        Setting setting = settingManager.getById(settingId);
        assertNull(setting);

        settings = settingManager.getAll();
        assertNotNull(settings);
        assertTrue(settings.isEmpty());
    }

    private void add() {
        Setting inserted = new Setting(packageName, sportName);
        settingManager.insert(inserted);
        settingId = inserted.getId();
        assertTrue(settingId > 0);
    }
}