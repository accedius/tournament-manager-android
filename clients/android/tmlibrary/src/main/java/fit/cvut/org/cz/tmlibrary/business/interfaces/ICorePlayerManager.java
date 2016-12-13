package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 4.12.2016.
 */
public interface ICorePlayerManager {
    /**
     * Get all players in core.
     * @return map of player_id as a key and player as a value
     */
    Map<Long, Player> getAllPlayers();

    /**
     * Get player by id
     * @param playerId id of player
     * @return found player
     */
    Player getPlayerById(long playerId);

    /**
     * Insert player to core.
     * @param player player to be inserted
     */
    void insertPlayer(Player player);

    /**
     * Update player in core.
     * @param player player to be updated
     */
    void updatePlayer(Player player);
}
