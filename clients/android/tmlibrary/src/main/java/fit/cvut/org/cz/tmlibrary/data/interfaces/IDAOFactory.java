package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

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

    <E extends IEntity> List<E> getListDataById (Class<E> clazz, String DBConstant, Object Id);

    <E extends IEntity> int deleteElement (Class<E> clazz, Object Id) throws SQLException;

    <E extends IEntity> int update (Class<E> clazz, E objectToUpdate);
}
