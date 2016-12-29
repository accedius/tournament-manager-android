package fit.cvut.org.cz.tournamentmanager.business.managers;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.ISettingManager;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;

/**
 * Created by kevin on 6.11.2016.
 */
public class SettingManager extends BaseManager<Setting> implements ISettingManager {
    @Override
    protected Class<Setting> getMyClass() {
        return Setting.class;
    }

    @Override
    public void deleteAll() {
        try {
            managerFactory.getDaoFactory().getMyDao(Setting.class).deleteBuilder().delete();
        } catch (SQLException e) {}
    }

    @Override
    public Setting getByPackageSport(String packageName, String sportName) {
        try {
            List<Setting> settings = managerFactory.getDaoFactory().getMyDao(Setting.class)
                    .queryBuilder().where()
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
