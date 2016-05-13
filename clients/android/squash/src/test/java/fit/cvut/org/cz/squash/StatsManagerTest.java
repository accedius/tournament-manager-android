package fit.cvut.org.cz.squash;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.PlayerManager;
import fit.cvut.org.cz.squash.business.managers.TeamsManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.daos.MatchDAO;
import fit.cvut.org.cz.squash.data.daos.ParticipantDAO;
import fit.cvut.org.cz.squash.data.daos.PlayerDAO;
import fit.cvut.org.cz.squash.data.daos.StatDAO;
import fit.cvut.org.cz.squash.data.daos.TournamentDAO;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.squash.data.interfaces.IStatDAO;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vaclav on 13. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StatsManagerTest {

    @Mock
    IPackagePlayerManager mockPlayerManager;
    @Mock
    IStatDAO mockStatsDAO;
    @Mock
    IParticipantDAO mockParticipantDAO;
    @Mock
    IMatchDAO mockMatchDAO;
    @Mock
    ITournamentDAO mockTournamentDAO;
    @Mock
    IPackagePlayerDAO mockPlayerDAO;
    @Mock
    ITeamManager mockTeamsManager;

    @Before
    public void setUp() throws Exception {

        mockPlayerManager = Mockito.mock(IPackagePlayerManager.class);
        mockStatsDAO = Mockito.mock(IStatDAO.class);
        mockParticipantDAO = Mockito.mock(IParticipantDAO.class);
        mockMatchDAO = Mockito.mock(IMatchDAO.class);
        mockTournamentDAO = Mockito.mock(ITournamentDAO.class);
        mockPlayerDAO = Mockito.mock(IPackagePlayerDAO.class);
        mockTeamsManager = Mockito.mock(ITeamManager.class);

        ManagersFactory.getInstance().playerManager = mockPlayerManager;
        ManagersFactory.getInstance().teamsManager = mockTeamsManager;
        DAOFactory.getInstance().statDAO = mockStatsDAO;
        DAOFactory.getInstance().participantDAO = mockParticipantDAO;
        DAOFactory.getInstance().matchDAO = mockMatchDAO;
        DAOFactory.getInstance().tournamentDAO = mockTournamentDAO;
        DAOFactory.getInstance().playerDAO = mockPlayerDAO;

    }

    private void prepData(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0, "A", null, null));
        players.add(new Player(1, "B", null, null));
        players.add(new Player(2, "C", null, null));
        when(mockPlayerManager.getPlayersByCompetition(RuntimeEnvironment.application, 0)).thenReturn(players);

        ArrayList<DStat> matches = new ArrayList<>();
        matches.add(new DStat(0, 0, 0, 0, 0, 1, -1, -1, StatsEnum.MATCH));
        matches.add(new DStat(0, 0, 0, 0, 1, -1, -1, -1, StatsEnum.MATCH));
        matches.add(new DStat(0, 0, 0, 0, 2, 1, -1, -1, StatsEnum.MATCH));
        matches.add(new DStat(0, 0, 0, 0, 3, -1, -1, -1, StatsEnum.MATCH));
        matches.add(new DStat(0, 0, 0, 0, 4, 1, -1, -1, StatsEnum.MATCH));
        matches.add(new DStat(0, 0, 0, 0, 5, -1, -1, -1, StatsEnum.MATCH));
        when(mockStatsDAO.getByCompetition(RuntimeEnvironment.application, 0, StatsEnum.MATCH)).thenReturn(matches);

        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 0))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[]{0L})));
        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 1))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[] {1L})));
        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 2))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[]{0L})));
        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 3))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[]{2L})));
        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 4))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[]{1L})));
        when(mockStatsDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 5))
                .thenReturn(new ArrayList<Long>(Arrays.asList(new Long[]{2L})));


        ArrayList<DStat> sets = new ArrayList<>();
        sets.add(new DStat(0, 0, 0, 0, 0, 1, 3, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 1, -1, 15, 3, StatsEnum.SET));

        sets.add(new DStat(0, 0, 0, 0, 2, 1, 13, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 3, -1, 15, 13, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 2, -1, 15, 8, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 3, 1, 8, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 2, 1, 9, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 3, -1, 15, 9, StatsEnum.SET));

        sets.add(new DStat(0, 0, 0, 0, 4, 1, 12, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 5, -1, 15, 12, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 4, 1, 13, 15, StatsEnum.SET));
        sets.add(new DStat(0, 0, 0, 0, 5, -1, 15, 13, StatsEnum.SET));

        when(mockStatsDAO.getByCompetition(RuntimeEnvironment.application, 0, StatsEnum.SET)).thenReturn(sets);


    }
    private void assertAgStat(SAggregatedStats stat, String name, int won, int lost, int draws, int setsWon, int setsLost, int ballsWon,
                             int ballsLost, double setsWonAvg, double setsLostAvg, double ballsWonAvg, double ballsLostAvg, double matchWinRate,
                             double setsWinRare){
        assertEquals(name, stat.playerName);
        assertEquals(won, stat.won);
        assertEquals(lost, stat.lost);
        assertEquals(draws, stat.draws);
        assertEquals(setsWon, stat.setsWon);
        assertEquals(setsLost, stat.setsLost);
        assertEquals(ballsWon, stat.ballsWon);
        assertEquals(ballsLost, stat.ballsLost);
        assertEquals(setsWonAvg, stat.setsWonAvg, 0.001d);
        assertEquals(setsLostAvg, stat.setsLostAvg, 0.001d);
        assertEquals(ballsWonAvg, stat.ballsWonAvg, 0.001d);
        assertEquals(ballsLostAvg, stat.ballsLostAvg, 0.001d);
        assertEquals(matchWinRate, stat.matchWinRate, 0.001d);
        assertEquals(setsWinRare, stat.setsWinRate, 0.001d);

    }

    @Test
    public void testGetAggregatedStats() throws Exception {

        prepData();

        ArrayList<SAggregatedStats> stats = ManagersFactory.getInstance().statsManager.getAggregatedStatsByCompetitionId(RuntimeEnvironment.application, 0);
        stats.size();
        assertTrue(stats.size() == 3);
        assertAgStat(stats.get(0), "A", 2, 0, 0, 3, 1, 53, 40, (double) 3 / 2, (double) 1 / 2, (double) 53 / 2, (double) 40 / 2, (double) 2 * 100 / 2, (double) 3 * 100 / 4);
        assertAgStat(stats.get(1), "B", 1, 1, 0, 2, 1, 33, 40, (double) 2 / 2, (double) 1 / 2, (double) 33 / 2, (double) 40 / 2, (double) 1 * 100 / 2, (double) (2.0 * 100 / 3));
        assertAgStat(stats.get(2), "C", 0, 2, 0, 1, 4, 62, 68, (double)1/2 , (double)4/2, (double)62/2, (double)68/2, (double)0/2, (double)1*100/5);
    }

    private void prepSetsForMatch(){

        ArrayList<DParticipant> participants = new ArrayList<>();
        participants.add(new DParticipant(1, 1, 1, "home"));
        participants.add(new DParticipant(2, 2, 1, "away"));
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 1)).thenReturn(participants);

        ArrayList<DStat> homeSets = new ArrayList<>();
        homeSets.add(new DStat(0, 0, 0, 0, 1, 1, 3, 15, StatsEnum.SET));
        homeSets.add(new DStat(0, 0, 0, 0, 1, -1, 15, 9, StatsEnum.SET));
        homeSets.add(new DStat(0, 0, 0, 0, 1, 1, 13, 15, StatsEnum.SET));
        when(mockStatsDAO.getByParticipant(RuntimeEnvironment.application, 1, StatsEnum.SET)).thenReturn(homeSets);

        ArrayList<DStat> awaySets = new ArrayList<>();
        awaySets.add(new DStat(0, 0, 0, 0, 2, -1, 15, 3, StatsEnum.SET));
        awaySets.add(new DStat(0, 0, 0, 0, 2, 1, 9, 15, StatsEnum.SET));
        awaySets.add(new DStat(0, 0, 0, 0, 2, -1, 15, 13, StatsEnum.SET));
        when(mockStatsDAO.getByParticipant(RuntimeEnvironment.application, 2, StatsEnum.SET)).thenReturn(awaySets);

    }
    private void assertSets(SetRowItem item, int hs, int as, int w){
        assertEquals(hs, item.getHomeScore());
        assertEquals(as, item.getAwayScore());
        assertEquals(w, item.getWinner());
    }

    @Test
    public void testGetSetsForMatch() throws Exception {

        prepSetsForMatch();
        ArrayList<SetRowItem> sets = ManagersFactory.getInstance().statsManager.getSetsForMatch(RuntimeEnvironment.application, 1);
        assertEquals(3, sets.size());
        assertSets(sets.get(0), 15, 3, 1);
        assertSets(sets.get(1), 9, 15, -1);
        assertSets(sets.get(2), 15, 13, 1);

    }

    private void assertStat(DStat stat, long partipId, int status, int lostValue, int value, StatsEnum type){
        assertEquals(partipId, stat.getParticipantId());
        assertEquals(status, stat.getStatus());
        assertEquals(lostValue, stat.getLostValue());
        assertEquals(value, stat.getValue());
        assertEquals(type, stat.getType());
    }
    @Test
    public void testUpdateStatsForMatch() throws Exception {

        ArrayList<SetRowItem> sets = new ArrayList<>();
        SetRowItem item = new SetRowItem();
        item.setHomeScore(15);
        item.setAwayScore(9);
        item.setWinner(1);
        sets.add(item);
        item = new SetRowItem();
        item.setHomeScore(12);
        item.setAwayScore(15);
        item.setWinner(-1);
        sets.add(item);
        item = new SetRowItem();
        item.setHomeScore(15);
        item.setAwayScore(13);
        item.setWinner(1);
        sets.add(item);
        DMatch m = new DMatch(1, 1, 1, 1, null, null, false);
        when(mockMatchDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(m);

        ArrayList<DParticipant> participants = new ArrayList<>();
        participants.add(new DParticipant(1, 1, 1, "home"));
        participants.add(new DParticipant(2, 2, 1, "away"));
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 1)).thenReturn(participants);

        when(mockTournamentDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(new DTournament(1, "naaem", null, null, null, null, null, null, null, 1));

        ArgumentCaptor<DMatch> captor = ArgumentCaptor.forClass(DMatch.class);
        ArgumentCaptor<DStat> statCaptor = ArgumentCaptor.forClass(DStat.class);
        ManagersFactory.getInstance().statsManager.updateStatsForMatch(RuntimeEnvironment.application, 1, sets);
        verify(mockMatchDAO).update(any(RuntimeEnvironment.application.getClass()), captor.capture());
        assertEquals(true, captor.getValue().isPlayed());
        verify(mockStatsDAO).delete(any(RuntimeEnvironment.application.getClass()), eq(1L), eq(StatsEnum.SET));
        verify(mockStatsDAO).delete(any(RuntimeEnvironment.application.getClass()), eq(1L), eq(StatsEnum.MATCH));
        verify(mockStatsDAO).delete(any(RuntimeEnvironment.application.getClass()), eq(2L), eq(StatsEnum.SET));
        verify(mockStatsDAO).delete(any(RuntimeEnvironment.application.getClass()), eq(2L), eq(StatsEnum.MATCH));
        verify(mockStatsDAO, times(8)).insert(any(RuntimeEnvironment.application.getClass()), statCaptor.capture());

        List<DStat> stats = statCaptor.getAllValues();
        DStat stat = stats.get(0);
        assertEquals(1, stat.getTournamentId());
        assertEquals(1, stat.getCompetitionId());
        assertStat(stats.get(0), 1, 1, 9, 15, StatsEnum.SET);
        assertStat(stats.get(1), 2, -1, 15, 9, StatsEnum.SET);
        assertStat(stats.get(2), 1, -1, 15, 12, StatsEnum.SET);
        assertStat(stats.get(3), 2, 1, 12, 15, StatsEnum.SET);
        assertStat(stats.get(4), 1, 1, 13, 15, StatsEnum.SET);
        assertStat(stats.get(5), 2, -1, 15, 13, StatsEnum.SET);
        assertStat(stats.get(6), 1, 1, -1, -1, StatsEnum.MATCH);
        assertStat(stats.get(7), 2, -1, -1, -1, StatsEnum.MATCH);

    }

    @Test
    public void testGetPlayersForMatch() throws Exception {

        when(mockMatchDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(new DMatch(1, 1, 1, 1, null, null, false));
        when(mockMatchDAO.getById(RuntimeEnvironment.application, 2)).thenReturn(new DMatch(2, 1, 1, 1, null, null, true));
        ArrayList<DParticipant> p = new ArrayList<>();
        p.add(new DParticipant(1, 1, 1, "home"));
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 1)).thenReturn(p);
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 2)).thenReturn(p);
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "A", null, null));
        players.add(new Player(2, "B", null, null));
        players.add(new Player(3, "C", null, null));
        Team t = new Team(0, null);
        t.setPlayers(players);
        when(mockTeamsManager.getById(RuntimeEnvironment.application, 1)).thenReturn(t);

        ArrayList<Player> notPlayed = ManagersFactory.getInstance().statsManager.getPlayersForMatch(RuntimeEnvironment.application, 1, "home");
        assertEquals("A", notPlayed.get(0).getName());
        assertEquals("B", notPlayed.get(1).getName());
        assertEquals("C", notPlayed.get(2).getName());

        Map<Long, DPlayer> allPlayers = new HashMap<>();
        allPlayers.put(0L, new DPlayer(0, "A", null, null));
        allPlayers.put(1L, new DPlayer(1, "B", null, null));
        allPlayers.put(2L, new DPlayer(2, "C", null, null));
        when(mockPlayerDAO.getAllPlayers(RuntimeEnvironment.application)).thenReturn(allPlayers);
        ArrayList<DStat> stats = new ArrayList<>();
        stats.add(new DStat(0, 0, 0, 0, 1, 1, -1, -1, StatsEnum.MATCH_PARTICIPATION));
        stats.add(new DStat(0, 0, 0, 2, 1, 1, -1, -1, StatsEnum.MATCH_PARTICIPATION));
        when(mockStatsDAO.getByParticipant(RuntimeEnvironment.application, 1, StatsEnum.MATCH_PARTICIPATION)).thenReturn(stats);
        ArrayList<Player> played = ManagersFactory.getInstance().statsManager.getPlayersForMatch(RuntimeEnvironment.application, 2, "home");
        assertEquals("A", played.get(0).getName());
        assertEquals("C", played.get(1).getName());

    }

    @After
    public void tearDown() throws Exception {
        ManagersFactory.getInstance().playerManager = new PlayerManager();
        ManagersFactory.getInstance().teamsManager = new TeamsManager();
        DAOFactory.getInstance().statDAO = new StatDAO();
        DAOFactory.getInstance().participantDAO = new ParticipantDAO();
        DAOFactory.getInstance().matchDAO = new MatchDAO();
        DAOFactory.getInstance().tournamentDAO = new TournamentDAO();
        DAOFactory.getInstance().playerDAO = new PlayerDAO();
    }
}
