package fit.cvut.org.cz.tmlibrary.business.managers;

import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerStatManager;

/**
 * Created by kevin on 2.12.2016.
 */
abstract public class PlayerStatManager extends TManager<PlayerStat> implements IPlayerStatManager {
    @Override
    protected Class<PlayerStat> getMyClass() {
        return PlayerStat.class;
    }
}
