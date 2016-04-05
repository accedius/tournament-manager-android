package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public interface IPackagePlayerManager {

    void addPlayerToCompetition(Context context, long playerId, long competitionId);
    void addPlayerToTournament(Context context, long playerId, long tournamentId);
    void addPlayerToMatch(Context context, long playerId, long matchId);

    void deletePlayerFromCompetition(Context context, long playerId, long competitionId);
    void deletePlayerFromTournament(Context context, long playerId, long tournamentId);
    void deletePlayerFromMatch(Context context, long playerId, long matchId);

    ArrayList<Player> getPlayersByCompetition(Context context, long competitionId);
    ArrayList<Player> getPlayersByTournament(Context context, long tournamentId);
    ArrayList<Player> getPlayersByMatch(Context context, long matchId);
    ArrayList<Player> getAllPlayers(Context context);


}
