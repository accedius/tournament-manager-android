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
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TournamentDAOTest {

    @Test
    public void testInsertUpdateDelete() throws Exception {
        DTournament tour = new DTournament(1, "TOURNAME", new Date(), new Date(), "TOURNOTE");
        tour.setCompetitionId(1);
        long newId = DAOFactory.getInstance().tournamentDAO.insert(RuntimeEnvironment.application, tour);
        tour.setId(newId);
        DTournament ret = DAOFactory.getInstance().tournamentDAO.getById(RuntimeEnvironment.application, newId);

        assertTrue(tour.getId() == ret.getId());
        assertTrue(tour.getCompetitionId() == ret.getCompetitionId());
        assertTrue(tour.getNote().equals(ret.getNote()));
        assertTrue(tour.getName().equals(ret.getName()));

        tour.setName("NEWNAME");
        tour.setNote("NEWNOTE");

        DAOFactory.getInstance().tournamentDAO.update(RuntimeEnvironment.application, tour);
        ret = DAOFactory.getInstance().tournamentDAO.getById(RuntimeEnvironment.application, newId);

        assertTrue(tour.getId() == ret.getId());
        assertTrue(tour.getCompetitionId() == ret.getCompetitionId());
        assertTrue(tour.getNote().equals(ret.getNote()));
        assertTrue(tour.getName().equals(ret.getName()));

        DAOFactory.getInstance().tournamentDAO.delete(RuntimeEnvironment.application, newId);
        ret = DAOFactory.getInstance().tournamentDAO.getById(RuntimeEnvironment.application, newId);

        assertNull(ret);
    }

    @Test
    public void testGetByCompId() throws Exception {
        DTournament tour = new DTournament(1, "TOURNAME", new Date(), new Date(), "TOURNOTE");
        tour.setCompetitionId(1);
        DTournament tour2 = new DTournament(1, "TOURNAME2", new Date(), new Date(), "TOURNOTE2");
        tour2.setCompetitionId(1);

        DAOFactory.getInstance().tournamentDAO.insert(RuntimeEnvironment.application, tour);
        DAOFactory.getInstance().tournamentDAO.insert(RuntimeEnvironment.application, tour2);

        ArrayList<DTournament> ret = DAOFactory.getInstance().tournamentDAO.getByCompetitionId(RuntimeEnvironment.application, 1);

        assertTrue( ret.size() == 2 );
    }
}