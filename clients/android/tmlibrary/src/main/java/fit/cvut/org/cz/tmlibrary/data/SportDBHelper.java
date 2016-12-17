package fit.cvut.org.cz.tmlibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 5.11.2016.
 */
abstract public class SportDBHelper extends OrmLiteSqliteOpenHelper implements IDAOFactory {
    protected String DBName;
    private Map<String, Dao<?, Long>> daoMap = new HashMap<>();

    public SportDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        DBName = databaseName;
    }

    @Override
    public <D extends Dao<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz) {
        try {
            if (!daoMap.containsKey(clazz.getName())) {
                daoMap.put(clazz.getName(), (Dao<?, Long>)getDao(clazz));
            }
            return (D) daoMap.get(clazz.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDBName() {
        return DBName;
    }
}
