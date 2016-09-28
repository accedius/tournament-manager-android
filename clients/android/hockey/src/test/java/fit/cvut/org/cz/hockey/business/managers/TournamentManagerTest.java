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

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAO.PointConfigDAO;
import fit.cvut.org.cz.hockey.data.DAO.TournamentDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TournamentManagerTest {
    @Mock
    ITournamentDAO mockTournamentDAO;

    @Mock
    IPointConfigDAO mockPointConfigDAO;

    @Before
    public void setUp() throws Exception {
        mockTournamentDAO = Mockito.mock(ITournamentDAO.class);
        mockPointConfigDAO = Mockito.mock(IPointConfigDAO.class);
        DAOFactory.getInstance().tournamentDAO = mockTournamentDAO;
        DAOFactory.getInstance().pointConfigDAO = mockPointConfigDAO;
    }

    @Test
    public void testInsert() throws Exception {
        when(mockTournamentDAO.insert(any(Context.class), any(DTournament.class))).thenReturn(1L);
        ManagerFactory.getInstance().tournamentManager.insert(RuntimeEnvironment.application, new Tournament(1, 1, "name", new Date(), new Date(), "note"));
        verify(mockPointConfigDAO).insertDefault(RuntimeEnvironment.application, 1L);
    }

    @Test
    public void testUpdate() throws Exception {
        ManagerFactory.getInstance().tournamentManager.update(RuntimeEnvironment.application,new Tournament(1, 1, "name", new Date(), new Date(), "note"));
        verify(mockTournamentDAO).update(any(Context.class), any(DTournament.class));
    }

    @Test
    public void testGetById() throws Exception {
        when(mockTournamentDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(new DTournament(1, "testName", new Date(), new Date(), "testNote"));
        Tournament t = ManagerFactory.getInstance().tournamentManager.getById(RuntimeEnvironment.application, 1);
        verify(mockTournamentDAO).getById(RuntimeEnvironment.application, 1);
        assertTrue(t.getId() == 1);
        assertTrue(t.getNote().equals("testNote"));
        assertTrue(t.getName().equals("testName"));
    }

    @Test
    public void testGetByCompId() throws Exception {
        when(mockTournamentDAO.getByCompetitionId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DTournament>());
        ManagerFactory.getInstance().tournamentManager.getByCompetitionId(RuntimeEnvironment.application, 1);
        verify(mockTournamentDAO).getByCompetitionId(RuntimeEnvironment.application, 1);
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().tournamentDAO = new TournamentDAO();
        DAOFactory.getInstance().pointConfigDAO = new PointConfigDAO();
    }
}