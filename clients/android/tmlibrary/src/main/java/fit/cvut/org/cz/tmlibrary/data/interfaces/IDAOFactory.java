package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;

/**
 * Interface for DAO Factory.
 */
public interface IDAOFactory {
    /**
     * Method for getting DAO by given class.
     * @param clazz class for DAO should be got
     * @param <D> DAO interface
     * @param <E> Entity class
     * @return class' DAO interface
     */
    <D extends IEntityDAO<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz);
}
