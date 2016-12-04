package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 6.11.2016.
 */
public interface ICompetitionManager extends IManager<Competition> {
    /**
     * add player to competition
     * @param context application context
     * @param competition competition where player should be added
     * @param player player to be added
     */
    void addPlayer(Context context, Competition competition, Player player);

    /**
     * get all players in competition
     * @param context application context
     * @param competitionId id of competition
     * @return found players
     */
    List<Player> getCompetitionPlayers(Context context, long competitionId);

    /**
     * get all players not in competition
     * @param context application context
     * @param competitionId id of competition
     * @return found players
     */
    List<Player> getCompetitionPlayersComplement(Context context, long competitionId);

    /**
     * remove player from competition
     * @param context application context
     * @param playerId id of player to be removed
     * @param competitionId id of competition
     * @return true if player was removed, false otherwise
     */
    boolean removePlayerFromCompetition(Context context, long playerId, long competitionId);
}
