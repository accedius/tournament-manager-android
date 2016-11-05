package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager implements IPlayerManager {
    @Override
    public void insert(Context context, Player player) throws SQLException {
        Dao<Player, Long> dao = DatabaseFactory.getDBHelper(context).getPlayerDao();
        dao.create(player);
    }

    @Override
    public void update(Context context, Player player) throws SQLException {
        Dao<Player, Long> dao = DatabaseFactory.getDBHelper(context).getPlayerDao();
        dao.update(player);
    }

    @Override
    public void delete(Context context, long id) throws SQLException {
        Dao<Player, Long> dao = DatabaseFactory.getDBHelper(context).getPlayerDao();
        dao.delete(dao.queryForId(id));
    }

    @Override
    public Player getById(Context context, long id) throws SQLException {
        Dao<Player, Long> dao = DatabaseFactory.getDBHelper(context).getPlayerDao();
        return dao.queryForId(id);
    }

    @Override
    public ArrayList<Player> getAll(Context context) throws SQLException {
        Dao<Player, Long> dao = DatabaseFactory.getDBHelper(context).getPlayerDao();
        ArrayList<Player> ret = new ArrayList<>();
        for (Player p : dao.queryForAll()) {
            ret.add(p);
        }
        return ret;
    }
}
