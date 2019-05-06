package fit.cvut.org.cz.tmlibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

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

    private <E extends IEntity> String getDBConstant (Class<E> clazz) {
        String DBConstant="";
        if(clazz.isInstance(Match.class))
            DBConstant = DBConstants.cTOURNAMENT_ID;
        if(clazz.isInstance(Tournament.class))
            DBConstant = DBConstants.cCOMPETITION_ID;
        if(clazz.isInstance(ParticipantStat.class) || clazz.isInstance(PlayerStat.class) )
            DBConstant = DBConstants.cPARTICIPANT_ID;
        if(clazz.isInstance(Participant.class) )
            DBConstant = DBConstants.cMATCH_ID;
        return DBConstant;
    }

    public <E extends IEntity> List<E> getListDataById (Class<E> clazz, String DBConstant, Object id) {
        try {
            Dao<E, Long> MyDao = getMyDao(clazz);
            //String DBConstant = getDBConstant(clazz);
            return MyDao.queryForEq(DBConstant, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <E extends IEntity> int deleteElement (Class<E> clazz, Object id) throws SQLException {
        try {
            DeleteBuilder<E, Long> builder = getMyDao(clazz).deleteBuilder();
            String DBConstant = getDBConstant(clazz);
            builder.where().eq(DBConstant, id);
            return builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public <E extends IEntity> int update (Class<E> clazz, E objectToUpdate) {
        try {
            return getMyDao(clazz).update(objectToUpdate);
        } catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
