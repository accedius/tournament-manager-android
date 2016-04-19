package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import fit.cvut.org.cz.squash.business.entities.PointConfig;
import fit.cvut.org.cz.squash.business.interfaces.IPointConfigManager;
import fit.cvut.org.cz.squash.data.DAOFactory;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigManager implements IPointConfigManager {
    @Override
    public void insert(Context context, long id) {
        DAOFactory.getInstance().pointCfgDAO.insert(context, id);
    }

    @Override
    public void update(Context context, PointConfig cfg) {
        DAOFactory.getInstance().pointCfgDAO.update(context, PointConfig.convertToDPointConfig(cfg));
    }

    @Override
    public void delete(Context context, long id) {
        DAOFactory.getInstance().pointCfgDAO.delete(context, id);
    }

    @Override
    public PointConfig getById(Context context, long id) {
        return new PointConfig(DAOFactory.getInstance().pointCfgDAO.getById(context, id));
    }
}
