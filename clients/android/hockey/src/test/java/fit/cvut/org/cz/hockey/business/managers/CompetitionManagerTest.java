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
import fit.cvut.org.cz.hockey.data.DAO.CompetitionDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CompetitionManagerTest {

    @Mock
    ICompetitionDAO mockCompDAO;

    @Mock
    ITournamentManager mockTourManager;


    @Before
    public void setUp() throws Exception {
        mockCompDAO = Mockito.mock(ICompetitionDAO.class);
        DAOFactory.getInstance().competitionDAO = mockCompDAO;
        mockTourManager = Mockito.mock(ITournamentManager.class);
        ManagerFactory.getInstance().tournamentManager = mockTourManager;
    }

    @Test
    public void testInsertCalled() throws Exception {
        Competition testComp = new Competition(1, "name", new Date(), new Date(), "note", CompetitionType.Teams);
        ManagerFactory.getInstance().competitionManager.insert(RuntimeEnvironment.application, testComp);
        verify(mockCompDAO).insert(any(Context.class), any(DCompetition.class));
    }

    @Test
    public void testUpdateCalled() throws Exception {
        Competition testComp = new Competition(1, "name", new Date(), new Date(), "note", CompetitionType.Teams);
        ManagerFactory.getInstance().competitionManager.update(RuntimeEnvironment.application, testComp);
        verify(mockCompDAO).update(any(Context.class), any(DCompetition.class));
    }

    @Test
    public void testDeleteCalled() throws Exception {
        when(mockTourManager.getByCompetitionId(RuntimeEnvironment.application, -1)).thenReturn(new ArrayList<Tournament>());
        ManagerFactory.getInstance().competitionManager.delete(RuntimeEnvironment.application, -1);
        verify(mockCompDAO).delete(RuntimeEnvironment.application, -1);
        verify(mockTourManager).getByCompetitionId(RuntimeEnvironment.application, -1);
    }

    @Test
    public void testGetByIdCalled() throws Exception {
        when(mockCompDAO.getById(RuntimeEnvironment.application, 2)).thenReturn(new DCompetition(2, "name", new Date(), new Date(), "note", CompetitionType.Teams.toString()));
        ManagerFactory.getInstance().competitionManager.getById(RuntimeEnvironment.application, 2);
        verify(mockCompDAO).getById(RuntimeEnvironment.application, 2);
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().competitionDAO = new CompetitionDAO();
        ManagerFactory.getInstance().tournamentManager = new TournamentManager();
    }
}