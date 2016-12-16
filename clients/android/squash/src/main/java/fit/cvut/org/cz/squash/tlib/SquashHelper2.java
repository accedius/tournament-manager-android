package fit.cvut.org.cz.squash.tlib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.tlib.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class SquashHelper2 extends OrmLiteSqliteOpenHelper implements IDAOFactory {
    private static final int DBVersion = 2;

    private Map<String, Dao<?, Long>> daoMap = new HashMap<>();

    public SquashHelper2(Context context, String name) {
        super(context, name, null, DBVersion);
    }

    public <D extends Dao<E, Long>, E extends IEntity> D getMyDao(E entity) {
        try {
            if (!daoMap.containsKey(entity.getClass().getName())) {
                daoMap.put(entity.getClass().getName(), (Dao<?, Long>)getDao(entity.getClass()));
            }
            return (D) daoMap.get(entity.getClass().getName());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Competition.class);
            TableUtils.createTable(connectionSource, CompetitionPlayer.class);
            TableUtils.createTable(connectionSource, Tournament.class);
            TableUtils.createTable(connectionSource, TournamentPlayer.class);
            TableUtils.createTable(connectionSource, PointConfiguration.class);
            TableUtils.createTable(connectionSource, Team.class);
            TableUtils.createTable(connectionSource, TeamPlayer.class);
            TableUtils.createTable(connectionSource, Match.class);
            TableUtils.createTable(connectionSource, Participant.class);
            TableUtils.createTable(connectionSource, ParticipantStat.class);
            TableUtils.createTable(connectionSource, PlayerStat.class);
        } catch (SQLException e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Competition.class, true);
            TableUtils.dropTable(connectionSource, CompetitionPlayer.class, true);
            TableUtils.dropTable(connectionSource, Tournament.class, true);
            TableUtils.dropTable(connectionSource, TournamentPlayer.class, true);
            TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            TableUtils.dropTable(connectionSource, Team.class, true);
            TableUtils.dropTable(connectionSource, TeamPlayer.class, true);
            TableUtils.dropTable(connectionSource, Match.class, true);
            TableUtils.dropTable(connectionSource, Participant.class, true);
            TableUtils.dropTable(connectionSource, ParticipantStat.class, true);
            TableUtils.dropTable(connectionSource, PlayerStat.class, true);
        } catch (SQLException e) {}
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
