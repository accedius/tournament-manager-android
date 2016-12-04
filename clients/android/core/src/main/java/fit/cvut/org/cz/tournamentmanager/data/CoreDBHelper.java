package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Setting;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class CoreDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DBName = "TMCore";
    private static final int DBVersion = 5;
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
            ArrayList<Player> playerArrayList = new ArrayList<Player>() {
                {
                    add(new Player(1, "Martin", "martin@seznam.cz", ""));
                    add(new Player(2, "Aleš", "ales@valenta.cz", ""));
                    add(new Player(3, "Jarda", "jarda@frk.cz", ""));
                    add(new Player(4, "Pavel", "pavel@frk.cz", ""));
                    add(new Player(5, "Honza", "honza@frk.cz", ""));
                    add(new Player(6, "Zdeněk", "zdenda@frk.cz", ""));
                    add(new Player(7, "Tomáš", "tomnas@frk.cz", ""));
                    add(new Player(8, "Radek", "Radek@frk.cz", ""));
                }
            };
            getPlayerDao().create(playerArrayList);
        } catch (SQLException e) {}
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
