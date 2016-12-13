package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.generators.RoundRobinTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager {
    protected SquashDBHelper sportDBHelper;

    public TeamManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Team, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getTeamDAO();
    }

    public List<Player> getTournamentPlayers(long tournamentId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();
        List<Player> res = new ArrayList<>();
        try {
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                res.add(allPlayers.get(tournamentPlayer.getPlayerId()));
            }
            return res;
        }
        catch (SQLException e) {
            return res;
        }
    }
    @Override
    public boolean generateRosters(long competitionId, long tournamentId, int generatingType) {
        List<Player> players = getTournamentPlayers(tournamentId);
        List<Team> teams = getByTournamentId(tournamentId);
        List<SAggregatedStats> stats = ManagerFactory.getInstance(context).statisticManager.getByCompetitionId(competitionId);
        Random r = new Random(System.currentTimeMillis());

        HashMap<Long, Player> playersHashMap = new HashMap<>();
        for (Player p : players)
            playersHashMap.put(p.getId(), p);

        HashMap<Long, Double> statsHashMap = new HashMap<>();
        for (SAggregatedStats s : stats) {
            if (generatingType == TournamentService.GENERATE_BY_TEAM_POINTS) {
                statsHashMap.put(s.playerId, (double) s.points);
            } else if (generatingType == TournamentService.GENERATE_BY_SETS) {
                statsHashMap.put(s.playerId, s.setsWinRate);
            } else if (generatingType == TournamentService.GENERATE_BY_BALLS) {
                statsHashMap.put(s.playerId, s.ballsWonAvg);
            } else if (generatingType == TournamentService.GENERATE_RANDOMLY) {
                statsHashMap.put(s.playerId, r.nextDouble());
            }
        }

        ITeamsRostersGenerator teamsRostersGenerator = new RoundRobinTeamsRostersGenerator();
        boolean res = teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ManagerFactory.getInstance(context).teamManager.updatePlayersInTeam(t.getId(), t.getPlayers());

        return res;
    }
}
