package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.generators.RoundRobinTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamManager implements ITeamManager {
    @Override
    public void insert(Context context, Team team) {
        DTeam dt = Team.convertToDTeam(team);
        DAOFactory.getInstance().teamDAO.insert(context, dt);
    }

    @Override
    public void update(Context context, Team team) {
        DTeam dt = Team.convertToDTeam(team);
        DAOFactory.getInstance().teamDAO.update(context, dt);
    }

    @Override
    public boolean delete(Context context, long id) {
        Team t = getById(context, id);
        ArrayList<ScoredMatch> matches = ManagerFactory.getInstance().matchManager.getByTournamentId(context, t.getTournamentId());
        for (ScoredMatch match : matches) {
            if (match.getHomeParticipantId() == id || match.getAwayParticipantId() == id) return false;
        }

        DAOFactory.getInstance().packagePlayerDAO.deleteAllPlayersFromTeam(context, id);
        DAOFactory.getInstance().teamDAO.delete(context, id);
        return true;
    }

    @Override
    public Team getById(Context context, long id) {
        DTeam dt = DAOFactory.getInstance().teamDAO.getById(context, id);
        Team t = new Team(dt);
        t.setPlayers(ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam(context, t.getId()));
        return t;
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {
        ArrayList<DTeam> dts = DAOFactory.getInstance().teamDAO.getByTournamentId(context, tournamentId);
        ArrayList<Team> ts = new ArrayList<>();

        for (DTeam i : dts) {
            Team t = new Team(i);
            t.setPlayers(ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam(context, t.getId()));
            ts.add(t);
        }

        return ts;
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
