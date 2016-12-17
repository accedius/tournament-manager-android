package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 30.11.2016.
 */
abstract public class BaseManager<T extends IEntity> implements IManager<T> {
    protected Context context;
    protected SportDBHelper sportDBHelper;
    protected ICorePlayerManager corePlayerManager;

    abstract protected Dao<T, Long> getDao();
    public BaseManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        this.context = context;
        this.corePlayerManager = corePlayerManager;
        this.sportDBHelper = sportDBHelper;
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
