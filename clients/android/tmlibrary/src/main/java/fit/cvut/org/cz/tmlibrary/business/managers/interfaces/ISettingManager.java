package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import fit.cvut.org.cz.tmlibrary.data.entities.Setting;

/**
 * Created by kevin on 4.12.2016.
 */
public interface ISettingManager extends IManager<Setting> {
    /**
     * Delete all settings.
     */
    void deleteAll();

    /**
     * Get Setting by package and sport.
     * @param packageName package name.
     * @param sportName sport name.
     * @return found setting.
     */
    Setting getByPackageSport(String packageName, String sportName);
}
