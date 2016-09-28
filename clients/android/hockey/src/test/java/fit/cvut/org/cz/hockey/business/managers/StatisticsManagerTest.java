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
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.data.DAO.MatchDAO;
import fit.cvut.org.cz.hockey.data.DAO.MatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.DAO.ParticipantDAO;
import fit.cvut.org.cz.hockey.data.DAO.PointConfigDAO;
import fit.cvut.org.cz.hockey.data.DAO.StatDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;
import fit.cvut.org.cz.hockey.data.interfaces.IMatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IStatDAO;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class StatisticsManagerTest {
    @Mock
    IPackagePlayerManager mockPlayerManager;

    @Mock
    IStatDAO mockStatDAO;

    @Mock
    IMatchDAO mockMatchDAO;

    @Mock
    IPointConfigDAO mockPointConfigDAO;

    @Mock
    IMatchStatisticsDAO mockMatchStatisticsDAO;

    @Mock
    IParticipantDAO mockParticipantDAO;

    @Mock
    ITeamManager mockTeamManager;

    @Mock
    IScoredMatchManager mockMatchManager;

    ArrayList<Player> players = new ArrayList<>();
    ArrayList<DStat> allStats = new ArrayList<>();
    ArrayList<Team> teams = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        mockPlayerManager = Mockito.mock(IPackagePlayerManager.class);
        mockStatDAO = Mockito.mock(IStatDAO.class);
        mockMatchDAO = Mockito.mock(IMatchDAO.class);
        mockPointConfigDAO = Mockito.mock(IPointConfigDAO.class);
        mockMatchStatisticsDAO = Mockito.mock(IMatchStatisticsDAO.class);
        mockParticipantDAO = Mockito.mock(IParticipantDAO.class);
        mockTeamManager = Mockito.mock(ITeamManager.class);
        mockMatchManager = Mockito.mock(IScoredMatchManager.class);

        ManagerFactory.getInstance().packagePlayerManager = mockPlayerManager;
        ManagerFactory.getInstance().teamManager = mockTeamManager;
        ManagerFactory.getInstance().matchManager = mockMatchManager;
        DAOFactory.getInstance().statDAO = mockStatDAO;
        DAOFactory.getInstance().matchDAO = mockMatchDAO;
        DAOFactory.getInstance().pointConfigDAO = mockPointConfigDAO;
        DAOFactory.getInstance().matchStatisticsDAO = mockMatchStatisticsDAO;
        DAOFactory.getInstance().participantDAO = mockParticipantDAO;

        players.add(new Player(1, "Pepa", "pepuvemail", "note"));
        players.add(new Player(2, "Jan", "honzuvemail", "note"));
        players.add(new Player(3, "Franta", "frantuvemail", "note"));

        allStats.add(new DStat(1, 1, 1, StatsEnum.goals.toString(), 1, 1, "1"));
        allStats.add(new DStat(1, 1, 1, StatsEnum.assists.toString(), 1, 1, "2"));
        allStats.add(new DStat(1, 1, 1, StatsEnum.saves.toString(), 1, 1, "3"));
        allStats.add(new DStat(1, 1, 1, StatsEnum.plus_minus_points.toString(), 1, 1, "4"));
        allStats.add(new DStat(1, 1, 1, StatsEnum.participates.toString(), 1, 1, "5"));

        allStats.add(new DStat(1, 2, 1, StatsEnum.goals.toString(), 1, 1, "6"));
        allStats.add(new DStat(1, 2, 1, StatsEnum.assists.toString(), 1, 1, "7"));
        allStats.add(new DStat(1, 2, 1, StatsEnum.saves.toString(), 1, 1, "8"));
        allStats.add(new DStat(1, 2, 1, StatsEnum.plus_minus_points.toString(), 1, 1, "9"));
        allStats.add(new DStat(1, 2, 1, StatsEnum.participates.toString(), 1, 1, "10"));

        allStats.add(new DStat(1, 3, 1, StatsEnum.goals.toString(), 1, 1, "11"));
        allStats.add(new DStat(1, 3, 1, StatsEnum.assists.toString(), 1, 1, "12"));
        allStats.add(new DStat(1, 3, 1, StatsEnum.saves.toString(), 1, 1, "13"));
        allStats.add(new DStat(1, 3, 1, StatsEnum.plus_minus_points.toString(), 1, 1, "14"));
        allStats.add(new DStat(1, 3, 1, StatsEnum.participates.toString(), 1, 1, "15"));

        teams.add(new Team(1, "TeamName1"));
        teams.get(0).setId(1);
        teams.add(new Team(2, "TeamName2"));
        teams.get(1).setId(2);

        when(mockPlayerManager.getAllPlayers(RuntimeEnvironment.application)).thenReturn(players);
    }

    private boolean testStats(AggregatedStatistics stats, long goals, long assists, long points, long plusmp, long matches, long saves){
        if (stats.getGoals() != goals) return false;
        if (stats.getAssists() != assists) return false;
        if (stats.getPoints() != points) return false;
        if (stats.getPlusMinusPoints() != plusmp) return false;
        if (stats.getMatches() != matches) return false;
        if (stats.getSaves() != saves) return false;
        return true;
    }

    private AggregatedStatistics getAgsById(long id, ArrayList<AggregatedStatistics> ags){
        for (AggregatedStatistics ag : ags) {
            if (ag.getPlayerID() == id) return ag;
        }
        return null;
    }

    private ArrayList<DStat> getPlayerStats(long id, ArrayList<DStat> stats){
        ArrayList<DStat> res = new ArrayList<>();
        for (DStat st : stats) {
            if (st.getPlayerId() == id) res.add(st);
        }
        return res;
    }

    @Test
    public void testGetAllAggregated() throws Exception {
        when(mockPlayerManager.getAllPlayers(RuntimeEnvironment.application)).thenReturn(players);
        ArrayList<DStat> playerStat;
        playerStat = getPlayerStats(1, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(2, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 2)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(3, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 3)).thenReturn(new ArrayList<DStat>(playerStat));
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getAllAggregated(RuntimeEnvironment.application);

        assertTrue(stats.size() == 3);
        assertTrue(testStats(getAgsById(1, stats), 1, 2, 3, 4, 1, 3));
        assertTrue(testStats(getAgsById(2, stats), 6, 7, 13, 9, 1, 8));
        assertTrue(testStats(getAgsById(3, stats), 11, 12, 23, 14, 1, 13));
        verify(mockStatDAO, times(0)).getStatsByCompetitionId(any(Context.class), anyLong());
        verify(mockStatDAO, times(0)).getStatsByTournamentId(any(Context.class), anyLong());
    }

    @Test
    public void testGetByCompId() throws Exception {
        when(mockPlayerManager.getPlayersByCompetition(RuntimeEnvironment.application, 1)).thenReturn(players);
        when(mockStatDAO.getStatsByCompetitionId(RuntimeEnvironment.application, 1)).thenReturn(allStats);
        ArrayList<DStat> playerStat;
        playerStat = getPlayerStats(1, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(2, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 2)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(3, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 3)).thenReturn(new ArrayList<DStat>(playerStat));
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByCompetitionID(RuntimeEnvironment.application, 1);

        assertTrue(stats.size() == 3);
        assertTrue(testStats(getAgsById(1, stats), 1, 2, 3, 4, 1, 3));
        assertTrue(testStats(getAgsById(2, stats), 6, 7, 13, 9, 1, 8));
        assertTrue(testStats(getAgsById(3, stats), 11, 12, 23, 14, 1, 13));
    }

    @Test
    public void testGetByTourId() throws Exception {
        when(mockPlayerManager.getPlayersByTournament(RuntimeEnvironment.application, 1)).thenReturn(players);
        when(mockStatDAO.getStatsByTournamentId(RuntimeEnvironment.application, 1)).thenReturn(allStats);
        ArrayList<DStat> playerStat;
        playerStat = getPlayerStats(1, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(2, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 2)).thenReturn(new ArrayList<DStat>(playerStat));
        playerStat.clear();
        playerStat = getPlayerStats(3, allStats);
        when(mockStatDAO.getStatsByPlayerId(RuntimeEnvironment.application, 3)).thenReturn(new ArrayList<DStat>(playerStat));
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByTournamentID(RuntimeEnvironment.application, 1);

        assertTrue(stats.size() == 3);
        assertTrue(testStats(getAgsById(1, stats), 1, 2, 3, 4, 1, 3));
        assertTrue(testStats(getAgsById(2, stats), 6, 7, 13, 9, 1, 8));
        assertTrue(testStats(getAgsById(3, stats), 11, 12, 23, 14, 1, 13));
    }

    @Test
    public void testGetStandings() throws Exception {
        when(mockTeamManager.getByTournamentId(RuntimeEnvironment.application, 1)).thenReturn(teams);
        when(mockPointConfigDAO.getByTournamentId(RuntimeEnvironment.application, 1L)).thenReturn(new DPointConfiguration(1L, 1L, 0L, 1L, 1L, 0L, 1L, 0L));
        ArrayList<DMatch> matches = new ArrayList<>();
        matches.add(new DMatch(1, 1, 1, 1, new Date(), "note", true));
        matches.add(new DMatch(1, 1, 1, 1, new Date(), "note", true));
        when(mockMatchDAO.getByTournamentId(RuntimeEnvironment.application, 1)).thenReturn(matches);
        ScoredMatch match = new ScoredMatch(new DMatch(1, 1, 1, 1, new Date(), "note", true));
        match.setHomeScore(10);
        match.setAwayScore(2);
        match.setHomeParticipantId(1);
        match.setAwayParticipantId(2);
        when(mockMatchManager.getById(RuntimeEnvironment.application, 1)).thenReturn(match);
        when(mockMatchStatisticsDAO.getByMatchId(any(Context.class), anyLong())).thenReturn(new DMatchStat(1, false, false));

        ArrayList<Standing> res = ManagerFactory.getInstance().statisticsManager.getStandingsByTournamentId(RuntimeEnvironment.application, 1);

        assertNotNull(res);
        assertTrue(res.size() == 2);
        assertTrue(res.get(0).getGoalsGiven() == 20);
        assertTrue(res.get(0).getGoalsReceived() == 4);
        assertTrue(res.get(1).getGoalsGiven() == 4);
        assertTrue(res.get(1).getGoalsReceived() == 20);
        assertTrue(res.get(0).getName().equals("TeamName1"));
        assertTrue(res.get(1).getName().equals("TeamName2"));
        assertTrue(res.get(0).getPoints() == 2);
        assertTrue(res.get(1).getPoints() == 0);
        assertTrue(res.get(1).getWins().equals(res.get(0).getLosses()));
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

    @Test
    public void testSetMatchScore() throws Exception {
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 1)).thenReturn(new ArrayList<DParticipant>());
        ManagerFactory.getInstance().statisticsManager.setMatchScoreByMatchId(RuntimeEnvironment.application, 1, new MatchScore(1, 5, 1, false, false));

        verify(mockMatchStatisticsDAO).update(any(Context.class), any(DMatchStat.class));
        verify(mockStatDAO, times(2)).update(any(Context.class), any(DStat.class));
    }

    @After
    public void tearDown() throws Exception {
        ManagerFactory.getInstance().packagePlayerManager = new PackagePlayerManager();
        ManagerFactory.getInstance().teamManager = new TeamManager();
        ManagerFactory.getInstance().matchManager = new MatchManager();
        DAOFactory.getInstance().statDAO = new StatDAO();
        DAOFactory.getInstance().matchDAO = new MatchDAO();
        DAOFactory.getInstance().pointConfigDAO = new PointConfigDAO();
        DAOFactory.getInstance().matchStatisticsDAO = new MatchStatisticsDAO();
        DAOFactory.getInstance().participantDAO = new ParticipantDAO();
    }
}