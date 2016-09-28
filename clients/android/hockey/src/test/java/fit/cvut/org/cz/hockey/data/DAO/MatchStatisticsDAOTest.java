package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MatchStatisticsDAOTest {
    @Test
    public void testInsertAndGet() throws Exception {
        DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(RuntimeEnvironment.application, new DMatchStat(1, true, true));
        DMatchStat returnedStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(RuntimeEnvironment.application, 1);

        assertTrue(returnedStat.isOvertime());
        assertTrue(returnedStat.isShootouts());
        assertTrue(returnedStat.getMatchId() == 1);
    }

    @Test
    public void testUpdate() throws Exception {
        DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(RuntimeEnvironment.application, new DMatchStat(2, true, true));
        DAOFactory.getInstance().matchStatisticsDAO.update(RuntimeEnvironment.application, new DMatchStat(2, false, true));
        DMatchStat returnedStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(RuntimeEnvironment.application, 2);

        assertFalse(returnedStat.isOvertime());
        assertTrue(returnedStat.isShootouts());
        assertTrue(returnedStat.getMatchId() == 2);
    }

    @Test
    public void testDelete() throws Exception {
        DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(RuntimeEnvironment.application, new DMatchStat(3, true, true));
        DAOFactory.getInstance().matchStatisticsDAO.delete(RuntimeEnvironment.application, 3);
        DMatchStat returnedStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(RuntimeEnvironment.application, 3);

        assertNull(returnedStat);
    }
}