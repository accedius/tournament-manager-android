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
    abstract protected Dao<T, Long> getDao();
    protected Context context;

    public CoreBaseManager(Context context) {
        this.context = context;
    }

    @Override
    public void insert(T entity) {
        try {
            getDao().create(entity);
        } catch (SQLException e) {}
    }

    @Override
    public void update(T entity) {
        try {
            getDao().update(entity);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(long id) {
        try {
            getDao().deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public T getById(long id) {
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return getDao().queryForAll();
        } catch (SQLException e) {
            return new ArrayList<T>();
        }
    }
}
