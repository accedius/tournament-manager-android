package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 6.11.2016.
 */
abstract public class SettingManager implements IManager<Setting> {
    abstract protected Dao<Setting, Long> getDao(Context context);

    public void insert(Context context, Setting setting) {
        try {
            getDao(context).create(setting);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, Setting entity) {}

    @Override
    public boolean delete(Context context, long id) { return false; }

    @Override
    public Setting getById(Context context, long id) { return null; }

    public void deleteAll(Context context) {
        try {
            getDao(context).delete(getDao(context).queryForAll());
        } catch (SQLException e) {}
    }

    public Setting getByPackageSport(Context context, String packageName, String sportName) {
        try {
            List<Setting> settings = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cSPORT_NAME, sportName)
                    .and()
                    .eq(DBConstants.cPACKAGE_NAME, packageName)
                    .query();
            if (settings.isEmpty())
                return null;
            return settings.get(0);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Setting> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<Setting>();
        }
    }
}
