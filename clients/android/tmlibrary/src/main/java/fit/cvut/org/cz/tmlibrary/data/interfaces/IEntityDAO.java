package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface IEntityDAO<E, ID> extends Dao<E, ID>{

    /**
     * Method for obtaining a List of Items from DAO by certain value (Id = identification value) in field (column) of a database table
     * @param DBConstant DAO constant of field name
     * @param Id value to be searched for in a field
     * @return List of found Items
     * */
    List<E> getListItemById (String DBConstant, Object Id);

    /**
     * Method for deleting item from DAO by certain value (Id = identification value) in field (column) of a database table
     * @param DBConstant DAO constant of field name
     * @param Id value to be searched for in a field
     * @return integer to describe if delete was successful
     * */
    int deleteItemById (String DBConstant, Object Id) throws SQLException;

    /**
     * Method to update an item
     * @param itemToUpdate item to update
     * @return integer to describe if update was successful
     * */
    int updateItem (E itemToUpdate);
}
