package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.generators.RoundRobinTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager {

    public TeamManager(IPackagePlayerManager packagePlayerManager) {
        super(packagePlayerManager);
    }

    @Override
    protected Dao<Team, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getTeamDao();
    }

    @Override
    public boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType) {
        ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId(context, tournamentId);
        ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByCompetitionID(context, competitionId);
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
            ManagerFactory.getInstance().packagePlayerManager.updatePlayersInTeam(context, t.getId(), t.getPlayers());

        return res;
    }
}
