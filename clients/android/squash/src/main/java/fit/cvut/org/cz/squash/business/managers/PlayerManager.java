package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerManager implements IPackagePlayerManager {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        DAOFactory.getInstance().playerDAO.addPlayerToCompetition(context, playerId, competitionId);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        DAOFactory.getInstance().playerDAO.addPlayerToTournament(context, playerId, tournamentId);
    }


    @Override
    public boolean deletePlayerFromCompetition(Context context, long playerId, long competitionId) {

        ArrayList<Tournament> tournaments = ManagersFactory.getInstance().tournamentManager.getByCompetitionId(context,competitionId);
        for (Tournament t : tournaments){
            ArrayList<Long> playerIds = DAOFactory.getInstance().playerDAO.getPlayerIdsByTournament(context, t.getId());
            if (playerIds.contains(playerId)) return false;
        }
        DAOFactory.getInstance().playerDAO.deletePlayerFromCompetition(context, playerId, competitionId);
        return true;
    }

    @Override
    public boolean deletePlayerFromTournament(Context context, long playerId, long tournamentId) {

        Player p = new Player(playerId, null, null, null);
        if (DAOFactory.getInstance().statDAO.getByPlayer(context, playerId, StatsEnum.MATCH_PARTICIPATION).size() != 0) return false;
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersNotInTeams(context, tournamentId);
        if (!players.contains(p)) return false;

        DAOFactory.getInstance().playerDAO.deletePlayerFromTournament(context, playerId, tournamentId);
        return true;
    }


    @Override
    public ArrayList<Player> getPlayersByCompetition(Context context, long competitionId) {
        Map<Long, DPlayer> players = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByCompetition(context, competitionId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(new Player(players.get(id)));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        Map<Long, DPlayer> players = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByTournament(context, tournamentId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(new Player(players.get(id)));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersByParticipant(Context context, long participantId) {

        Map<Long, DPlayer> players = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
        ArrayList<Long> ids = DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, participantId);
        ArrayList<Player> filtered = new ArrayList<>();

        for (long id : ids) filtered.add(new Player(players.get(id)));

        return filtered;
    }

    @Override
    public ArrayList<Player> getPlayersByTeam(Context context, long teamId) {

        Map<Long, DPlayer> players = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByTeam(context, teamId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(new Player(players.get(id)));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        ArrayList<Player> players = new ArrayList<>();
        Map<Long, DPlayer> dPlayers = DAOFactory.getInstance().playerDAO.getAllPlayers(context);

        for (Long key : dPlayers.keySet()) players.add(new Player(dPlayers.get(key)));

        return players;
    }

    @Override
    public ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId) {
        Map<Long, DPlayer> players = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByCompetition(context, competitionId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) players.remove(id);
        for (Long id : players.keySet()) filteredPlayers.add(new Player(players.get(id)));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId) {

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, tournamentId);
        ArrayList<Player> playersInCompetition = getPlayersByCompetition(context, t.getCompetitionId());
        ArrayList<Player> playersInTournament = getPlayersByTournament(context, tournamentId);

        for (Player p : playersInTournament) playersInCompetition.remove(p);

        return playersInCompetition;
    }

    @Override
    public void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players) {

        DAOFactory.getInstance().playerDAO.deleteAllPlayersFromTeam(context, teamId);
        for (Player p : players) DAOFactory.getInstance().playerDAO.addPlayerToTeam(context, p.getId(), teamId);
    }

    @Override
    public ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId) {

        ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
        ArrayList<Player> players = getPlayersByTournament(context, tournamentId);

        for (Team t : teams)
            for (Player p : t.getPlayers())
                players.remove(p);

        return players;
    }

    @Override
    public void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players) {
        DAOFactory.getInstance().statDAO.delete(context, participantId, StatsEnum.MATCH_PARTICIPATION);
        for (Player p : players){
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, competitionId, tournamentId, p.getId(), participantId, 1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
        }
    }

    @Override
    public ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId) {
        return null;
    }
}
