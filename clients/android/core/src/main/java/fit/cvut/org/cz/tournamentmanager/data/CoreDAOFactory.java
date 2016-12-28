package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Setting;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class CoreDAOFactory extends DAOFactory {
    private static final String DBName = "TMCore";
    private static final int DBVersion = 1;

    public CoreDAOFactory(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Player.class);
            TableUtils.createTable(connectionSource, Setting.class);
/*            ArrayList<Player> playerArrayList = new ArrayList<Player>() {
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
            getPlayerDao().create(playerArrayList);*/
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
}
