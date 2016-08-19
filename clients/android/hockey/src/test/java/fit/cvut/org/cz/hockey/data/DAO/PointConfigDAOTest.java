package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PointConfigDAOTest {

    @Test
    public void testInsertUpdateDeleteGet() throws Exception {
        DPointConfiguration conf = new DPointConfiguration(2L, 2L, 2L, 2L, 2L, 2L, 2L, 2L);

        DAOFactory.getInstance().pointConfigDAO.insertDefault(RuntimeEnvironment.application, 1);
        DPointConfiguration ret = DAOFactory.getInstance().pointConfigDAO.getByTournamentId(RuntimeEnvironment.application, 1L);

        assertNotNull(ret);

        DAOFactory.getInstance().pointConfigDAO.update(RuntimeEnvironment.application, conf, 1L);
        ret = DAOFactory.getInstance().pointConfigDAO.getByTournamentId(RuntimeEnvironment.application, 1L);

        assertTrue( ret.ntD == 2L && ret.ntL == 2L && ret.ntW == 2L );

        DAOFactory.getInstance().pointConfigDAO.delete(RuntimeEnvironment.application, 1L);
        ret = DAOFactory.getInstance().pointConfigDAO.getByTournamentId(RuntimeEnvironment.application, 1L);

        assertNull(ret);
    }
}