package fit.cvut.org.cz.tmlibrary.business.managers;

import fit.cvut.org.cz.tmlibrary.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 4.12.2016.
 */
public abstract class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    public PointConfigurationManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }
}
