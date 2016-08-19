package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;

import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PackagePlayerDAOTest {

    @Test
    public void testComp() throws Exception {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToCompetition(RuntimeEnvironment.application, 1, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToCompetition(RuntimeEnvironment.application, 2, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToCompetition(RuntimeEnvironment.application, 3, 1);

        ArrayList<Long> ids = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(RuntimeEnvironment.application, 1);

        assertTrue(ids.size() == 3);
    }

    @Test
    public void testTourn() throws Exception {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTournament(RuntimeEnvironment.application, 1, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTournament(RuntimeEnvironment.application, 2, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTournament(RuntimeEnvironment.application, 3, 1);

        ArrayList<Long> ids = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament(RuntimeEnvironment.application, 1);

        assertTrue(ids.size() == 3);
    }

    @Test
    public void testTeam() throws Exception {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTeam(RuntimeEnvironment.application, 1, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTeam(RuntimeEnvironment.application, 2, 1);
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTeam(RuntimeEnvironment.application, 3, 1);

        ArrayList<Long> ids = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(RuntimeEnvironment.application, 1);

        assertTrue(ids.size() == 3);

        DAOFactory.getInstance().packagePlayerDAO.deleteAllPlayersFromTeam(RuntimeEnvironment.application, 1);

        ids = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(RuntimeEnvironment.application, 1);

        assertTrue(ids.size() == 0);
    }
}