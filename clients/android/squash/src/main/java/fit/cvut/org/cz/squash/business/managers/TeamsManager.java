package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.RoundRobinTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamsManager implements ITeamManager {
    @Override
    public void insert(Context context, Team team) {
        DAOFactory.getInstance().teamDAO.insert(context, Team.convertToDTeam(team));
    }

    @Override
    public void update(Context context, Team team) {
        DAOFactory.getInstance().teamDAO.update(context, Team.convertToDTeam(team));
    }

    @Override
    public boolean delete(Context context, long id) {
        Team t = getById(context, id);
        if (t.getPlayers().size() != 0) return false;
        if (DAOFactory.getInstance().participantDAO.getParticipantsByTeamId(context, id).size() != 0) return false;
        DAOFactory.getInstance().teamDAO.delete(context, id);
        return true;
    }

    @Override
    public Team getById(Context context, long id) {
        Team t = new Team(DAOFactory.getInstance().teamDAO.getById(context, id));
        t.setPlayers(ManagersFactory.getInstance().playerManager.getPlayersByTeam(context, t.getId()));
        return t;
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {
        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<DTeam> dTeams = DAOFactory.getInstance().teamDAO.getByTournamentId(context, tournamentId);
        for (DTeam dTeam : dTeams) {
            Team t = new Team(dTeam);
            t.setPlayers(ManagersFactory.getInstance().playerManager.getPlayersByTeam(context, t.getId()));
            teams.add(t);
        }

        return teams;
    }

    @Override
    public void generateRosters(Context context, long competitionId, long tournamentId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
        ArrayList<SAggregatedStats> stats = ManagersFactory.getInstance().statsManager.getAggregatedStatsByCompetitionId(context, competitionId);

        HashMap<Long, Player> playersHashMap = new HashMap<>();
        for (Player p : players)
            playersHashMap.put(p.getId(), p);

        HashMap<Long, Double> statsHashMap = new HashMap<>();
        for(SAggregatedStats s : stats)
            statsHashMap.put(s.playerId, (double)s.points);

        ITeamsRostersGenerator teamsRostersGenerator = new RoundRobinTeamsRostersGenerator();
        teamsRostersGenerator.generateRosters(teams, playersHashMap, statsHashMap);

        for (Team t : teams)
            ManagersFactory.getInstance().playerManager.updatePlayersInTeam( context, t.getId(), t.getPlayers());
    }
}
