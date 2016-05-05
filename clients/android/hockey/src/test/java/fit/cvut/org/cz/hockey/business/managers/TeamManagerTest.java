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

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAO.PackagePlayerDAO;
import fit.cvut.org.cz.hockey.data.DAO.TeamDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TeamManagerTest {

    @Mock
    ITeamDAO mockTeamDAO;

    @Mock
    IPackagePlayerDAO mockPackagePlayerDAO;

    @Mock
    IScoredMatchManager mockMatchManager;

    @Mock
    IPackagePlayerManager mockPlayerManager;

    @Before
    public void setUp() throws Exception {
        mockTeamDAO = Mockito.mock(ITeamDAO.class);
        mockPackagePlayerDAO = Mockito.mock(IPackagePlayerDAO.class);
        mockMatchManager = Mockito.mock(IScoredMatchManager.class);
        mockPlayerManager = Mockito.mock(IPackagePlayerManager.class);
        DAOFactory.getInstance().teamDAO = mockTeamDAO;
        DAOFactory.getInstance().packagePlayerDAO = mockPackagePlayerDAO;
        ManagerFactory.getInstance().matchManager = mockMatchManager;
        ManagerFactory.getInstance().packagePlayerManager = mockPlayerManager;
    }

    @Test
    public void testInsertCalled() throws Exception {
        ManagerFactory.getInstance().teamManager.insert(RuntimeEnvironment.application, new Team(1, "name"));
        verify(mockTeamDAO).insert(any(Context.class), any(DTeam.class));
    }

    @Test
    public void testUpdateCalled() throws Exception {
        ManagerFactory.getInstance().teamManager.update(RuntimeEnvironment.application, new Team(1, "name"));
        verify(mockTeamDAO).update(any(Context.class), any(DTeam.class));
    }

    @Test
    public void testDelete() throws Exception {
        when(mockMatchManager.getByTournamentId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<ScoredMatch>());
        when(mockPlayerManager.getPlayersByTeam(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<Player>());
        when(mockTeamDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(new DTeam(1,1,"testName"));
        ManagerFactory.getInstance().teamManager.delete(RuntimeEnvironment.application, 1);
        verify(mockPackagePlayerDAO).deleteAllPlayersFromTeam(RuntimeEnvironment.application, 1);
        verify(mockTeamDAO).delete(RuntimeEnvironment.application, 1);
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().teamDAO = new TeamDAO();
        DAOFactory.getInstance().packagePlayerDAO = new PackagePlayerDAO();
        ManagerFactory.getInstance().matchManager = new MatchManager();
        ManagerFactory.getInstance().packagePlayerManager = new PackagePlayerManager();
    }
}