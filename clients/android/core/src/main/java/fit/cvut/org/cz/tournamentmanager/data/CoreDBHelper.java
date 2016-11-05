package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class CoreDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DBName = "TMCore";
    private static final int DBVersion = 4;
    private Dao<Player, Long> playerDAO;
    private Dao<Setting, Long> settingDAO;

    public CoreDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Player.class);
            TableUtils.createTable(connectionSource, Setting.class);
        } catch (SQLException e) {}
        /*
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_1);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_2);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_3);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_4);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_5);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_6);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_7);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Player.class, true);
            TableUtils.dropTable(connectionSource, Setting.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Dao<Player, Long> getPlayerDao() {
        if (playerDAO == null) {
            try {
                playerDAO = getDao(Player.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return playerDAO;
    }

    public Dao<Setting, Long> getSettingDao() {
        if (settingDAO == null) {
            try {
                settingDAO = getDao(Setting.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return settingDAO;
    }
}
