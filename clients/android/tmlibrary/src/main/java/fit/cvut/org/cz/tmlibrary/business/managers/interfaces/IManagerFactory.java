package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Interface for Manager Factory.
 */

public interface IManagerFactory {
    /**
     * Get entity manager by given entity class.
     * @param entity given class.
     * @param <M> class extending manager, this class is returned
     * @param <T> class extending entity, for which should be manager returned
     * @return manager for given entity
     */
    <M extends IManager<T>, T extends IEntity> M getEntityManager(Class<T> entity);

    /**
     * DAO Factory getter
     * @return IDAOFactory instance
     */
    IDAOFactory getDaoFactory();
}
