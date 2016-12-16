package fit.cvut.org.cz.tmlibrary.tlib;

import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 14.12.2016.
 */

public interface ICorePlayerManager extends IManager<Player> {
    /**
     * Get all players in core.
     * @return map of player_id as a key and player as a value
     */
    Map<Long, Player> getMapAll();
}

