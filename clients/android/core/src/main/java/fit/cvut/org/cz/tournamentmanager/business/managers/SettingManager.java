package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 23.10.2016.
 */
public class SettingManager {
    public void insert(Context context, Setting setting) {
        try {
            Dao<Setting, Long> dao = DatabaseFactory.getDBHelper(context).getSettingDao();
            dao.create(setting);
        } catch (SQLException e) {}
    }

    public void deleteAll(Context context) {
        try {
            Dao<Setting, Long> dao = DatabaseFactory.getDBHelper(context).getSettingDao();
            dao.delete(dao.queryForAll());
        } catch (SQLException e) {}
    }

    public Setting getByPackageSport(Context context, String packageName, String sportName) {
        try {
            Dao<Setting, Long> dao = DatabaseFactory.getDBHelper(context).getSettingDao();
            List<Setting> settings = dao.queryBuilder()
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

    public List<Setting> getAll(Context context) {
        try {
            Dao<Setting, Long> dao = DatabaseFactory.getDBHelper(context).getSettingDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            return new ArrayList<Setting>();
        }
    }
}
