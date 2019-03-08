package fit.cvut.org.cz.hockey.business.managers;

import fit.cvut.org.cz.hockey.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;

/**
 * Created by kevin on 4.12.2016.
 */
public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    @Override
    protected Class<PointConfiguration> getMyClass() {
        return PointConfiguration.class;
    }
}
