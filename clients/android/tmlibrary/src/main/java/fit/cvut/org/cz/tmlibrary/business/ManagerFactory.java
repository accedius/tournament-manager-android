package fit.cvut.org.cz.tmlibrary.business;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Manager Factory.
 */
public abstract class ManagerFactory implements IManagerFactory {
    @Override
    abstract public <M extends IManager<T>, T extends IEntity> M getEntityManager(Class<T> entity);

    @Override
    abstract public IDAOFactory getDaoFactory();
}