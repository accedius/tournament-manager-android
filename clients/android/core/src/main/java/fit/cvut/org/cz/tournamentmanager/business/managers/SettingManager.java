package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.data.entities.DSetting;
import fit.cvut.org.cz.tournamentmanager.data.DAOFactory;

/**
 * Created by kevin on 23.10.2016.
 */
public class SettingManager {
    public void insert(Context context, Setting setting) {
        DAOFactory.getInstance().settingDAO.insert(context, Setting.convertToDSetting(setting));
    }

    public void delete(Context context, String packageName, String sportName) {
        DAOFactory.getInstance().settingDAO.delete(context, packageName, sportName);
    }

    public Setting getByPackageSport(Context context, String packageName, String sportName) {
        DSetting dSetting = DAOFactory.getInstance().settingDAO.getByPackageSport(context, packageName, sportName);
        if (dSetting == null)
            return null;
        return new Setting(dSetting);
    }

    public ArrayList<Setting> getAll(Context context) {
        ArrayList<Setting> settings = new ArrayList<>();
        ArrayList<DSetting> dSettings = DAOFactory.getInstance().settingDAO.getAll(context);
        for (DSetting d : dSettings) {
            settings.add(new Setting(d));
        }
        return settings;
    }
}
