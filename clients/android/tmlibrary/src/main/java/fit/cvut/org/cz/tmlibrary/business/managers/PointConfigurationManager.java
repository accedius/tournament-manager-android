package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 4.12.2016.
 */
public abstract class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    public PointConfigurationManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }
}
