package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 2.12.2016.
 */
abstract public class PlayerStatManager extends BaseManager<PlayerStat> implements IPlayerStatManager {
    public PlayerStatManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }
}
