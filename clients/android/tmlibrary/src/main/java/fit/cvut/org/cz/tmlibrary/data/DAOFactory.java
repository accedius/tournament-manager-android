package fit.cvut.org.cz.tmlibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

/**
 * Created by kevin on 5.11.2016.
 */
abstract public class DAOFactory extends OrmLiteSqliteOpenHelper implements IDAOFactory {
    protected String DBName;
    private Map<String, Dao<?, Long>> daoMap = new HashMap<>();

    public DAOFactory(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        DBName = databaseName;
    }

    public <D extends Dao<E, Long>, E extends IEntity> D getAppDao(Class<E> clazz) {
        try {
            if (!daoMap.containsKey(clazz.getName())) {
                daoMap.put(clazz.getName(), (Dao<?, Long>)getDao(clazz));
            }
            return (D) daoMap.get(clazz.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on getAppDao", e);
            return null;
        }
    }

    @Override
    public <D extends IEntityDAO<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz) {
        IEntityDAO<E, Long> itemInterface = new EntityDAO<E, Long>(getAppDao(clazz));
        return (D) itemInterface;
    }

    /*public <D extends Dao<E, Long>, E extends IEntity> D getAppDao(Class<E> clazz) {
        try {
            if (!daoMap.containsKey(clazz.getName())) {
                daoMap.put(clazz.getName(), (Dao<?, Long>)getDao(clazz));
            }
            return (D) daoMap.get(clazz.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on getAppDao", e);
            return null;
        }
    }

    public <D extends EntityDAO<E, Long>, E extends IEntity> D getEntityDao(Class<E> clazz) {
        EntityDAO<E, Long> itemDAO = new EntityDAO<>(getAppDao(clazz));
        return (D) itemDAO;
    }*/

    /*@Override
    public <D extends Dao<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz) {
        try {
            if (!daoMap.containsKey(clazz.getName())) {
                daoMap.put(clazz.getName(), (Dao<?, Long>)getDao(clazz));
            }
            return (D) daoMap.get(clazz.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on getMyDao", e);
            return null;
        }
    }

    public <D extends IEntityDAO<E, Long>, E extends IEntity> D getEntityDAO(Class<E> clazz) {
        IEntityDAO<E, Long> itemInterface = new EntityDAO<E, Long>(getMyDao(clazz));
        return (D) itemInterface;
    }*/
}
