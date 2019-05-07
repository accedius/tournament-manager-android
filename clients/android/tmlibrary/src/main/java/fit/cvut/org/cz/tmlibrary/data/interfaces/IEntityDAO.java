package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface IEntityDAO<E, ID> extends Dao<E, ID>{

    /**
     * Method for obtaining a List of Items from DAO by Id of placeholder
     * @param DBConstant DAO constant of placeholder
     * @param Id ID of placeholder in DAO
     * @return List of Items in placeholder
     * */
    List<E> getListItemById (String DBConstant, Object Id);

    /**
     * Method for deleting placeholder from DAO by Id of placeholder
     * @param DBConstant DAO constant of placeholder
     * @param Id ID of placeholder in DAO
     * @return integer to describe if delete was successful
     * */
    int deleteItemById (String DBConstant, Object Id) throws SQLException;

    /**
     * Method for updating placeholder
     * @param itemToUpdate placeholder to update
     * @return integer to describe if update was successful
     * */
    int updateItem (E itemToUpdate);
}
