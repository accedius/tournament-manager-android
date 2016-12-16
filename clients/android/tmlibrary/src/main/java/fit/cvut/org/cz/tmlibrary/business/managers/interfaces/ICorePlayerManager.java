package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 4.12.2016.
 */
public interface ICorePlayerManager extends IManager<Player> {
    /**
     * Get all players in core.
     * @return map of player_id as a key and player as a value
     */
    Map<Long, Player> getMapAll();
}