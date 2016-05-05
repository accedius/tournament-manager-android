package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.data.DAO.PointConfigDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;
import fit.cvut.org.cz.hockey.data.interfaces.IPointConfigDAO;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PointConfigManagerTest {

    @Mock
    IPointConfigDAO mockPointConfigDAO;

    @Before
    public void setUp() throws Exception {
        mockPointConfigDAO = Mockito.mock(IPointConfigDAO.class);
        DAOFactory.getInstance().pointConfigDAO = mockPointConfigDAO;
    }

    @Test
    public void testUpdateCalled() throws Exception {
        ManagerFactory.getInstance().pointConfigManager.update(RuntimeEnvironment.application, new PointConfiguration(1L,1L,1L,1L,1L,1L,1L,1L), 1L);
        verify(mockPointConfigDAO).update(any(Context.class), any(DPointConfiguration.class), anyLong());
    }

    @Test
    public void testGetPointConfig() throws Exception {
        when(mockPointConfigDAO.getByTournamentId(RuntimeEnvironment.application, 1L)).thenReturn(new DPointConfiguration(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L));
        PointConfiguration config = ManagerFactory.getInstance().pointConfigManager.getByTournamentId(RuntimeEnvironment.application, 1L);
        verify(mockPointConfigDAO).getByTournamentId(any(Context.class), anyLong());
        assertTrue(config.ntD == 1L);
        assertTrue(config.ntW == 1L);
        assertTrue(config.ntL == 1L);
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().pointConfigDAO = new PointConfigDAO();

    }
}