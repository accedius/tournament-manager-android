package fit.cvut.org.cz.tournamentmanager.business;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.PlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.SettingManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class ManagersFactory {
    public static IManager<Player> playerManager() { return new PlayerManager(); }
    public static SettingManager settingManager() { return new SettingManager(); }
}
