package fit.cvut.org.cz.tournamentmanager.business;

import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ISettingManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.PlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.SettingManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class ManagersFactory {
    private static ManagersFactory instance;

    public IPlayerManager playerManager;
    public ISettingManager settingManager;

    public static ManagersFactory getInstance() {
        if (instance == null) {
            instance = new ManagersFactory();
            instance.playerManager = new PlayerManager();
            instance.settingManager = new SettingManager();
        }
        return instance;
    }
}
