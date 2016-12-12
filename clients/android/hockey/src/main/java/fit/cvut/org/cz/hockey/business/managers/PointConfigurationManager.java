package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.business.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 4.12.2016.
 */
public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    protected HockeyDBHelper sportDBHelper;

    public PointConfigurationManager(ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<PointConfiguration, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getHockeyPointConfigurationDAO();
    }
}
