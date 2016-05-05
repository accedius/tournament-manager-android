package fit.cvut.org.cz.hockey.business.managers;

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
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.data.DAO.MatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.DAO.StatDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.hockey.data.interfaces.IMatchStatisticsDAO;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IStatDAO;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StatisticsManagerTest {

    @Mock
    IPackagePlayerManager mockPlayerManager;

    @Mock
    IStatDAO mockStatDAO;

    @Mock
    IMatchStatisticsDAO mockMatchStatisticsDAO;

    ArrayList<Player> players = new ArrayList<>();
    ArrayList<DStat> allStats = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        mockPlayerManager = Mockito.mock(IPackagePlayerManager.class);
        mockStatDAO = Mockito.mock(IStatDAO.class);
        mockMatchStatisticsDAO = Mockito.mock(IMatchStatisticsDAO.class);

        ManagerFactory.getInstance().packagePlayerManager = mockPlayerManager;
        DAOFactory.getInstance().statDAO = mockStatDAO;
        DAOFactory.getInstance().matchStatisticsDAO = mockMatchStatisticsDAO;

        players.add(new Player(1, "Pepa", "pepuvemail", "note"));
        players.add(new Player(2, "Jan", "honzuvemail", "note"));
        players.add(new Player(3, "Franta", "frantuvemail", "note"));

        allStats.add(new DStat(1, 1, 1, StatsEnum.goals.toString(), 1, 1, "2"));
        allStats.add(new DStat(1, 2, 1, StatsEnum.goals.toString(), 1, 1, "4"));
        allStats.add(new DStat(1, 3, 1, StatsEnum.goals.toString(), 1, 1, "8"));

        when(mockPlayerManager.getAllPlayers(RuntimeEnvironment.application)).thenReturn(players);
    }

    @Test
    public void testGetByCompId() throws Exception {
        when(mockPlayerManager.getPlayersByCompetition(RuntimeEnvironment.application, 1)).thenReturn(players);
        when(mockStatDAO.getStatsByCompetitionId(RuntimeEnvironment.application, 1)).thenReturn(allStats);
        ArrayList<DStat> playerStat = new ArrayList<>();
        playerStat.add(allStats.get(0));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat.add(allStats.get(1));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 2)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat.add(allStats.get(2));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 3)).thenReturn(new ArrayList<DStat>(playerStat));
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByCompetitionID(RuntimeEnvironment.application, 1);

        assertTrue(stats.size() == 3);
        assertTrue(stats.get(0).getGoals() == 2);
        assertTrue(stats.get(1).getGoals() == 4);
        assertTrue(stats.get(2).getGoals() == 8);

    }

    @Test
    public void testGetByTourId() throws Exception {
        when(mockPlayerManager.getPlayersByTournament(RuntimeEnvironment.application, 1)).thenReturn(players);
        when(mockStatDAO.getStatsByTournamentId(RuntimeEnvironment.application, 1)).thenReturn(allStats);
        ArrayList<DStat> playerStat = new ArrayList<>();
        playerStat.add(allStats.get(0));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat.add(allStats.get(1));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 2)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat.add(allStats.get(2));
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 3)).thenReturn(new ArrayList<DStat>(playerStat));
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByTournamentID(RuntimeEnvironment.application, 1);

        assertTrue(stats.size() == 3);
        assertTrue(stats.get(0).getGoals() == 2);
        assertTrue(stats.get(1).getGoals() == 4);
        assertTrue(stats.get(2).getGoals() == 8);

    }

    @Test
    public void testMatchScoreCallsDAO() throws Exception {
        when(mockMatchStatisticsDAO.getByMatchId(RuntimeEnvironment.application, 1)).thenReturn(new DMatchStat(1, true, true));
        MatchScore score = ManagerFactory.getInstance().statisticsManager.getMatchScoreByMatchId(RuntimeEnvironment.application, 1);
        verify(mockMatchStatisticsDAO).getByMatchId(RuntimeEnvironment.application, 1);
        assertTrue(score.isOvertime());
        assertTrue(score.isShootouts());
        assertTrue(score.getMatchId() == 1);
    }

    @After
    public void tearDown() throws Exception {
        ManagerFactory.getInstance().packagePlayerManager = new PackagePlayerManager();
        DAOFactory.getInstance().statDAO = new StatDAO();
        DAOFactory.getInstance().matchStatisticsDAO = new MatchStatisticsDAO();
    }
}