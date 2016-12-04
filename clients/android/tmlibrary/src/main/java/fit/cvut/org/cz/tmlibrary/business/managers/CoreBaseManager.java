package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.IEntity;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 4.12.2016.
 */
abstract public class CoreBaseManager<T extends IEntity> implements IManager<T> {
    abstract protected Dao<T, Long> getDao(Context context);

    @Override
    public void insert(Context context, T entity) {
        try {
            getDao(context).create(entity);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, T entity) {
        try {
            getDao(context).update(entity);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(Context context, long id) {
        try {
            getDao(context).delete(getDao(context).queryForId(id));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public T getById(Context context, long id) {
        try {
            return getDao(context).queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<T> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<T>();
        }
    }
}
