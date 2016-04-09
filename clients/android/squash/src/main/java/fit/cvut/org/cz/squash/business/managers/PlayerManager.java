package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerManager implements IPackagePlayerManager {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {

    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {

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
        //TODO implement this mock so far

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0, "peter", "peter@douche.cz", null));
        players.add(new Player(0, "Mike", "mike@mike.cz", null));
        players.add(new Player(0, "Ike", "ike@spike.cz", null));

        return players;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        return null;
    }

    @Override
    public ArrayList<Player> getPlayersByMatch(Context context, long matchId) {
        return null;
    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        return null;
    }
}
