package fit.cvut.org.cz.tmlibrary.business;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 14.12.2016.
 */

public abstract class ManagerFactory implements IManagerFactory {
    protected IDAOFactory daoFactory;

    @Override
    abstract public <M extends IManager<T>, T extends IEntity> M getEntityManager(Class<T> entity);

    public IDAOFactory getDaoFactory() {
        return daoFactory;
    }

    public static ManagerFactory getInstance() {
        throw new UnsupportedOperationException("Method not implmeneted");
    }
}