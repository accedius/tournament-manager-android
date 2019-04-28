package fit.cvut.org.cz.bowling.business.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.generators.BalancedTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager {
    @Override
    public boolean generateRosters(long competitionId, long tournamentId, int generatingType) {
        List<Player> players = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getTournamentPlayers(tournamentId);
        List<Team> teams = getByTournamentId(tournamentId);
        List<AggregatedStatistics> stats = ((IStatisticManager)managerFactory.getEntityManager(AggregatedStatistics.class)).getByCompetitionId(competitionId);
        Random r = new Random(System.currentTimeMillis());

        HashMap<Long, Player> playersHashMap = new HashMap<>();
        for (Player p : players)
            playersHashMap.put(p.getId(), p);

        HashMap<Long, Double> statsHashMap = new HashMap<>();
        for (AggregatedStatistics s : stats) {
            if (generatingType == TournamentService.GENERATE_BY_TEAM_POINTS) {
                statsHashMap.put(s.getPlayerId(), s.getAvgTeamPoints());
            } else if (generatingType == TournamentService.GENERATE_BY_WINS) {
                statsHashMap.put(s.getPlayerId(), s.getAvgWins());
            } else if (generatingType == TournamentService.GENERATE_BY_GOALS) {
                statsHashMap.put(s.getPlayerId(), s.getAvgGoals());
            } else if (generatingType == TournamentService.GENERATE_RANDOMLY) {
                statsHashMap.put(s.getPlayerId(), r.nextDouble());
            }
        }

        ITeamsRostersGenerator teamsRostersGenerator = new BalancedTeamsRostersGenerator();
        boolean res = teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ((ITeamManager)managerFactory.getEntityManager(Team.class)).updatePlayersInTeam(t.getId(), t.getPlayers());

        return res;
    }
}
