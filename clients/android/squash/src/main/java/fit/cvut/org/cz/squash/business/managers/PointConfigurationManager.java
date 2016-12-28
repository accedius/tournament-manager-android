package fit.cvut.org.cz.squash.business.managers;

import fit.cvut.org.cz.squash.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    @Override
    protected Class<PointConfiguration> getMyClass() {
        return PointConfiguration.class;
    }
}
