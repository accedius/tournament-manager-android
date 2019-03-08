package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Interface for Package Player Manager.
 */
public interface IPackagePlayerManager extends IManager<Player> {
    /**
     * Get all players in core.
     * @return map of player_id as a key and player as a value
     */
    Map<Long, Player> getMapAll();
}