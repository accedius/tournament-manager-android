package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.squash.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    protected SquashDBHelper sportDBHelper;

    public PointConfigurationManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<PointConfiguration, Long> getDao() {
        return DatabaseFactory.getDBHelper(context).getSquashPointConfigurationDAO();
    }
}
