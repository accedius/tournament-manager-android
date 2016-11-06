package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.IEntity;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface IManager<T extends IEntity> {
    /**
     * insert new competition
     * @param context application context
     * @param entity entity to be inserted
     * @return id of inserted competition
     */
    void insert(Context context, T entity);

    /**
     * update competition
     * @param context application context
     * @param entity entity to be updated
     */
    void update(Context context, T entity);

    /**
     * delete competition from app
     * @param context application context
     * @param id id of competition to be deleted
     * @return true of competition is deleted, false if competition contains something and thus cannot be deleted
     */
    boolean delete(Context context, long id);

    /**
     * get competition by its id
     * @param context application context
     * @param id id of the competition
     * @return found competition
     */
    T getById(Context context, long id);

    /**
     * get competition by its id
     * @param context application context
     * @return all competitions
     */
    List<T> getAll(Context context);
}
