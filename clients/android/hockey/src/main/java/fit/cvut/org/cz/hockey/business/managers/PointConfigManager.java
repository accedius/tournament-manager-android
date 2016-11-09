package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.business.interfaces.IPointConfigManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class PointConfigManager implements IPointConfigManager {
    public PointConfiguration getByTournamentId(Context context, Long tournamentId) {
        return getById(context, tournamentId);
    }

    @Override
    public void insert(Context context, PointConfiguration pointConfiguration) {
        // TODO check if id is filled
        try {
            getDao(context).create(pointConfiguration);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, PointConfiguration pointConfiguration) {
        try {
            getDao(context).update(pointConfiguration);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(Context context, long id) {
        try {
            getDao(context).delete(getById(context, id));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public PointConfiguration getById(Context context, long id) {
        PointConfiguration pc;
        try {
            pc = getDao(context).queryForId(id);
        } catch (SQLException e) {
            return null;
        }
        return pc;
    }

    @Override
    public List<PointConfiguration> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<PointConfiguration>();
        }
    }

    protected Dao<PointConfiguration, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getPointConfigurationDao();
    }
}
