package fit.cvut.org.cz.tmlibrary.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 14.12.2016.
 */

public abstract class TManager<T extends IEntity> implements IManager<T> {
    abstract protected Class<T> getMyClass();

    public void insert(T entity) {
        try {
            ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try {
            ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).update(entity);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(long id) {
        try {
            ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<T> getByColumn(String column, Object value) {
        try {
            return ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).queryForEq(column, value);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public T getById(long id) {
        try {
            return ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return ManagerFactory.getInstance().getDaoFactory().getMyDao(getMyClass()).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<T>();
        }
    }
}