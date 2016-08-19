package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MatchDAOTest {

    @Test
    public void testInsert() throws Exception {
        DMatch toInsert = new DMatch(1, 1, 1, 1, new Date(), "note", false);
        DAOFactory.getInstance().matchDAO.insert(RuntimeEnvironment.application, toInsert);
        DMatch returnedMatch = DAOFactory.getInstance().matchDAO.getById(RuntimeEnvironment.application, toInsert.getId());

        assertTrue(toInsert.getId() == returnedMatch.getId());
        assertTrue(toInsert.getRound() == returnedMatch.getRound());
        assertTrue(toInsert.getPeriod() == returnedMatch.getPeriod());
        assertTrue(toInsert.getTournamentId() == returnedMatch.getTournamentId());
        assertTrue(toInsert.getNote().equals(returnedMatch.getNote()));
    }

    @Test
    public void testDelete() throws Exception {
        DMatch toInsert = new DMatch(2, 1, 1, 1, new Date(), "note", false);
        DAOFactory.getInstance().matchDAO.insert(RuntimeEnvironment.application, toInsert);
        DAOFactory.getInstance().matchDAO.delete(RuntimeEnvironment.application, 2);
        DMatch returnedMatch = DAOFactory.getInstance().matchDAO.getById(RuntimeEnvironment.application, toInsert.getId());

        assertNull( returnedMatch );
    }

    @Test
    public void testGetByTournamentId() throws Exception {
        DMatch toInsert1 = new DMatch(3, 2, 1, 1, new Date(), "note", false);
        DMatch toInsert2 = new DMatch(4, 2, 1, 1, new Date(), "note", false);

        DAOFactory.getInstance().matchDAO.insert(RuntimeEnvironment.application, toInsert1);
        DAOFactory.getInstance().matchDAO.insert(RuntimeEnvironment.application, toInsert2);

        ArrayList<DMatch> returned = DAOFactory.getInstance().matchDAO.getByTournamentId(RuntimeEnvironment.application, 2);

        assertTrue(returned.size() == 2);
    }
}