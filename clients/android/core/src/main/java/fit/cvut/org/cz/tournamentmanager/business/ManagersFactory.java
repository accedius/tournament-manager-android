package fit.cvut.org.cz.tournamentmanager.business;

import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.PlayerManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class ManagersFactory {
    private static ManagersFactory ourInstance = new ManagersFactory();

    public static ManagersFactory getInstance() {
        return ourInstance;
    }

    private ManagersFactory() {
    }

    public IPlayerManager playerManager = new PlayerManager();
}
