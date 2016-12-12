package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentManager extends IManager<Tournament> {
    /**
     * get all tournaments in competition
     * @param context application context
     * @param competitionId id of the competition
     * @return found tournaments
     */
    List<Tournament> getByCompetitionId(Context context, long competitionId);

    /**
     * get all players in tournament
     * @param context application context
     * @param tournamentId id of tournament
     * @return found players
     */
    List<Player> getTournamentPlayers(Context context, long tournamentId);

    /**
     * get all players not in tournament
     * @param context application context
     * @param tournamentId id of tournament
     * @return found players
     */
    List<Player> getTournamentPlayersComplement(Context context, long tournamentId);

    /**
     * remove player from tournament
     * @param context application context
     * @param playerId id of player to be removed
     * @param tournamentId id of tournament
     * @return true if player was removed, false otherwise
     */
    boolean removePlayerFromTournament(Context context, long playerId, long tournamentId);

    /**
     * add player to tournament
     * @param context application context
     * @param playerId id of player to be added
     * @param tournamentId id of tournament
     */
    void addPlayer(Context context, long playerId, long tournamentId);

    /**
     * get all tournaments for player
     * @param context application context
     * @param playerId id of player
     * @return found tournaments
     */
    List<Tournament> getByPlayer(Context context, long playerId);
}
