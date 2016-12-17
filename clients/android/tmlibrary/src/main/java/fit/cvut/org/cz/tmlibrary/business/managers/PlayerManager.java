package fit.cvut.org.cz.tmlibrary.business.managers;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager extends TManager<Player> implements IPlayerManager {
    @Override
    protected Class<Player> getMyClass() {
        return Player.class;
    }
}