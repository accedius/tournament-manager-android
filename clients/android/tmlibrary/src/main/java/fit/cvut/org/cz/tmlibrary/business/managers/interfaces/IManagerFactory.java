package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 14.12.2016.
 */

public interface IManagerFactory {
    <M extends IManager<T>, T extends IEntity> M getEntityManager(Class<T> entity);
    IDAOFactory getDaoFactory();
}
