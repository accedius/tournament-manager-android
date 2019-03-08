package fit.cvut.org.cz.tournamentmanager.business.helpers;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fit.cvut.org.cz.tournamentmanager.BuildConfig;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;

/**
 * Created by kevin on 16.12.2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class JsonFileValidatorTest extends AndroidTestCase {
    private static final String sportName = "Tennis";

    @Before
    public void setUp() {
    }

    @After
    public void reset() {
        ManagerFactory.reset();
    }

    /**
     * Verify valid json content.
     */
    @Test
    public void valid() {
        String validContent = "{  " +
                "   \"created\":null," +
                "   \"etag\":null," +
                "   \"id\":6," +
                "   \"modified\":null," +
                "   \"presenter\":\"Competition\"," +
                "   \"uid\":\"6\"," +
                "   \"subItems\":[]," +
                "   \"syncData\":{  " +
                "      \"Sport\":\"Tennis\"," +
                "      \"type\":\"0\"," +
                "      \"start\":null," +
                "      \"note\":\"\"," +
                "      \"end\":null," +
                "      \"name\":\"A\"" +
                "   }," +
                "   \"token\":{  " +
                "      \"type\":null," +
                "      \"value\":null" +
                "   }," +
                "   \"type\":\"Competition\"" +
                "}";
        assertTrue(JsonFileValidator.valid(validContent, sportName));
    }

    /**
     * Verify invalid json content.
     */
    @Test
    public void invalid() {
        String invalidContent = "";
        assertFalse(JsonFileValidator.valid(invalidContent, sportName));


        invalidContent = "{  " +
                "   \"created\":null," +
                "   \"etag\":null," +
                "   \"id\":6," +
                "   \"modified\":null," +
                "   \"presenter\":\"Competition\"," +
                "   \"uid\":\"6\"," +
                "   \"subItems\":[]," +
                "   \"syncData\":{  " +
                "      \"Sport\":\"Badminton\"," +
                "      \"type\":\"0\"," +
                "      \"start\":null," +
                "      \"note\":\"\"," +
                "      \"end\":null," +
                "      \"name\":\"A\"" +
                "   }," +
                "   \"token\":{  " +
                "      \"type\":null," +
                "      \"value\":null" +
                "   }," +
                "   \"type\":\"Competition\"" +
                "}";
        assertFalse(JsonFileValidator.valid(invalidContent, sportName));
    }
}