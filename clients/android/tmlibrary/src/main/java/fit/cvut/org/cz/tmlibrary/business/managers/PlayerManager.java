package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class PlayerManager extends TManager<Player> implements IPlayerManager {
    @Override
    protected Class<Player> getMyClass() {
        return Player.class;
    }
}