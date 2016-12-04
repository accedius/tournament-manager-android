package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.generators.RoundRobinTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager {

    public TeamManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<Team, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getTeamDAO();
    }

    public List<Player> getTournamentPlayers(Context context, long tournamentId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);
        List<Player> res = new ArrayList<>();
        try {
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .query();
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
    public boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType) {
        List<Player> players = getTournamentPlayers(context, tournamentId);
        List<Team> teams = getByTournamentId(context, tournamentId);
        //List<AggregatedStatistics> stats = getFactory(context).statisticsManager.getByCompetitionID(context, competitionId);
        // TODO how to call statistic manager? :\
        // put StatisticManager as a parameter of function ?
        List<AggregatedStatistics> stats = new ArrayList<>();
        Random r = new Random(System.currentTimeMillis());

        HashMap<Long, Player> playersHashMap = new HashMap<>();
        for (Player p : players)
            playersHashMap.put(p.getId(), p);

        HashMap<Long, Double> statsHashMap = new HashMap<>();
        for (AggregatedStatistics s : stats) {
            if (generatingType == TournamentService.GENERATE_BY_TEAM_POINTS) {
                statsHashMap.put(s.getPlayerID(), s.getAvgTeamPoints());
            } else if (generatingType == TournamentService.GENERATE_BY_WINS) {
                statsHashMap.put(s.getPlayerID(), s.getAvgWins());
            } else if (generatingType == TournamentService.GENERATE_BY_GOALS) {
                statsHashMap.put(s.getPlayerID(), s.getAvgGoals());
            } else if (generatingType == TournamentService.GENERATE_RANDOMLY) {
                statsHashMap.put(s.getPlayerID(), r.nextDouble());
            }
        }

        ITeamsRostersGenerator teamsRostersGenerator = new RoundRobinTeamsRostersGenerator();
        boolean res = teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ManagerFactory.getInstance(context).teamManager.updatePlayersInTeam(context, t.getId(), t.getPlayers());

        return res;
    }
}
