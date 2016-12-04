package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.entities.Setting;

/**
 * Created by kevin on 4.12.2016.
 */
public interface ISettingManager extends IManager<Setting> {
    /**
     * Delete all settings.
     * @param context application context.
     */
    void deleteAll(Context context);

    /**
     * Get Setting by package and sport.
     * @param context application context.
     * @param packageName package name.
     * @param sportName sport name.
     * @return found setting.
     */
    Setting getByPackageSport(Context context, String packageName, String sportName);
}
