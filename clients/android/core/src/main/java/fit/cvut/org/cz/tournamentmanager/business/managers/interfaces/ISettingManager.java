package fit.cvut.org.cz.tournamentmanager.business.managers.interfaces;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;

/**
 * Interface for Setting Manager.
 */
public interface ISettingManager extends IManager<Setting> {
    /**
     * Delete all settings.
     */
    void deleteAll();

    /**
     * Get Setting by package and sport.
     * @param packageName package name
     * @param sportName sport name
     * @return found setting
     */
    Setting getByPackageSport(String packageName, String sportName);
}
