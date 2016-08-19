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
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PackagePlayerManagerTest {

    @Mock
    IPackagePlayerDAO mockPlayerDAO;

    @Before
    public void setUp() throws Exception {
        mockPlayerDAO = Mockito.mock(IPackagePlayerDAO.class);
        DAOFactory.getInstance().packagePlayerDAO = mockPlayerDAO;
    }

    @Test
    public void testAddToCompCalled() throws Exception {
        ManagerFactory.getInstance().packagePlayerManager.addPlayerToCompetition(RuntimeEnvironment.application, 1, 1);
        verify(mockPlayerDAO).addPlayerToCompetition(RuntimeEnvironment.application, 1, 1);
    }

    @Test
    public void testAddToTourCalled() throws Exception {
        ManagerFactory.getInstance().packagePlayerManager.addPlayerToTournament(RuntimeEnvironment.application, 1, 1);
        verify(mockPlayerDAO).addPlayerToTournament(RuntimeEnvironment.application, 1, 1);
    }

    @Test
    public void testGetPlayersInComp() throws Exception {
        when(mockPlayerDAO.getPlayerIdsByCompetition(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<Long>());
        ManagerFactory.getInstance().packagePlayerManager.getPlayersByCompetition(RuntimeEnvironment.application, 1);
        verify(mockPlayerDAO).getPlayerIdsByCompetition(RuntimeEnvironment.application, 1);
        verify(mockPlayerDAO, times(0)).getPlayerIdsByTournament(any(Context.class), anyLong());
    }

    @Test
    public void testGetPlayersInTourn() throws Exception {
        when(mockPlayerDAO.getPlayerIdsByTournament(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<Long>());
        ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament(RuntimeEnvironment.application, 1);
        verify(mockPlayerDAO).getPlayerIdsByTournament(RuntimeEnvironment.application, 1);
        verify(mockPlayerDAO, times(0)).getPlayerIdsByCompetition(any(Context.class), anyLong());
    }

    @Test
    public void testGetPlayersInTeam() throws Exception {
        when(mockPlayerDAO.getPlayerIdsByTeam(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<Long>());
        ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam(RuntimeEnvironment.application, 1);
        verify(mockPlayerDAO).getPlayerIdsByTeam(RuntimeEnvironment.application, 1);
    }

    @Test
    public void testUpdatePlayersInTeamCallsAddPlayer() throws Exception {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "name", "email", "note"));
        ManagerFactory.getInstance().packagePlayerManager.updatePlayersInTeam(RuntimeEnvironment.application, 2, players);
        verify(mockPlayerDAO).addPlayerToTeam(RuntimeEnvironment.application, 1, 2);
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().packagePlayerDAO = new PackagePlayerDAO();

    }
}