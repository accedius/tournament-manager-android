package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface IManager<T extends IEntity> {
    /**
     * insert new entity
     * @param entity entity to be inserted
     * @return id of inserted entity
     */
    void insert(T entity);

    /**
     * update entity
     * @param entity entity to be updated
     */
    void update(T entity);

    /**
     * delete entity from app
     * @param id id of entity to be deleted
     * @return true of entity is deleted, false if entity contains something and thus cannot be deleted
     */
    boolean delete(long id);

    /**
     * get entity by its id
     * @param id id of the entity
     * @return found entity
     */
    T getById(long id);

    /**
     * get entities by some column
     * @param column column of the entity
     * @param value value of the column
     * @return found entitys
     */
    List<T> getByColumn(String column, Object value);

    /**
     * get all entities
     * @return all entities
     */
    List<T> getAll();
}
