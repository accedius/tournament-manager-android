package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ISettingManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 6.11.2016.
 */
abstract public class SettingManager extends CoreBaseManager<Setting> implements ISettingManager {
    public SettingManager(Context context) {
        super(context);
    }

    @Override
    public void deleteAll() {
        try {
            getDao().delete(getDao().queryForAll());
        } catch (SQLException e) {}
    }

    @Override
    public Setting getByPackageSport(String packageName, String sportName) {
        try {
            List<Setting> settings = getDao().queryBuilder().where()
                    .eq(DBConstants.cSPORT_NAME, sportName).and()
                    .eq(DBConstants.cPACKAGE_NAME, packageName).query();
            if (settings.isEmpty())
                return null;
            return settings.get(0);
        } catch (SQLException e) {
            return null;
        }
    }
}
