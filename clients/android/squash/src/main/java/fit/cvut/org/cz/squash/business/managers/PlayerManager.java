package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
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
    public void addPlayerToMatch(Context context, long playerId, long matchId) {

    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {

    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {

    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {

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
    public ArrayList<Player> getPlayersByMatch(Context context, long matchId) {
        return null;
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
}
