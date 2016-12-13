package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.interfaces.IEntity;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface IManager<T extends IEntity> {
    /**
     * insert new competition
     * @param entity entity to be inserted
     * @return id of inserted competition
     */
    void insert(T entity);

    /**
     * update competition
     * @param entity entity to be updated
     */
    void update(T entity);

    /**
     * delete competition from app
     * @param id id of competition to be deleted
     * @return true of competition is deleted, false if competition contains something and thus cannot be deleted
     */
    boolean delete(long id);

    /**
     * get competition by its id
     * @param id id of the competition
     * @return found competition
     */
    T getById(long id);

    /**
     * get all competitions
     * @return all competitions
     */
    List<T> getAll();
}
