package fit.cvut.org.cz.tournamentmanager.business;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ISettingManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.PlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.SettingManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class ManagersFactory {
    private static ManagersFactory instance;

    public IPlayerManager playerManager;
    public ISettingManager settingManager;

    public static ManagersFactory getInstance(Context context) {
        if (instance == null) {
            instance = new ManagersFactory();
            instance.playerManager = new PlayerManager(context);
            instance.settingManager = new SettingManager(context);
        }
        return instance;
    }
}
