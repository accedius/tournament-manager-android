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
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.generators.BalancedTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager {
    protected HockeyDBHelper sportDBHelper;

    public TeamManager(Context context, ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
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
        List<AggregatedStatistics> stats = ManagerFactory.getInstance(context).statisticsManager.getByCompetitionId(competitionId);
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

        ITeamsRostersGenerator teamsRostersGenerator = new BalancedTeamsRostersGenerator();
        boolean res = teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ManagerFactory.getInstance(context).teamManager.updatePlayersInTeam(t.getId(), t.getPlayers());

        return res;
    }
}
