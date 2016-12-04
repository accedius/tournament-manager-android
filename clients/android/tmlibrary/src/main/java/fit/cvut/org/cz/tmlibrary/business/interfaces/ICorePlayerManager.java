package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 4.12.2016.
 */
public interface ICorePlayerManager {
    /**
     * Get all players in core.
     * @param context application context.
     * @return map of player_id as a key and player as a value
     */
    Map<Long, Player> getAllPlayers(Context context);

    /**
     * Get player by id
     * @param context application context.
     * @param playerId id of player
     * @return found player
     */
    Player getPlayerById(Context context, long playerId);

    /**
     * Insert player to core.
     * @param context application context.
     * @param player player to be inserted
     */
    void insertPlayer(Context context, Player player);

    /**
     * Update player in core.
     * @param context application context.
     * @param player player to be updated
     */
    void updatePlayer(Context context, Player player);
}
