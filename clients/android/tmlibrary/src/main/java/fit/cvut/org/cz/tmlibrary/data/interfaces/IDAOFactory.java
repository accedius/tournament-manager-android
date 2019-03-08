package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

/**
 * Interface for DAO Factory.
 */
public interface IDAOFactory {
    /**
     * Method for getting DAO by given class.
     * @param clazz class for DAO should be got
     * @param <D> DAO Factory class
     * @param <E> Entity class
     * @return DAO Factory instance
     */
    <D extends Dao<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz);
}
