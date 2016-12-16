package fit.cvut.org.cz.squash.business.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.generators.BalancedTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamManager extends fit.cvut.org.cz.tmlibrary.business.managers.TeamManager implements ITeamManager {
    @Override
    public boolean generateRosters(long competitionId, long tournamentId, int generatingType) {
        List<Player> players = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getTournamentPlayers(tournamentId);
        List<Team> teams = getByTournamentId(tournamentId);
        List<SAggregatedStats> stats = ((IStatisticManager)managerFactory.getEntityManager(SAggregatedStats.class)).getByCompetitionId(competitionId);
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

        ITeamsRostersGenerator teamsRostersGenerator = new BalancedTeamsRostersGenerator();
        boolean res = teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ((ITeamManager)managerFactory.getEntityManager(Team.class)).updatePlayersInTeam(t.getId(), t.getPlayers());

        return res;
    }
}
