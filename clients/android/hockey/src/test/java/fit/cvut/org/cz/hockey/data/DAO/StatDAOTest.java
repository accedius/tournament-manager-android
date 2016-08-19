package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StatDAOTest {

    @Test
    public void testInsertUpdateDelete() throws Exception {
        long newId = DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(1, 1, 1, StatsEnum.goals.toString(), 1, 1, "20"));
        ArrayList<DStat> retStat = DAOFactory.getInstance().statDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1);
        assertTrue(!retStat.isEmpty());

        DStat newStat = new DStat(newId, 1, 1, StatsEnum.goals.toString(), 1, 1, "5");
        DAOFactory.getInstance().statDAO.update(RuntimeEnvironment.application, newStat );
        retStat = DAOFactory.getInstance().statDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1);
        assertTrue(retStat.get(0).getValue().equals("5"));

        DAOFactory.getInstance().statDAO.delete(RuntimeEnvironment.application, newId);
        retStat = DAOFactory.getInstance().statDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1);
        assertTrue(retStat.isEmpty());
    }

    @Test
    public void testGetByTourId() throws Exception {
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(1, 1, 1, StatsEnum.goals.toString(), 1, 1, "20"));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(2, 2, 1, StatsEnum.goals.toString(), 1, 1, "20"));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(3, 3, 1, StatsEnum.goals.toString(), 1, 1, "20"));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(4, 4, 1, StatsEnum.goals.toString(), 1, 1, "20"));

        ArrayList<DStat> retStat = DAOFactory.getInstance().statDAO.getStatsByTournamentId(RuntimeEnvironment.application, 1);

        assertTrue(retStat.size() == 4);
    }
}