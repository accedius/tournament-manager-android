package fit.cvut.org.cz.hockey.business.managers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MatchManagerTest {

    @Mock
    IScoredMatchManager mockMM;
    long id = 3;

    @Before
    public void setUp() throws Exception {
        mockMM = Mockito.mock(IScoredMatchManager.class);
        when(mockMM.getById(RuntimeEnvironment.application, 1)).thenReturn( new ScoredMatch(new DMatch(1, 1, 1, 1, new Date(), "note", true)) );
        ManagerFactory.getInstance().matchManager = mockMM;
    }

    @Test
    public void testMatchFind() throws Exception {
        ScoredMatch match = ManagerFactory.getInstance().matchManager.getById(RuntimeEnvironment.application, 1);
        assertTrue(match.getId() == 1);
    }

    @After
    public void tearDown() throws Exception {
        ManagerFactory.getInstance().matchManager = new MatchManager();

    }
}