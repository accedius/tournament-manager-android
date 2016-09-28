package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CompetitionDAOTest {
    @Test
    public void testInsert() throws Exception {
        DCompetition comp = new DCompetition(1, "name", new Date(), new Date(), "note", CompetitionType.Teams.toString());
        DAOFactory.getInstance().competitionDAO.insert(RuntimeEnvironment.application, comp);
        DCompetition returnedComp = DAOFactory.getInstance().competitionDAO.getById(RuntimeEnvironment.application, 1);

        assertTrue(comp.getId() == returnedComp.getId());
        assertTrue(comp.getName().equals(returnedComp.getName()));
        assertTrue(comp.getNote().equals(returnedComp.getNote()));
        assertTrue(comp.getType().equals(returnedComp.getType()));
    }

    @Test
    public void testDelete() throws Exception {
        DCompetition comp = new DCompetition(2, "name", new Date(), new Date(), "note", CompetitionType.Teams.toString());
        DAOFactory.getInstance().competitionDAO.insert(RuntimeEnvironment.application, comp);
        DAOFactory.getInstance().competitionDAO.delete(RuntimeEnvironment.application, 2);
        DCompetition returnedComp = DAOFactory.getInstance().competitionDAO.getById(RuntimeEnvironment.application, 2);

        assertNull(returnedComp);
    }

}