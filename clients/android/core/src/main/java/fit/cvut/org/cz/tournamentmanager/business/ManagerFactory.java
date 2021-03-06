package fit.cvut.org.cz.tournamentmanager.business;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tournamentmanager.business.managers.PlayerManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.SettingManager;
import fit.cvut.org.cz.tournamentmanager.data.CoreDAOFactory;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;

/**
 * Manager factory. Class for getting all possible Managers.
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.ManagerFactory {
    private static Context context;
    private static IManagerFactory instance;
    private static IDAOFactory helper;

    @Override
    public <M extends IManager<E>, E extends IEntity> M getEntityManager(Class<E> entity) {
        BaseManager<? extends IEntity> manager = null;

        // Setting
        if (entity.getName().equals(Setting.class.getName())) {
            manager = new SettingManager();
        }
        // Player
        else if (entity.getName().equals(Player.class.getName())) {
            manager = new PlayerManager();
        }

        if (manager == null)
            return null;

        manager.setManagerFactory(this);
        return (M) manager;
    }

    private ManagerFactory() {}

    @Override
    public IDAOFactory getDaoFactory() {
        if (helper == null)
            helper = new CoreDAOFactory(context);
        return helper;
    }

    public static IManagerFactory getInstance(Context c) {
        context = c;
        if (instance == null)
            instance = new ManagerFactory();
        return instance;
    }

    public static void reset() {
        instance = null;
        helper = null;
    }
}