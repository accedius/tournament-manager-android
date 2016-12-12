package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class PlayerManager extends CoreBaseManager<Player> implements IPlayerManager {
    public PlayerManager(Context context) {
        super(context);
    }
}