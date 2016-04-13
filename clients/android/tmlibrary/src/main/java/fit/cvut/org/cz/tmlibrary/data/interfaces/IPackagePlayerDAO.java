package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public interface IPackagePlayerDAO {

    void addPlayerToCompetition(Context context, long playerId, long competitionId);
    void addPlayerToTournament(Context context, long playerId, long tournamentId);
    void addPlayerToMatch(Context context, long playerId, long matchId);

    void deletePlayerFromCompetition(Context context, long playerId, long competitionId);
    void deletePlayerFromTournament(Context context, long playerId, long tournamentId);
    void deletePlayerFromMatch(Context context, long playerId, long matchId);

    ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId);
    ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId);
    ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId);

    Map<Long, DPlayer> getAllPlayers(Context context);


}
